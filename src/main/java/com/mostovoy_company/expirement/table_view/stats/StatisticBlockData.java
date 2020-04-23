package com.mostovoy_company.expirement.table_view.stats;

import com.mostovoy_company.expirement.table_view.stats.sub_block.BlackBlockData;
import com.mostovoy_company.expirement.table_view.stats.sub_block.WhiteBlockDataColumn;
import com.mostovoy_company.expirement.table_view.stats.sub_block.WhiteBlockDataRow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatisticBlockData {
    BlackBlockData blackBlockData;
    WhiteBlockDataColumn whiteBlockDataColumn;
    WhiteBlockDataRow whiteBlockDataRow;
}
