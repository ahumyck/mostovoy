package com.mostovoy_company.expirement.table_experiment.stats.sub_block;

import com.mostovoy_company.expirement.table_experiment.TableViewData;
import com.mostovoy_company.expirement.table_experiment.TableViewRepresentable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlackBlockData implements TableViewRepresentable {
    private double totalBlackCellsAverage;
    private double totalBlackCellsDispersion;

    private double emptyRowsAverage;
    private double emptyRowsDispersion;

    private double blackCellsPerRowAverage;
    private double blackCellsPerRowDispersion;

    @Override
    public TableViewData getDataForTableViewRepresentation(int matrixSize, double percolation) {
        return new TableViewData(
                String.valueOf(matrixSize),
                String.format("%.2f", percolation),
                String.format("%.2f", totalBlackCellsAverage),
                String.format("%.2f", totalBlackCellsDispersion),
                String.format("%.2f", emptyRowsAverage),
                String.format("%.2f", emptyRowsDispersion),
                String.format("%.2f", blackCellsPerRowAverage),
                String.format("%.2f", blackCellsPerRowDispersion)
        );
    }
}
