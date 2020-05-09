package com.mostovoy_company.expirement.chart_experiment.lightningbolt.cost;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;

public interface CostRules {
    int setCostUsingRules(Cell startCell, Cell endCell, int shiftedSize);
}
