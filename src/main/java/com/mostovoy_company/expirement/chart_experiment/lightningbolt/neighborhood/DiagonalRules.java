package com.mostovoy_company.expirement.chart_experiment.lightningbolt.neighborhood;

import com.mostovoy_company.expirement.chart_experiment.lightningbolt.Paired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DiagonalRules implements NeighborhoodRules {
    @Override
    public List<Paired<Integer, Integer>> getRules(int i, int j) {
        List<Paired<Integer,Integer>> rules = new ArrayList<>();
        rules.add(new Paired<>(i + 1, j + 1)); //right and up
        rules.add(new Paired<>(i + 1, j - 1)); //right and down
        rules.add(new Paired<>(i - 1, j + 1)); //left and up
        rules.add(new Paired<>(i - 1, j - 1)); //left and down
        return rules;
    }
}
