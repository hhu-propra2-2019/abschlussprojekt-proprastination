package mops.services;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Der Service braucht keinen Konstruktor
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@Service
public class CSVService {
    /**
     *
     * @param csvName path of file to open
     * @return list of String[]
     */
    public static List<String[]> readFromCSV(final String csvName) {
        List<String[]> list = new ArrayList<>();
        final Charset charset = Charset.forName("UTF-8");
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
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(csvName, true));
            for (String[] s: input) {
                writer.writeNext(s);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return list of countries
     */

    public static List<String> getCountries() {
        List<String> list = new ArrayList<>();
        List<String[]> countries = readFromCSV("src/main/resources/csv/countries.csv");
        String[] strArr;
        for (int i = 0; i < countries.size(); i++) {
            strArr = countries.get(i);
            list.add(strArr[0]);
        }
        return list;
    }

    /**
     * get modules with all details
     * @return return module with details as list of string arrays
     */
    public static List<String[]> getModulesWithDetails() {
        List<String[]> modules = readFromCSV("src/main/resources/csv/module.csv");
        return modules;
    }

    /**
     * return Module from module.csv
     * @return List of all modules
     */
    public static List<String> getModules() {
        List<String> list = new ArrayList<>();
        List<String[]> modules = readFromCSV("src/main/resources/csv/module.csv");
        String[] strArr;
        for (int i = 0; i < modules.size(); i++) {
            strArr = modules.get(i);
            list.add(strArr[0]);
        }
        return list;
    }

    /**
     * Get short name for modules
     */
    public  static List<String> getShortModuleNames() {
        List<String> list = new ArrayList<>();
        List<String[]> moduleNames = readFromCSV("src/main/resources/csv/module.csv");
        String[] strArr;
        for (int i = 0; i < moduleNames.size(); i++) {
            strArr = moduleNames.get(i);
            list.add(strArr[1]);
        }
        return list;
    }

    /**
     * return hour limit for modules
     * @return List of all limits
     */
    public static List<String> getHourLimits() {
        List<String> list = new ArrayList<>();
        List<String[]> limits = readFromCSV("src/main/resources/csv/module.csv");
        String[] strArr;
        for (int i = 0; i < limits.size(); i++) {
            strArr = limits.get(i);
            list.add(strArr[3]);
        }
        return list;
    }

    /**
     * get the limit of workers allowed on a module
     * @return limit of workers as list
     */

    public static List<String> getPersonLimits() {
        List<String> list = new ArrayList<>();
        List<String[]> limits = readFromCSV("src/main/resources/csv/module.csv");
        String[] strArr;
        for (int i = 0; i < limits.size(); i++) {
            strArr = limits.get(i);
            list.add(strArr[4]);
        }
        return list;
    }

    /**
     * Get persons responsible for modules
     * @return responsible persons as list
     */

    public static List<String> getModuleProfs() {
        List<String> list = new ArrayList<>();
        List<String[]> profs = readFromCSV("src/main/resources/csv/module.csv");
        String[] strArr;
        for (int i = 0; i < profs.size(); i++) {
            strArr = profs.get(i);
            list.add(strArr[2]);
        }
        return list;
    }
    /**
     * Get person responsible for single module
     * @return responsible person as String
     * @param moduleName name of the module
     */

    public static String getProfForModule(final String moduleName) {
        List<String> list = new ArrayList<>();
        List<String[]> data = readFromCSV("src/main/resources/csv/module.csv");
        String[] strArr;
        for (int i = 0; i < data.size(); i++) {
            strArr = data.get(i);
            if (strArr[0].equals(moduleName)) {
                return strArr[2];
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
        for (int i = 0; i < profs.size(); i++) {
            strArr = profs.get(i);
            if (strArr[2].equals(profName)) {
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
        List<String> list = new ArrayList<>();
        List<String[]> courses = readFromCSV("src/main/resources/csv/courses.csv");
        String[] strArr;
        for (int i = 0; i < courses.size(); i++) {
            strArr = courses.get(i);
            list.add(strArr[0]);
        }
        return list;
    }

    /**
     * returnes semesters from semester.csv
     * @return List of all Semesters
     */
    public static List<String> getSemester() {
        List<String> list = new ArrayList<>();
        List<String[]> semester = readFromCSV("src/main/resources/csv/semester.csv");
        String[] strArr;
        for (int i = 0; i < semester.size(); i++) {
            strArr = semester.get(i);
            list.add(strArr[0]);
        }
        return list;
    }
}
