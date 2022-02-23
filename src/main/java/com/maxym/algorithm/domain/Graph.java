package com.maxym.algorithm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Graph {

    private final Node[][] adjacencyMatrix;

    public Graph(Node[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public int getTrainId(int i, int j) {
        return adjacencyMatrix[i][j].trainId;
    }

    public double getPrice(int i, int j) {
        return adjacencyMatrix[i][j].price;
    }

    public boolean haveConnection(int i, int j) {
        return adjacencyMatrix[i][j] != null;
    }

    public int getLength() {
        return adjacencyMatrix.length;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Node {
        private int trainId;
        private double price;
    }

}
