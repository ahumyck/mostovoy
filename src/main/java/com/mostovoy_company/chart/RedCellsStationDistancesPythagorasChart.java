package com.mostovoy_company.chart;

import com.mostovoy_company.paint.Painter;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.RED_CELLS_STATION_DISTANCES_PYTHAGORAS_CHART;

@Component(RED_CELLS_STATION_DISTANCES_PYTHAGORAS_CHART)
public class RedCellsStationDistancesPythagorasChart extends BaseLineChartData {
    public RedCellsStationDistancesPythagorasChart(Painter painter) {
        super(painter);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1;
    }
}
