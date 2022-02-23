package com.maxym.algorithm;

import com.maxym.algorithm.domain.TreeNode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Hamilton {

    private Hamilton() {
    }

    public static int[] findCheapestPath(int[][] graph) {
        double cheapestPrice = Double.MAX_VALUE;
        TreeNode lastChild = null;

        for (int i = 0; i < graph.length; i++) {
            TreeNode child = findCheapestPathLastChild(graph, i);
            double price = child.getFullPrice();

            if (price < cheapestPrice) {
                cheapestPrice = price;
                lastChild = child;
            }
        }

        return buildFullPath(lastChild);
    }

    private static TreeNode findCheapestPathLastChild(int[][] graph, int startStation) {
        TreeNode pathTree = new TreeNode(startStation);
        buildPathTree(graph, pathTree);

        List<TreeNode> endStations = findAllEndStations(pathTree, graph.length);

        return findCheapestPathLastChild(endStations);
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

    private static int[] buildFullPath(TreeNode node) {
        if (node == null) {
            return new int[]{};
        }

        int[] path = new int[node.getDepth()];
        int i = 0;
        path[i++] = node.getStation();

        TreeNode parent = node.getParent();
        while (parent != null) {
            path[i++] = parent.getStation();
            parent = parent.getParent();
        }

        reverse(path);
        return path;
    }

    private static void reverse(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }
}
