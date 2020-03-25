package mops.services;

import mops.model.classes.Applicant;
import mops.model.classes.Application;
import mops.model.classes.Module;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    /**
     *
     * @param pdfService
     * @param applicantService
     * @param applicationService
     * @param moduleService
     * @param studentService
     */
    public ZIPService(final PDFService pdfService, final ApplicantService applicantService,
                      final ApplicationService applicationService, final ModuleService moduleService,
                      final StudentService studentService) {
        this.pdfService = pdfService;
        this.applicantService = applicantService;
        this.applicationService = applicationService;
        this.moduleService = moduleService;
        this.studentService = studentService;
    }

    /**
     *
     * @param module
     * @param zipPath
     * @return zipPath
     */
    public String getZipFileForModule(final Module module, final String zipPath) {
        String filepath;
        Applicant applicant;
        List<Application> applicationList = applicationService.findApplicationsByModule(module);
        try {
            FileOutputStream fos = new FileOutputStream(zipPath);
            ZipOutputStream zipOS = new ZipOutputStream(fos);
            for (Application application : applicationList) {
                    applicant = applicantService.findByApplications(application);
                    filepath = pdfService.generatePDF(application, applicant);
                    writeToZipFile(filepath, zipOS);
            }
            zipOS.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipPath;
    }

    /**
     *
     * @param zipPath
     * @return zipPath
     */
    public String getAllZipFiles(final String zipPath) {
        String retZip = "";
        List<Module> modules = moduleService.getModules();
        for (Module module : modules) {
                retZip = getZipFileForModule(module, zipPath);
        }
        return retZip;
    }

    /**
     * writes a file into a zipfile
     * @param path
     * @param zipStream
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeToZipFile(final String path,
                                      final ZipOutputStream zipStream) throws FileNotFoundException, IOException {
        File aFile = new File(path);
        FileInputStream fileInputStream = new FileInputStream(aFile);
        ZipEntry zipEntry = new ZipEntry(path);
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
