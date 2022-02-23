package com.maxym.algorithm;

import com.maxym.algorithm.domain.Edge;
import com.maxym.algorithm.domain.Graph;
import com.maxym.algorithm.domain.TreeNode;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.*;
import java.util.stream.Collectors;

public class Hamilton {

    private Hamilton() {
    }

    public static List<Edge> findCheapestPath(List<Edge> edges) {
        BidiMap<Integer, Integer> stationsMap = mapStationsToIndex(edges);
        Graph graph = buildAdjacencyMatrix(edges, stationsMap);

        double cheapestPrice = Double.MAX_VALUE;
        TreeNode lastChild = null;

        for (int i = 0; i < graph.getLength(); i++) {
            TreeNode child = findCheapestPathLastChild(graph, i);
            double price = child.getFullPrice();

            if (price < cheapestPrice) {
                cheapestPrice = price;
                lastChild = child;
            }
        }

        return buildFullPath(lastChild, stationsMap.inverseBidiMap());
    }

    private static BidiMap<Integer, Integer> mapStationsToIndex(List<Edge> edges) {
        Set<Integer> stations = new LinkedHashSet<>();
        for (Edge edge : edges) {
            stations.add(edge.getFromStation());
            stations.add(edge.getToStation());
        }

        int i = 0;
        BidiMap<Integer, Integer> stationMap = new DualHashBidiMap<>();
        for (Integer station : stations) {
            stationMap.put(station, i++);
        }

        return stationMap;
    }

    private static Graph buildAdjacencyMatrix(List<Edge> edges, Map<Integer, Integer> stationsMap) {
        Graph.Node[][] nodes = new Graph.Node[stationsMap.size()][];
        for (int i = 0; i < stationsMap.size(); i++) {
            nodes[i] = new Graph.Node[stationsMap.size()];
        }

        for (Edge edge : edges) {
            int fromStation = stationsMap.get(edge.getFromStation());
            int toStation = stationsMap.get(edge.getToStation());

            if (nodes[fromStation][toStation] == null) {
                nodes[fromStation][toStation] = new Graph.Node(edge.getTrainId(), edge.getPrice());
            } else if (edge.getPrice() < nodes[fromStation][toStation].getPrice()) {
                nodes[fromStation][toStation].setTrainId(edge.getTrainId());
                nodes[fromStation][toStation].setPrice(edge.getPrice());
            }
        }

        return new Graph(nodes);
    }

    private static TreeNode findCheapestPathLastChild(Graph graph, int startStation) {
        TreeNode pathTree = new TreeNode(startStation);
        buildPathTree(graph, pathTree);

        List<TreeNode> endStations = findAllEndStations(pathTree, graph.getLength());

        return findCheapestPathLastChild(endStations);
    }

    private static void buildPathTree(Graph graph, TreeNode currentNode) {
        int currentStation = currentNode.getStation();

        for (int i = 0; i < graph.getLength(); i++) {
            if (graph.haveConnection(currentStation, i) && !haveParentStation(currentNode, i)) {
                int trainId = graph.getTrainId(currentStation, i);
                double price = graph.getPrice(currentStation, i);

                buildPathTree(graph, currentNode.addChild(trainId, i, price));
            }
        }
    }

    private static boolean haveParentStation(TreeNode treeNode, int station) {
        if (treeNode.getStation() == station) {
            return true;
        }

        TreeNode parent = treeNode.getParent();

        while (parent != null) {
            if (parent.getStation() == station) {
                return true;
            }
            parent = parent.getParent();
        }

        return false;
    }

    private static List<TreeNode> findAllEndStations(TreeNode startNode, int wantedDepth) {
        List<TreeNode> children = startNode.getChildren();

        for (int i = 0; i < wantedDepth - 2; i++) {
            children = children.stream()
                               .flatMap(node -> node.getChildren().stream())
                               .collect(Collectors.toList());
        }

        return children;
    }

    private static TreeNode findCheapestPathLastChild(List<TreeNode> nodes) {
        return nodes.stream()
                    .min(Comparator.comparingDouble(TreeNode::getFullPrice))
                    .orElse(TreeNode.builder().fullPrice(Double.MAX_VALUE).build());
    }

    private static List<Edge> buildFullPath(TreeNode node, Map<Integer, Integer> indexToStation) {
        if (node == null) {
            return Collections.emptyList();
        }

        List<Edge> path = new ArrayList<>();

        TreeNode parent = node.getParent();
        TreeNode child = node;

        while (parent != null) {
            path.add(Edge.builder()
                         .trainId(child.getTrainId())
                         .fromStation(indexToStation.get(parent.getStation()))
                         .toStation(indexToStation.get(child.getStation()))
                         .price(child.getPrice())
                         .build());
            child = parent;
            parent = parent.getParent();
        }

        Collections.reverse(path);
        return path;
    }
}
