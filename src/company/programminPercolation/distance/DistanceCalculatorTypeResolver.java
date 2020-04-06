package company.programminPercolation.distance;

import company.programminPercolation.distance.impl.EdgeDistanceCalculator;
import company.programminPercolation.distance.impl.PythagoreanTheoremCalculator;

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
