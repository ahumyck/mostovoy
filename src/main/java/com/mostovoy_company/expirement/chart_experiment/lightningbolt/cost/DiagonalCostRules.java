package com.mostovoy_company.expirement.chart_experiment.lightningbolt.cost;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import org.springframework.stereotype.Component;

@Component
public class DiagonalCostRules implements CostRules {
    @Override
    public int setCostUsingRules(Cell startCell, Cell endCell, int shiftedSize) {
        return 0;
    }
}
