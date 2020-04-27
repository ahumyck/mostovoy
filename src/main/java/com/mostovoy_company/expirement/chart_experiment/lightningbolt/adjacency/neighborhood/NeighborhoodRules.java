package com.mostovoy_company.expirement.chart_experiment.lightningbolt.adjacency.neighborhood;

import com.mostovoy_company.expirement.chart_experiment.lightningbolt.Paired;

import java.util.List;

public interface NeighborhoodRules {
    List<Paired<Integer, Integer>> getRules(int i, int j);
}
