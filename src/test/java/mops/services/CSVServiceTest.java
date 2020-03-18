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

import org.hamcrest.collection.IsEmptyCollection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.MatcherAssert.assertThat;

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

    }
}