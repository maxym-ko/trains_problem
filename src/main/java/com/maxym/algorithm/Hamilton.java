package com.maxym.algorithm;

import com.maxym.algorithm.domain.Connection;
import com.maxym.algorithm.domain.Graph;
import com.maxym.algorithm.domain.TreeNode;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.*;
import java.util.stream.Collectors;

import static com.maxym.algorithm.util.Utils.generatePerm;

public class Hamilton {

    private Hamilton() {
    }

    public static List<Connection> findCheapestPath(List<Connection> connections) {
        BidiMap<Integer, Integer> stationsMap = mapStationsToIndex(connections);
        Graph graph = buildAdjacencyMatrix(connections, stationsMap);

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

    public static double findCheapestPathBruteForce(List<Connection> connections) {
        BidiMap<Integer, Integer> stationsMap = mapStationsToIndex(connections);
        Graph graph = buildAdjacencyMatrix(connections, stationsMap);

        List<List<Integer>> permutations = generatePerm(new ArrayList<>(stationsMap.values()));

        double cheapestPrice = Integer.MAX_VALUE;
        for (List<Integer> permutation : permutations) {
            double currentPrice = 0;
            for (int i = 0; i < permutation.size() - 1; i++) {
                int k = permutation.get(i);
                int m = permutation.get(i + 1);

                if (!graph.haveConnection(k, m)) {
                    currentPrice = Double.MAX_VALUE;
                    break;
                }

                currentPrice += graph.getPrice(k, m);
            }
            if (currentPrice < cheapestPrice) {
                cheapestPrice = currentPrice;
            }
        }

        return cheapestPrice;
    }

    private static BidiMap<Integer, Integer> mapStationsToIndex(List<Connection> connections) {
        Set<Integer> stations = new LinkedHashSet<>();
        for (Connection connection : connections) {
            stations.add(connection.getFromStation());
            stations.add(connection.getToStation());
        }

        int i = 0;
        BidiMap<Integer, Integer> stationMap = new DualHashBidiMap<>();
        for (Integer station : stations) {
            stationMap.put(station, i++);
        }

        return stationMap;
    }

    private static Graph buildAdjacencyMatrix(List<Connection> connections, Map<Integer, Integer> stationsMap) {
        Graph.Node[][] nodes = new Graph.Node[stationsMap.size()][];
        for (int i = 0; i < stationsMap.size(); i++) {
            nodes[i] = new Graph.Node[stationsMap.size()];
        }

        for (Connection connection : connections) {
            int fromStation = stationsMap.get(connection.getFromStation());
            int toStation = stationsMap.get(connection.getToStation());

            if (nodes[fromStation][toStation] == null) {
                nodes[fromStation][toStation] = new Graph.Node(connection.getTrainId(), connection.getPrice());
            } else if (connection.getPrice() < nodes[fromStation][toStation].getPrice()) {
                nodes[fromStation][toStation].setTrainId(connection.getTrainId());
                nodes[fromStation][toStation].setPrice(connection.getPrice());
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

    private static List<Connection> buildFullPath(TreeNode node, Map<Integer, Integer> indexToStation) {
        if (node == null) {
            return Collections.emptyList();
        }

        List<Connection> path = new ArrayList<>();

        TreeNode parent = node.getParent();
        TreeNode child = node;

        while (parent != null) {
            path.add(Connection.builder()
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
