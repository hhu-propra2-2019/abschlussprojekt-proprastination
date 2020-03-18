package mops.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.After;
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
    void readLimitsFromCSV() {
        List<String> limits;

        limits = service.getHourLimits();

        assertThat(limits.size()).isEqualTo(3);
        assertThat(limits.get(0)).isEqualTo("40");
        assertThat(limits.get(1)).isEqualTo("0");
        assertThat(limits.get(2)).isEqualTo("80");
    }
}