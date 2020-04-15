package com.mostovoy_company.chart;

import com.mostovoy_company.paint.Painter;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.CLUSTER_SIZE_CHART;

@Component(CLUSTER_SIZE_CHART)
public class ClusterSizeChart extends BaseLineChartData {

    public ClusterSizeChart(Painter painter) {
        super(painter);
    }

    @Override
    protected double getNormalizedKoef(int size) {
        return 1;
    }
}
