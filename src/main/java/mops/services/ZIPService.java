package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Distribution;
import mops.model.classes.Module;
import mops.services.dbServices.ApplicantService;
import mops.services.dbServices.ApplicationService;
import mops.services.dbServices.DbDistributionService;
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

    private PDFService pdfService;
    private ApplicantService applicantService;
    private ApplicationService applicationService;
    private DbDistributionService dbDistributionService;

    /**
     *
     * @param pdfService
     * @param applicantService
     * @param applicationService
     * @param dbDistributionService
     */
    public ZIPService(final PDFService pdfService, final ApplicantService applicantService,
                      final ApplicationService applicationService,
                      final DbDistributionService dbDistributionService) {
        this.pdfService = pdfService;
        this.applicantService = applicantService;
        this.applicationService = applicationService;
        this.dbDistributionService = dbDistributionService;
    }

    /**
     * writes a file into a zipfile
     *
     * @param file
     * @param zipStream
     * @param fileName
     * @throws IOException
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
     * @return Zip File
     */
    public File getZipFileForAllDistributions() throws IOException {
        File file;
        String fileName;
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
                for (Applicant applicant : distribution.getEmployees()) {
                    Optional<Application> application = applicant.getApplications().stream()
                            .filter(app -> app.getModule().equals(distribution.getModule())).findFirst();
                    if (application.isPresent()) {
                        file = pdfService.generatePDF(application.get(), applicant);
                        fileName = (distribution.getModule().getName() + File.separator
                                + applicant.getFirstName() + "_" + applicant.getSurname() + ".pdf");
                        writeToZipFile(file, zipOS, fileName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    /**
     * returns path for zipFile
     *
     * @param modules
     * @return randomised zipPath
     */
    public File getZipFileForModule(final List<Module> modules) throws IOException {
        File file;
        String fileName;
        Applicant applicant;
        File tmpFile = null;
        List<Application> applicationList;
        try {
            tmpFile = File.createTempFile("bewerbung", ".zip");
            tmpFile.deleteOnExit();
        } catch (Exception e) {
            return null;
        }
        try (FileOutputStream fos = new FileOutputStream(tmpFile);
             ZipOutputStream zipOS = new ZipOutputStream(fos)
        ) {

            for (Module module : modules) {
                applicationList = applicationService.findApplicationsByModule(module);
                for (Application application : applicationList) {
                    applicant = applicantService.findByApplications(application);
                    file = pdfService.generatePDF(application, applicant);
                    fileName = (module.getName() + File.separator
                            + applicant.getFirstName() + "_" + applicant.getSurname() + ".pdf");
                    writeToZipFile(file, zipOS, fileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tmpFile;
    }
}
