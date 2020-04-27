package com.mostovoy_company.expirement.chart_experiment.lightningbolt.neighborhood;

import com.mostovoy_company.expirement.chart_experiment.lightningbolt.Paired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultRules implements NeighborhoodRules {
    @Override
    public List<Paired<Integer, Integer>> getRules(int i, int j) {
        List<Paired<Integer,Integer>> rules = new ArrayList<>();
        rules.add(new Paired<>(i - 1, j)); //up
        rules.add(new Paired<>(i + 1, j)); //down
        rules.add(new Paired<>(i, j + 1)); //right
        rules.add(new Paired<>(i, j - 1)); //left
        return rules;
    }
}
