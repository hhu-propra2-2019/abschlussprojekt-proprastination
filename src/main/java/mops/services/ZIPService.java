package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Distribution;
import mops.model.classes.Module;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@SuppressWarnings({"checkstyle:HiddenField", "checkstyle:HideUtilityClassConstructor"})
public class ZIPService {

    private PDFService pdfService;
    private ApplicantService applicantService;
    private ApplicationService applicationService;
    private ModuleService moduleService;
    private StudentService studentService;
    private DistributionService distributionService;

    /**
     *
     * @param pdfService
     * @param applicantService
     * @param applicationService
     * @param moduleService
     * @param studentService
     * @param distributionService
     */
    public ZIPService(final PDFService pdfService, final ApplicantService applicantService,
                      final ApplicationService applicationService, final ModuleService moduleService,
                      final StudentService studentService, final DistributionService distributionService) {
        this.pdfService = pdfService;
        this.applicantService = applicantService;
        this.applicationService = applicationService;
        this.moduleService = moduleService;
        this.studentService = studentService;
        this.distributionService = distributionService;
    }

    /**
     *
     * @param distributions
     * @return
     */
    public String getZipFileForDistribution(final List<Distribution> distributions) {
        return "";
    }

    /**
     * returns path for zipFile
     * @param module
     * @return randomised zipPath
     */
    public File getZipFileForModule(final Module module) {
        File file;
        Applicant applicant;
        List<Application> applicationList = applicationService.findApplicationsByModule(module);
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("bewerbung", ".zip");
            tmpFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tmpFile);
            ZipOutputStream zipOS = new ZipOutputStream(fos);
            for (Application application : applicationList) {
                    applicant = applicantService.findByApplications(application);
                    file = pdfService.generatePDF(application, applicant);
                    writeToZipFile(file, zipOS, applicant);
            }
            zipOS.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpFile;
    }

    /**
     * @return zipPath
     */
    public File getAllZipFiles() {
        File retZip = null;
        List<Module> modules = moduleService.getModules();
        for (Module module : modules) {
                retZip = getZipFileForModule(module);
        }
        return retZip;
    }

    /**
     * writes a file into a zipfile
     * @param file
     * @param zipStream
     * @param applicant
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeToZipFile(final File file,
                                      final ZipOutputStream zipStream, final Applicant applicant) throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(applicant.getFirstName() + "_" + applicant.getSurname() + ".pdf");
        zipStream.putNextEntry(zipEntry);
        final int b = 1024;
        byte[] bytes = new byte[b];
        int length;
        while ((length = fileInputStream.read(bytes)) >= 0) {
            zipStream.write(bytes, 0, length);
        }
        zipStream.closeEntry();
        fileInputStream.close();
    }
}
