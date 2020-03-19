package mops.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.ls.LSInput;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CSVServiceTest{
    @Autowired
    CSVService service;

    @AfterEach
    void cleanTestCSV(){
        try {
            new FileWriter("src/test/java/mops/test.csv").close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readFromCSVTest() {
        String csvName = "src/test/java/mops/test.csv";
        String[] module1 = {"propra","21"};
        List<String[]> data = new ArrayList<>();
        data.add(module1);
        List<String[]> readData = new ArrayList<>();
        final Charset charset = Charset.forName("UTF-8");

        service.writeInCSV(csvName,data);
        readData = service.readFromCSV(csvName);

        try {
            CSVReader csvReader = new CSVReader(new FileReader(csvName, charset));
            readData = csvReader.readAll();
            csvReader.close();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        assertThat(readData.size()).isEqualTo(1);
        assertThat(readData.get(0)).isEqualTo(data.get(0));
    }

    @Test
    void writeInCSVTest(){
        String csvName = "src/test/java/mops/test.csv";
        String[] module1 = {"propra","22"};
        List<String[]> data = new ArrayList<>();
        data.add(module1);
        List<String[]> readData = new ArrayList<>();
        final Charset charset = Charset.forName("UTF-8");

        service.writeInCSV(csvName,data);

        try {
            CSVReader csvReader = new CSVReader(new FileReader(csvName, charset));
            readData = csvReader.readAll();
            csvReader.close();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        assertThat(readData.size()).isEqualTo(1);
        assertThat(readData.get(0)).isEqualTo(data.get(0));
    }

    @Test
    void getCountriesTest() {
        List<String> countries;
        String csvname = "src/main/resources/csv/countries.csv";
        List<String[]> data = service.readFromCSV(csvname);

        countries = service.getCountries();

        assertThat(countries.size()).isEqualTo(197);
        for (int i = 0; i<countries.size();i++) {
            String[] tmp = data.get(i);
            assertThat(countries.get(i)).isEqualTo(tmp[0]);
        }
    }

    @Test
    void getModulesWithDetailsTest() {
        List<String[]> data = service.getModulesWithDetails();
        String[] s1 = {"Rechnernetze Datenbanken und Betriebssysteme","RDB", "Jens Bendisposto", "40", "0"};
        String[] s2 = {"Rechnerarchitektur","RA", "Christian Meter", "0", "4"};
        String[] s3 = {"Theoretische Informatik", "Theo Info", "Anna Wintour", "80", "2"};

        assertThat(data.get(0)).isEqualTo(s1);
        assertThat(data.get(1)).isEqualTo(s2);
        assertThat(data.get(2)).isEqualTo(s3);
    }

    @Test
    void getModuleShortNamesTest() {
        List<String> data = service.getShortModuleNames();

        assertThat(data.get(0)).isEqualTo("RDB");
        assertThat(data.get(1)).isEqualTo("RA");
        assertThat(data.get(2)).isEqualTo("Theo Info");
    }

    @Test
    void getModulesTest() {
        List<String> modules;
        String csvname = "src/main/resources/csv/module.csv";
        List<String[]> data = service.readFromCSV(csvname);

        modules = service.getModules();

        assertThat(modules.size()).isEqualTo(3);
        for (int i = 0; i<modules.size();i++) {
            String[] tmp = data.get(i);
            assertThat(modules.get(i)).isEqualTo(tmp[0]);
        }
    }

    @Test
    void readLimitsFromCSV() {
        List<String> limits;

        limits = service.getHourLimits();

        assertThat(limits.size()).isEqualTo(3);
        assertThat(limits.get(0)).isEqualTo("40");
        assertThat(limits.get(1)).isEqualTo("0");
        assertThat(limits.get(2)).isEqualTo("80");
    }

    @Test
    void getPersonLimitsTest() {
        List<String> limits;

        limits = service.getPersonLimits();

        assertThat(limits.size()).isEqualTo(3);
        assertThat(limits.get(0)).isEqualTo("0");
        assertThat(limits.get(1)).isEqualTo("4");
        assertThat(limits.get(2)).isEqualTo("2");
    }

    @Test
    void getModuleProfsTest() {
        List<String> profs;

        profs = service.getModuleProfs();

        assertThat(profs.size()).isEqualTo(3);
        assertThat(profs.get(0)).isEqualTo("Jens Bendisposto");
        assertThat(profs.get(1)).isEqualTo("Christian Meter");
        assertThat(profs.get(2)).isEqualTo("Anna Wintour");
    }
    @Test
    void getProfForModuleTestNotFail() {
        String prof;

        prof = service.getProfForModule("Rechnernetze Datenbanken und Betriebssysteme");

        assertThat(prof).isEqualTo("Jens Bendisposto");
    }

    @Test
    void getProfForModuleTestFail() {
        String prof;

        prof = service.getProfForModule("RDBBBB");

        assertThat(prof).isEqualTo("Not found");
    }

    @Test
    void getModulesForProfTest() {
        List<String> modules;

        modules = service.getModulesForProf("Anna Wintour");

        assertThat(modules.get(0)).isEqualTo("Theoretische Informatik");
    }

    @Test
    void getCourses() {
        List<String> courses;
        String csvname = "src/main/resources/csv/courses.csv";
        List<String[]> data = service.readFromCSV(csvname);

        courses = service.getCourses();

        assertThat(courses.size()).isEqualTo(60);
        for (int i = 0; i<courses.size();i++) {
            String[] tmp = data.get(i);
            assertThat(courses.get(i)).isEqualTo(tmp[0]);
        }
    }

    @Test
    void getSemesterTest() {
        List<String> semester;
        String csvname = "src/main/resources/csv/semester.csv";
        List<String[]> data = service.readFromCSV(csvname);

        semester = service.getSemester();

        assertThat(semester.size()).isEqualTo(29);
        for (int i = 0; i<semester.size();i++) {
            String[] tmp = data.get(i);
            assertThat(semester.get(i)).isEqualTo(tmp[0]);
        }
    }

    @Test
    void deleteModule() {
    }

    @Test
    void cleanModules() {
    }
}