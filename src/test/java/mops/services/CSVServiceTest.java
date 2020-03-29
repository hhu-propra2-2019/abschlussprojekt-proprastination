package mops.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CSVServiceTest{
    @Autowired
    CSVService csvService;

    @AfterEach
    void cleanTestCSV() {
        final Charset charset = StandardCharsets.UTF_8;
        try {
            new FileWriter(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test.csv", charset).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    void deleteTestCSV() {
        File file = new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test.csv");
        file.delete();
    }

    @Test
    void readFromCSVTest() {
        String csvName = System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test.csv";
        String[] module1 = {"propra", "21"};
        List<String[]> data = new ArrayList<>();
        data.add(module1);
        List<String[]> readData;
        final Charset charset = StandardCharsets.UTF_8;

        CSVService.writeInCSV(csvName, data);
        readData = CSVService.readFromCSV(csvName);

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
        String csvName = System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test.csv";
        String[] module1 = {"propra","22"};
        List<String[]> data = new ArrayList<>();
        data.add(module1);
        List<String[]> readData = new ArrayList<>();
        final Charset charset = StandardCharsets.UTF_8;

        CSVService.writeInCSV(csvName, data);

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
        String csvname = System.getProperty("user.dir") + File.separator + "csv" + File.separator + "countries.csv";
        List<String[]> data = CSVService.readFromCSV(csvname);

        countries = CSVService.getCountries();

        assertThat(countries.size()).isEqualTo(256);
        for (int i = 0; i<countries.size();i++) {
            String[] tmp = data.get(i);
            assertThat(countries.get(i)).isEqualTo(tmp[0]);
        }
    }

    @Test
    void getModulesWithDetailsTest() {

    }

    @Test
    void getModuleShortNamesTest() {
        List<String> data = CSVService.getShortModuleNames();

        assertThat(data.get(0)).isEqualTo("RDB");
        assertThat(data.get(1)).isEqualTo("RA");
        assertThat(data.get(2)).isEqualTo("Theo Info");
    }

    @Test
    void getModulesTest() {
        List<String> modules;
        String csvname = System.getProperty("user.dir") + File.separator + "csv" + File.separator + "module.csv";
        List<String[]> data = CSVService.readFromCSV(csvname);

        modules = CSVService.getModules();

        assertThat(modules.size()).isEqualTo(3);
        for (int i = 0; i<modules.size();i++) {
            String[] tmp = data.get(i);
            assertThat(modules.get(i)).isEqualTo(tmp[0]);
        }
    }

    @Test
    void getModuleProfsTest() {
        List<String> profs;

        profs = CSVService.getModuleProfs();

        assertThat(profs.size()).isEqualTo(3);
        assertThat(profs.get(0)).isEqualTo("Jens Bendisposto");
        assertThat(profs.get(1)).isEqualTo("Christian Meter");
        assertThat(profs.get(2)).isEqualTo("Anna Wintour");
    }

    @Test
    void getProfForModuleTestNotFail() {
        String prof;

        prof = CSVService.getProfForModule("Rechnernetze Datenbanken und Betriebssysteme");

        assertThat(prof).isEqualTo("Jens Bendisposto");
    }

    @Test
    void getProfForModuleTestFail() {
        String prof;

        prof = CSVService.getProfForModule("RDBBBB");

        assertThat(prof).isEqualTo("Not found");
    }

    @Test
    void getModulesForProfTest() {
        List<String> modules;

        modules = CSVService.getModulesForProf("Anna Wintour");

        assertThat(modules.get(0)).isEqualTo("Theoretische Informatik");
    }

    @Test
    void getCourses() {
        List<String> courses;
        String csvname = System.getProperty("user.dir") + File.separator + "csv" + File.separator + "courses.csv";
        List<String[]> data = CSVService.readFromCSV(csvname);

        courses = CSVService.getCourses();

        assertThat(courses.size()).isEqualTo(60);
        for (int i = 0; i<courses.size();i++) {
            String[] tmp = data.get(i);
            assertThat(courses.get(i)).isEqualTo(tmp[0]);
        }
    }

    @Test
    void getSemesterTest() {
        List<String> semester;
        String csvname = System.getProperty("user.dir") + File.separator + "csv" + File.separator + "semester.csv";
        List<String[]> data = CSVService.readFromCSV(csvname);

        semester = CSVService.getSemester();

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
