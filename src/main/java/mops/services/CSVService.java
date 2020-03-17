package mops.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
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
