package com.mostovoy_company.chart;

import com.mostovoy_company.paint.Painter;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.WAY_LENGTHS_CHART;

@Component(WAY_LENGTHS_CHART)
public class WayLengthsChart extends BaseLineChartData {
    public WayLengthsChart(Painter painter) {
        super(painter);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0/ size;
    }
}
