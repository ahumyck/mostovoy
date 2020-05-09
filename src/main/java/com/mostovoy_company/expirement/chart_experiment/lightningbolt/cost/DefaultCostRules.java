package com.mostovoy_company.expirement.chart_experiment.lightningbolt.cost;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import org.springframework.stereotype.Component;

@Component
public class DefaultCostRules extends CostRules {
    @Override
    public int setCostUsingRules(Cell startCell, Cell endCell, int shiftedSize) {
        return this.setCosts(startCell, endCell, 1, shiftedSize * shiftedSize + 1);
    }
}
