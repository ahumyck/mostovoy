package com.mostovoy_company.expirement.table_experiment.stats.sub_block;

import com.mostovoy_company.expirement.table_experiment.TableViewData;
import com.mostovoy_company.expirement.table_experiment.TableViewRepresentable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WhiteBlockDataRow implements TableViewRepresentable {
    private double whiteCellsPerRowAverage;
    private double whiteCellsPerRowDispersion;

    private double minWhiteCellsRowAverage;
    private double minWhiteCellsRowDispersion;

    private double maxWhiteCellsRowAverage;
    private double maxWhiteCellsRowDispersion;


    @Override
    public TableViewData getDataForTableViewRepresentation(int matrixSize, double percolation) {
        return new TableViewData(
                String.valueOf(matrixSize),
                String.format("%.2f", percolation),
                String.format("%.2f", whiteCellsPerRowAverage),
                String.format("%.2f", whiteCellsPerRowDispersion),
                String.format("%.2f", minWhiteCellsRowAverage),
                String.format("%.2f", minWhiteCellsRowDispersion),
                String.format("%.2f", maxWhiteCellsRowAverage),
                String.format("%.2f", maxWhiteCellsRowDispersion)
        );
    }
}
