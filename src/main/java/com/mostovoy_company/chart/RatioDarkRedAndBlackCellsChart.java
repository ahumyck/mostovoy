package com.mostovoy_company.chart;

import com.mostovoy_company.paint.Painter;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.RATIO_DARK_RED_AND_BLACK_CELLS_CHART;

@Component(RATIO_DARK_RED_AND_BLACK_CELLS_CHART)
public class RatioDarkRedAndBlackCellsChart extends BaseLineChartData{
    public RatioDarkRedAndBlackCellsChart(Painter painter) {
        super(painter);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1;
    }
}
