package company.programminPercolation;

import company.programminPercolation.distance.DistanceCalculator;
import company.programminPercolation.distance.impl.EdgeDistanceCalculator;
import company.programminPercolation.distance.impl.PythagoreanTheoremCalculator;

public class DistancePercolationTypeResolver {

    public static final String PIFAGOR = "Пифагор";
    public static final String NE_PIFAGOR = "Не пифагор";
    public static DistanceCalculator getDistanceCalculator(String type) {
        if(type.equals(PIFAGOR)){
            return new PythagoreanTheoremCalculator();
        }
        else if (type.equals(NE_PIFAGOR)){
            return new EdgeDistanceCalculator();
        }
        else
            return null;
    }
}
