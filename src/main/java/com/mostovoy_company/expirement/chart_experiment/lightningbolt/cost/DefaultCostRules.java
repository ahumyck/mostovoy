package com.mostovoy_company.expirement.chart_experiment.lightningbolt.cost;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import org.springframework.stereotype.Component;

@Component
public class DefaultCostRules implements CostRules {
    @Override
    public int setCostUsingRules(Cell startCell, Cell endCell, int shiftedSize) {
        int cheapCost = 1;
        int expensiveCost = shiftedSize * shiftedSize + 1;
        int cost = 0;

        if (startCell.isBlack() && endCell.isBlack())  //black -> black - cheap
            cost = cheapCost;
        if (startCell.isBlack() && endCell.isWhite()) // black -> white - expensive
            cost = expensiveCost;
        if (startCell.isWhite() && endCell.isBlack()) // white -> black - half expensive
            cost = cheapCost;
        if (startCell.isWhite() && endCell.isWhite()) // white -> white - twice expensive
            cost = expensiveCost;

        return cost;
    }
}
