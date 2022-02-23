package com.maxym;

import com.maxym.algorithm.Hamilton;
import com.maxym.algorithm.domain.Edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Edge> edges = new ArrayList<>();
        edges.add(Edge.builder().trainId(12).fromStation(1).toStation(2).price(2).build());
        edges.add(Edge.builder().trainId(13).fromStation(1).toStation(3).price(5).build());
        edges.add(Edge.builder().trainId(23).fromStation(2).toStation(3).price(8).build());
        edges.add(Edge.builder().trainId(24).fromStation(2).toStation(4).price(10).build());
        edges.add(Edge.builder().trainId(34).fromStation(3).toStation(4).price(6).build());
        edges.add(Edge.builder().trainId(42).fromStation(4).toStation(2).price(7).build());
        edges.add(Edge.builder().trainId(43).fromStation(4).toStation(3).price(7).build());

        System.out.println(Arrays.toString(Hamilton.findCheapestPath(edges)));
    }
}
