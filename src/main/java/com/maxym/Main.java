package com.maxym;

import com.maxym.algorithm.Hamilton;
import com.maxym.algorithm.domain.Connection;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws CsvValidationException, IOException {
        List<Connection> connections = readFile("D:\\temp\\trains_problem\\test_task_data.csv");

        List<Connection> cheapestPath = Hamilton.findCheapestPath(connections);

        System.out.println("The cheapest price to visit all stations according to the bruteforce solution: " +
                           Hamilton.findCheapestPathBruteForce(connections));
        System.out.println("The cheapest price to visit all stations: " +
                           cheapestPath.stream().map(Connection::getPrice).reduce(Double::sum));
        System.out.println("The cheapest route to visit all stations: " +
                           cheapestPath);
    }

    private static List<Connection> readFile(String filePath) throws IOException, CsvValidationException {
        List<Connection> connections = new ArrayList<>();

        try (CSVReader csvReader = getReader(filePath)) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                connections.add(Connection.builder()
                                          .trainId(Integer.parseInt(values[0]))
                                          .toStation(Integer.parseInt(values[2]))
                                          .fromStation(Integer.parseInt(values[1]))
                                          .price(Double.parseDouble(values[3]))
                                          .build());
            }
        }

        return connections;
    }

    private static CSVReader getReader(String filePath) throws FileNotFoundException {
        return new CSVReaderBuilder(new FileReader(filePath))
            .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
            .build();
    }
}
