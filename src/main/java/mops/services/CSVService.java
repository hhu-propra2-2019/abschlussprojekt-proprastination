package mops.services;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import mops.model.classes.Module;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Der Service braucht keinen Konstruktor
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@Service
public class CSVService {
    private static final int NAME = 0;
    private static final int SHORT_NAME = 1;
    private static final int PROF_NAME = 2;
    private static final int SEVEN_HOUR_LIMIT = 3;
    private static final int NINE_HOUR_LIMIT = 4;
    private static final int SEVENTEEN_HOUR_LIMIT = 5;
    private static final int HOUR_LIMIT = 6;

    /**
     * Reads from CSV-file
     * @param csvName path of file to open
     * @return list of String[]
     */
    public static List<String[]> readFromCSV(final String csvName) {
        List<String[]> list = new ArrayList<>();
        final Charset charset = StandardCharsets.UTF_8;
        try {
            CSVReader csvReader = new CSVReader(new FileReader(csvName, charset));
            list = csvReader.readAll();
            csvReader.close();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Write in existing CSV file
     * @param csvName file name
     * @param input lines to write into the file
     */
    public static void writeInCSV(final String csvName, final List<String[]> input) {
        final Charset charset = StandardCharsets.UTF_8;
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(csvName, charset, true));
            for (String[] s: input) {
                writer.writeNext(s);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets data from CSV file
     * @param dataPosition Where the data is saved
     * @param csvPath Where the CSV document is
     * @return List with the data
     */
    private static List<String> getCSVData(final int dataPosition, final String csvPath) {
        List<String> output = new ArrayList<>();
        List<String[]> data = readFromCSV(csvPath);
        String[] strArr;
        for (String[] strings : data) {
            strArr = strings;
            output.add(strArr[dataPosition]);
        }
        return output;
    }

    /**
     * deletes module line from module.csv
     * @param moduleName Name of the module
     */
    public static void deleteModule(final String moduleName) {
        List<String[]> readData = readFromCSV("src/main/resources/csv/module.csv");
        List<String[]> writeData = new ArrayList<>();
        String[] tmp;
        for (String[] readDatum : readData) {
            tmp = readDatum;
            if (!(tmp[0].equals(moduleName))) {
                writeData.add(tmp);
            }
        }
        cleanModules();
        writeInCSV("src/main/resources/csv/module.csv", writeData);
    }

    /**
     * Empty out module.csv
     */
    public static void cleanModules() {
        final Charset charset = StandardCharsets.UTF_8;
        try {
            new FileWriter("src/main/resources/csv/module.csv", charset).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * return Module from module.csv
     * @return List of all modules
     */
    public static List<String> getModules() {
        return getCSVData(NAME, "src/main/resources/csv/module.csv");
    }

    /**
     * Get short name for modules
     * @return list of short names
     */
    public  static List<String> getShortModuleNames() {
        return getCSVData(SHORT_NAME, "src/main/resources/csv/module.csv");
    }

    /**
     * Get persons responsible for modules
     * @return responsible persons as list
     */

    public static List<String> getModuleProfs() {
        return getCSVData(PROF_NAME, "src/main/resources/csv/module.csv");
    }

    /**
     * return hour limit for modules
     * @return List of all limits
     */
    public static List<String> getHourLimits() {
        return getCSVData(HOUR_LIMIT, "src/main/resources/csv/module.csv");
    }

    /**
     * Get modules with all details
     * @return return module with details as list of Modules
     */
    public static List<Module> getModulesWithDetails() {
        List<String[]> modules = readFromCSV("src/main/resources/csv/module.csv");
        List<Module> moduleList = new ArrayList<>();
        String[] tmp;
        for (String[] module : modules) {
            tmp = module;
            Module newModule = Module.builder()
                    .name(tmp[NAME])
                    .shortName(tmp[SHORT_NAME])
                    .profName(tmp[PROF_NAME])
                    .sevenHourLimit(tmp[SEVEN_HOUR_LIMIT])
                    .nineHourLimit(tmp[NINE_HOUR_LIMIT])
                    .seventeenHourLimit(tmp[SEVENTEEN_HOUR_LIMIT])
                    .hourLimit(tmp[HOUR_LIMIT])
                    .build();
            moduleList.add(newModule);
        }
        return moduleList;
    }

    /**
     * Gets list of countries
     * @return list of countries
     */
    public static List<String> getCountries() {
        return getCSVData(0, "src/main/resources/csv/countries.csv");
    }

    /**
     * Get person responsible for single module
     * @return responsible person as String
     * @param moduleName name of the module
     */

    public static String getProfForModule(final String moduleName) {
        List<String[]> data = readFromCSV("src/main/resources/csv/module.csv");
        String[] strArr;
        for (String[] datum : data) {
            strArr = datum;
            if (strArr[0].equals(moduleName)) {
                return strArr[PROF_NAME];
            }
        }
        return "Not found";
    }

    /**
     * Get modules asserted to prof/organizer
     * @return modules as list
     * @param profName name of the professor/organizer
     */

    public static List<String> getModulesForProf(final String profName) {
        List<String> list = new ArrayList<>();
        List<String[]> profs = readFromCSV("src/main/resources/csv/module.csv");
        String[] strArr;
        for (String[] prof : profs) {
            strArr = prof;
            if (strArr[PROF_NAME].equals(profName)) {
                list.add(strArr[0]);
            }
        }
        return list;
    }

    /**
     * returns courses from courses.csv
     * @return List of all courses
     */
    public static List<String> getCourses() {
        return getCSVData(0, "src/main/resources/csv/courses.csv");
    }

    /**
     * returnes semesters from semester.csv
     * @return List of all Semesters
     */
    public static List<String> getSemester() {
        return getCSVData(0, "src/main/resources/csv/semester.csv");
    }
}
