package com.maxym.algorithm.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Edge {

    private final int trainId;

    private final int fromStation;

    private final int toStation;

    private final double price;
}
