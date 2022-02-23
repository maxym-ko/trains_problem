package com.maxym;

import com.maxym.algorithm.Hamilton;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[][] graph = {{0, 2, 5, 0},
                         {0, 0, 8, 10},
                         {0, 0, 0, 6},
                         {0, 7, 7, 0}};

        System.out.println(Arrays.toString(Hamilton.findCheapestPath(graph)));
    }
}
