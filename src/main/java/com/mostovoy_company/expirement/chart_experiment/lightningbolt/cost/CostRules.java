package com.mostovoy_company.expirement.chart_experiment.lightningbolt.cost;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;

abstract public class CostRules {
    public abstract int setCostUsingRules(Cell startCell, Cell endCell, int shiftedSize);

    protected int setCosts(Cell startCell, Cell endCell, int cheapCost, int expensiveCost) {
        if (startCell.isBlack() && endCell.isBlack())  //black -> black - cheap
            return cheapCost;
        if (startCell.isBlack() && endCell.isWhite()) // black -> white - expensive
            return expensiveCost;
        if (startCell.isWhite() && endCell.isBlack()) // white -> black - cheap
            return cheapCost;
        if (startCell.isWhite() && endCell.isWhite()) // white -> white - twice expensive
            return expensiveCost;
        return 0;
    }
}
