package com.maxym.algorithm.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Edge {

    private final int trainId;

    private final int fromStation;

    private final int toStation;

    private final double price;
}
