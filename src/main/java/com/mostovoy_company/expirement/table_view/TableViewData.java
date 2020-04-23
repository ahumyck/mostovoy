package com.mostovoy_company.expirement.table_view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TableViewData {
    private String size;
    private String percolation;

    private String firstParamAverage;
    private String firstParamDispersion;

    private String secondParamAverage;
    private String secondParamDispersion;

    private String thirdParamAverage;
    private String thirdParamDispersion;
}
