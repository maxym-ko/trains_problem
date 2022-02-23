package com.maxym.algorithm.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TreeNode {

    private final int station;

    private final double price;

    private final double fullPrice;

    private final TreeNode parent;

    private final List<TreeNode> children;

    private final int depth;

    public TreeNode(int station) {
        this(station, 0, 0, null, 1);
    }

    private TreeNode(int station, double price, double fullPrice, TreeNode parent, int depth) {
        this.station = station;
        this.price = price;
        this.fullPrice = fullPrice;
        this.children = new LinkedList<>();
        this.parent = parent;
        this.depth = depth;
    }

    public TreeNode addChild(int station, double price) {
        TreeNode childNode = new TreeNode(station, price, this.fullPrice + price, this, this.depth + 1);
        this.children.add(childNode);

        return childNode;
    }
}