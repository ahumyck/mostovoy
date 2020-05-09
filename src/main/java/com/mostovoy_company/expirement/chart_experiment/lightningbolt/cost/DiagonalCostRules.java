package com.mostovoy_company.expirement.chart_experiment.lightningbolt.cost;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import org.springframework.stereotype.Component;

@Component
public class DiagonalCostRules extends CostRules {

    private static int INFINITY = Integer.MAX_VALUE;

    @Override
    public int setCostUsingRules(Cell startCell, Cell endCell, int shiftedSize) {
        int cheapCost = 1;
        int expensiveCost = shiftedSize + 1;

        int cheapPunish = expensiveCost * expensiveCost;
        int expensivePunish = expensiveCost * cheapPunish;

        int dx = endCell.getX() - startCell.getX();
        int dy = endCell.getY() - startCell.getY();

        if (dx == 0) {
            if (dy > 0) {
                return this.setCosts(startCell, endCell, cheapCost, expensiveCost);
            } else {
                return this.setCosts(startCell, endCell, cheapPunish, expensivePunish);
            }
        }
        if (dy == 0) {
            if (dx < 0) {
                return this.setCosts(startCell, endCell, cheapPunish, expensivePunish);
            } else {
                return this.setCosts(startCell, endCell, cheapCost, expensiveCost);
            }
        }

        try {
            throw new Exception("I AM NOT SUPPOSED TO BE HERE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
