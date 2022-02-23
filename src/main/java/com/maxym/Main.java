package com.maxym;

public class Main {

    public static void main(String[] args) {
        int[][] graph = {{0, 2, 5, 0},
                         {0, 0, 8, 10},
                         {1, 0, 0, 6},
                         {0, 7, 7, 0}};

        System.out.println(Hamilton.findCheapestPathPrice(graph));
    }
}
