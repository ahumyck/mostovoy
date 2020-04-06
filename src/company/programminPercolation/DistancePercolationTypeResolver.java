package company.programminPercolation;

import company.programminPercolation.distance.DistanceCalculator;
import company.programminPercolation.distance.impl.EdgeDistanceCalculator;
import company.programminPercolation.distance.impl.PythagoreanTheoremCalculator;

public class DistancePercolationTypeResolver {

    public static DistanceCalculator getDistanceCalculator(String type) {
        if(type.equals("Пифагор")){
            return new PythagoreanTheoremCalculator();
        }
        else if (type.equals("Не пифагор")){
            return new EdgeDistanceCalculator();
        }
        else
            return null;
    }
}
