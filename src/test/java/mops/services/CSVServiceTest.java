package mops.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    void getProfForModuleTest() {
    }

    @Test
    void getModuleForProfTest() {
    }

    @Test
    void getCourses() {
    }

    @Test
    void getSemester() {
    }
}