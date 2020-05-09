package com.mostovoy_company.expirement.chart_experiment.lightningbolt.cost;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import org.springframework.stereotype.Component;

@Component
public class DiagonalCostRules extends CostRules {

    @Override
    public int setCostUsingRules(Cell startCell, Cell endCell, int shiftedSize) {
        int cheapCost = 1;
        int expensiveCost = shiftedSize * shiftedSize + 1;
        int punishCost = expensiveCost * expensiveCost;

        int dx = endCell.getX() - startCell.getX();
        int dy = endCell.getY() - startCell.getY();

        if (dx == 0) {
            //if we are here, it means dy != 0
            if (dy < 0) {
                //if dy > 0 it means start cell is above end cell
                //and its fine
                return this.setCosts(startCell, endCell, cheapCost, cheapCost);
            } //dy == 0 and dx == 0 is not possible
            else {
                //if dy < 0 it means we're looking backwards
                //awful situation, DO NOT REWARD IT
                return this.setCosts(startCell, endCell, punishCost, punishCost);
            }
        }

        if (dy == 0) {
            //if we are here, it means dy != 0
            if (dx < 0) {
                //if dx > 0 it means start cell is to the right of end cell
                //and it's awful situation, PUNISH IT
                return this.setCosts(startCell, endCell, punishCost, punishCost*punishCost);
            } //dy == 0 and dx == 0 is not possible
            else {
                //if dx < 0 it means we're looking towards
                //and its fine
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
