package com.mostovoy_company.analyzer.data_block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlackBlockData
{
    private int sumBlackCell;
    private List<Integer> emptyRows;
    private double averageBlackCells;
}
