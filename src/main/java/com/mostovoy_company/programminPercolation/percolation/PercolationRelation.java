package com.mostovoy_company.programminPercolation.percolation;

import com.mostovoy_company.entity.Cell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PercolationRelation {
    private Cell darkRedCell;
    private Cell redCell;
    private double distance;
}
