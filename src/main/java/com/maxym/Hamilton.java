package com.maxym;

import java.util.List;
import java.util.stream.Collectors;

public class Hamilton {

    private Hamilton() {
    }

    public static double findCheapestPathPrice(int[][] graph) {
        double cheapestPath = Double.MAX_VALUE;

        for (int i = 0; i < graph.length; i++) {
            double price = findCheapestPrice(graph, i);
            cheapestPath = Math.min(price, cheapestPath);
        }

        return cheapestPath;
    }

    private static double findCheapestPrice(int[][] graph, int startStation) {
        TreeNode pathTree = new TreeNode(startStation);
        buildPathTree(graph, pathTree);

        List<TreeNode> endStations = findAllEndStations(pathTree, graph.length);

        return getCheapestPathPrice(endStations);
    }

    private static void buildPathTree(int[][] graph, TreeNode currentNode) {
        int[] prices = graph[currentNode.getStation()];

        for (int i = 0; i < prices.length; i++) {
            if (prices[i] != 0 && !haveParentStation(currentNode, i)) {
                buildPathTree(graph, currentNode.addChild(i, prices[i]));
            }
        }
    }

    private static boolean haveParentStation(TreeNode treeNode, int station) {
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

    private static double getCheapestPathPrice(List<TreeNode> nodes) {
        return nodes.stream()
                    .map(TreeNode::getFullPrice)
                    .min(Double::compareTo)
                    .orElse(Double.MAX_VALUE);
    }
}
