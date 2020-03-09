package mops.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    public static List<String[]> readFromCSV(String csvName){
        List<String[]> help = new ArrayList<>();
        try {
            CSVReader csvReader = new CSVReader(new FileReader(csvName));
            help = csvReader.readAll();
            csvReader.close();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return help;
    }

    public static List<String> getCountries(){
        List<String> list = new ArrayList<>();
        List<String[]> countries = new ArrayList<>();
        countries = readFromCSV("src/main/resources/csv/countries.csv");
        String[] help;
        for(int i = 0; i < countries.size(); i++){
            help = countries.get(i);
            list.add(help[0]);
        }
        return list;
    }
}
