package com.mostovoy_company.chart;

import com.mostovoy_company.paint.Painter;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.RED_CELLS_ADDED_CHART;

@Component(RED_CELLS_ADDED_CHART)
public class RedCellsAddedChart extends BaseLineChartData {
    public RedCellsAddedChart(Painter painter) {
        super(painter);
    }

    @Override
    protected double getNormalizedKoef(int size) {
        double Dsize = size;
        return 1.0/ Dsize;
    }
}
