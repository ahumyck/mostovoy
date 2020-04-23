package com.mostovoy_company.expirement.table_experiment.stats.sub_block;

import com.mostovoy_company.expirement.table_experiment.TableViewData;
import com.mostovoy_company.expirement.table_experiment.TableViewRepresentable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WhiteBlockDataColumn implements TableViewRepresentable {
    private double whiteCellsPerColumnAverage;
    private double whiteCellsPerColumnDispersion;

    private double minWhiteCellsColumnAverage;
    private double minWhiteCellsColumnDispersion;

    private double maxWhiteCellsColumnAverage;
    private double maxWhiteCellsColumnDispersion;


    @Override
    public TableViewData getDataForTableViewRepresentation(int matrixSize, double percolation) {
        return new TableViewData(
                String.valueOf(matrixSize),
                String.format("%.2f", percolation),
                String.format("%.2f", whiteCellsPerColumnAverage),
                String.format("%.2f", whiteCellsPerColumnDispersion),
                String.format("%.2f", minWhiteCellsColumnAverage),
                String.format("%.2f", minWhiteCellsColumnDispersion),
                String.format("%.2f", maxWhiteCellsColumnAverage),
                String.format("%.2f", maxWhiteCellsColumnDispersion)
        );
    }
}
