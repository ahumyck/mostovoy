package com.mostovoy_company.chart;

import com.mostovoy_company.paint.Painter;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.RED_CELLS_STATION_DISTANCES_DISCRETE_CHART;

@Component(RED_CELLS_STATION_DISTANCES_DISCRETE_CHART)
public class RedCellsStationDistancesDiscreteChart extends BaseLineChartData {
    public RedCellsStationDistancesDiscreteChart(Painter painter) {
        super(painter);
    }

    @Override
    protected double getNormalizedKoef(int size) {
        return 1;
    }
}
