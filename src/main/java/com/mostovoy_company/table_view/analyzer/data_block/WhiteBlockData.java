package com.mostovoy_company.table_view.analyzer.data_block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WhiteBlockData {
    private double whileCellsAveragePerColumn;
    private double whiteCellsAveragePerRow;
    private int maxWhiteCellsColumn;
    private int minWhiteCellsColumn;
    private int maxWhiteCellsRow;
    private int minWhiteCellsRow;
}
