package com.mostovoy_company.chart;

import com.mostovoy_company.paint.Painter;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.CLUSTER_COUNT_CHART;

@Component(CLUSTER_COUNT_CHART)
public class ClusterCountChart extends BaseLineChartData {

    public ClusterCountChart(Painter painter) {
        super(painter);
    }

    @Override
    protected double getNormalizedKoef(int size) {
        double Dsize = size*size;
        return 1.0/ Dsize;
    }
}
