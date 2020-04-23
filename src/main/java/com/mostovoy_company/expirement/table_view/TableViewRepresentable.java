package com.mostovoy_company.expirement.table_view;

import com.mostovoy_company.expirement.table_view.TableViewData;

public interface TableViewRepresentable {
    TableViewData getDataForTableViewRepresentation(int matrixSize, double percolation);
}
