package com.mostovoy_company.expirement.chart_experiment.programminPercolation.distance;

import com.mostovoy_company.expirement.chart_experiment.programminPercolation.distance.calculator.DistanceCalculator;
import com.mostovoy_company.expirement.chart_experiment.programminPercolation.distance.calculator.PythagoreanTheoremCalculator;

public class DistanceCalculatorTypeResolver {

    public static final String PYTHAGORAS = "Пифагор";
    public static final String DISCRETE = "Дискретный";

    public static DistanceCalculator getDistanceCalculator(String type) {
        if (type.equals(PYTHAGORAS)) {
            return new PythagoreanTheoremCalculator();
        } else
            return null;
    }
}
