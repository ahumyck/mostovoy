package com.mostovoy_company.chart;

import com.mostovoy_company.paint.Painter;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.RED_CELLS_STATION_DISTANCES_NE_PI_CHART;

@Component(RED_CELLS_STATION_DISTANCES_NE_PI_CHART)
public class RedCellsStationDistancesNePiChart extends BaseLineChartData {
    public RedCellsStationDistancesNePiChart(Painter painter) {
        super(painter);
    }
}
