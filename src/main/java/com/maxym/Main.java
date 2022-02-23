package com.maxym;

import com.maxym.algorithm.Hamilton;
import com.maxym.algorithm.domain.Edge;
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
        List<Edge> edges = readFile("D:\\temp\\trains_problem\\test_task_data.csv");

        List<Edge> cheapestPath = Hamilton.findCheapestPath(edges);

        System.out.println("The cheapest price to visit all stations according to the bruteforce solution: " + Hamilton.bruteForce(edges));
        System.out.println("The cheapest price to visit all stations: " + cheapestPath.stream().map(Edge::getPrice).reduce(Double::sum));
        System.out.println("The cheapest route to visit all stations: " + cheapestPath);
    }

    private static List<Edge> readFile(String filePath) throws IOException, CsvValidationException {
        List<Edge> edges = new ArrayList<>();

        try (CSVReader csvReader = getReader(filePath)) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                edges.add(Edge.builder()
                              .trainId(Integer.parseInt(values[0]))
                              .toStation(Integer.parseInt(values[2]))
                              .fromStation(Integer.parseInt(values[1]))
                              .price(Double.parseDouble(values[3]))
                              .build());
            }
        }

        return edges;
    }

    private static CSVReader getReader(String filePath) throws FileNotFoundException {
        return new CSVReaderBuilder(new FileReader(filePath))
            .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
            .build();
    }
}
