package com.mostovoy_company.programminPercolation.distance;

import com.mostovoy_company.programminPercolation.distance.impl.EdgeDistanceCalculator;
import com.mostovoy_company.programminPercolation.distance.impl.PythagoreanTheoremCalculator;

public class DistanceCalculatorTypeResolver {

    public static final String PYTHAGORAS = "Пифагор";
    public static final String DISCRETE = "Дискретный";
    public static DistanceCalculator getDistanceCalculator(String type) {
        if(type.equals(PYTHAGORAS)){
            return new PythagoreanTheoremCalculator();
        }
        else if (type.equals(DISCRETE)){
            return new EdgeDistanceCalculator();
        }
        else
            return null;
    }
}
