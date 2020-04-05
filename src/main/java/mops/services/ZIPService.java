package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Distribution;
import mops.model.classes.Module;
import mops.model.classes.Organizer;
import mops.model.classes.webclasses.DownloadProgress;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ApplicationService;
import mops.services.dbServices.DbDistributionService;
import mops.services.dbServices.OrganizerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@SuppressWarnings({"checkstyle:HiddenField", "checkstyle:HideUtilityClassConstructor"})
public class ZIPService {

    private Logger logger = LoggerFactory.getLogger(ZIPService.class);

    private PDFService pdfService;
    private ApplicantService applicantService;
    private ApplicationService applicationService;
    private DbDistributionService dbDistributionService;
    private OrganizerService organizerService;

    /**
     * @param pdfService            Retrieves/receives PDF data
     * @param applicantService      Retrieves/receives Applicant data
     * @param applicationService    Retrieves/receives Application data
     * @param dbDistributionService Retrieves Distribution data
     * @param organizerService      organizerservice
     */
    public ZIPService(final PDFService pdfService, final ApplicantService applicantService,
                      final ApplicationService applicationService,
                      final DbDistributionService dbDistributionService,
                      final OrganizerService organizerService) {
        this.pdfService = pdfService;
        this.applicantService = applicantService;
        this.applicationService = applicationService;
        this.dbDistributionService = dbDistributionService;
        this.organizerService = organizerService;
    }

    /**
     * writes a file into a zipfile
     *
     * @param file      File
     * @param zipStream ZipStream
     * @param fileName  filename
     * @throws IOException IoException
     */
    public static void writeToZipFile(final File file,
                                      final ZipOutputStream zipStream,
                                      final String fileName) throws IOException {
        final int b = 1024;
        byte[] bytes = new byte[b];
        int length;

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipStream.putNextEntry(zipEntry);
            while ((length = fileInputStream.read(bytes)) >= 0) {
                zipStream.write(bytes, 0, length);
            }
        }
    }

    /**
     * Returns all Distributed Applications as PDFs
     *
     * @param downloadProgress Progress
     * @return Zip File
     */
    public File getZipFileForAllDistributions(final DownloadProgress downloadProgress) throws IOException {
        File tmpFile = null;
        List<Distribution> distributions = dbDistributionService.findAll();
        FileOutputStream fos = null;
        ZipOutputStream zipOS = null;
        try {
            tmpFile = File.createTempFile("bewerbung", ".zip");
            tmpFile.deleteOnExit();
            fos = new FileOutputStream(tmpFile);
            zipOS = new ZipOutputStream(fos);
            for (Distribution distribution : distributions) {
                writeFilesForDistribution(zipOS, distribution, downloadProgress);
            }
        } catch (IOException e) {
            logger.warn(e.getMessage());
        } finally {
            if (zipOS != null) {
                zipOS.close();
            }
            if (fos != null) {
                fos.close();
            }
        }

        return tmpFile;
    }

    private void writeFilesForDistribution(final ZipOutputStream zipOS, final Distribution distribution,
                                           final DownloadProgress downloadProgress) throws IOException {
        Organizer organizer;
        File file;
        String fileName;
        for (Applicant applicant : distribution.getEmployees()) {
            Optional<Application> application = applicant.getApplications().stream()
                    .filter(app -> app.getModule().equals(distribution.getModule())).findFirst();
            if (application.isPresent()) {
                downloadProgress.addSize(1);
                organizer = organizerService.findByUniserial(application.get().getModule().getProfSerial());
                file = pdfService.generatePDF(application.get(), applicant, organizer);
                fileName = (distribution.getModule().getName() + File.separator
                        + applicant.getFirstName() + "_" + applicant.getSurname() + ".pdf");
                writeToZipFile(file, zipOS, fileName);
                downloadProgress.addProgress();
                boolean deleted = file.delete();
                if (!deleted) {
                    logger.warn("Could not delete File: " + file.getName() + " on Path: "
                            + file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * returns path for zipFile
     *
     * @param modules          modules
     * @param downloadProgress Progress
     * @return randomised zipPath
     */
    public File getZipFileForModule(final List<Module> modules, final DownloadProgress downloadProgress) {
        File file;
        String fileName;
        Applicant applicant;
        File tmpFile;
        Organizer organizer;
        List<Application> applicationList;
        try {
            tmpFile = File.createTempFile("bewerbung", ".zip");
            tmpFile.deleteOnExit();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return null;
        }
        try (FileOutputStream fos = new FileOutputStream(tmpFile);
             ZipOutputStream zipOS = new ZipOutputStream(fos)
        ) {

            for (Module module : modules) {
                applicationList = applicationService.findApplicationsByModule(module);
                downloadProgress.addSize(applicationList.size());
                for (Application application : applicationList) {
                    organizer = organizerService.findByUniserial(application.getModule().getProfSerial());
                    applicant = applicantService.findByApplications(application);
                    file = pdfService.generatePDF(application, applicant, organizer);
                    fileName = (module.getName() + File.separator
                            + applicant.getFirstName().replaceAll("[^A-Za-z0-9.,ß]", "") + "_"
                            + applicant.getSurname().replaceAll("[^A-Za-z0-9.,ß]", "") + ".pdf");
                    writeToZipFile(file, zipOS, fileName);
                    boolean deleted = file.delete();
                    if (!deleted) {
                        logger.warn("Could not delete File: " + file.getName() + " on Path: " + file.getAbsolutePath());
                    }
                    downloadProgress.addProgress();
                }
            }
        } catch (IOException e) {
            logger.warn("Error creating Application PDF");
            logger.debug(e.getMessage());
        }
        downloadProgress.setFinished(true);
        return tmpFile;
    }

    /**
     * Returns all Distributed Applications for a given module as PDFs
     *
     * @param module           module
     * @param downloadProgress Progress
     * @return Zip File
     */
    public File getZipFileForModuleDistributions(final Module module,
                                                 final DownloadProgress downloadProgress) throws IOException {
        File tmpFile = null;
        FileOutputStream fos = null;
        ZipOutputStream zipOS = null;
        try {
            tmpFile = File.createTempFile("bewerbung", ".zip");
            tmpFile.deleteOnExit();
            fos = new FileOutputStream(tmpFile);
            zipOS = new ZipOutputStream(fos);
            Distribution distribution = dbDistributionService.findByModule(module);
            writeFilesForDistribution(zipOS, distribution, downloadProgress);
        } catch (IOException e) {
            logger.warn(e.getMessage());
        } finally {
            if (zipOS != null) {
                zipOS.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return tmpFile;
    }
}
