package mops.services;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CSVServiceTest{
    @Autowired
    CSVService service;

    @After
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
        List<String[]> data = new ArrayList<String[]>();
        data.add(module1);

        service.writeInCSV(csvName,data);
    }
}