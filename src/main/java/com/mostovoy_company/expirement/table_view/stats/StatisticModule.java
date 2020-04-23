package com.mostovoy_company.expirement.table_view.stats;


import com.mostovoy_company.expirement.table_view.analyzer.AnalyzerData;
import com.mostovoy_company.expirement.table_view.analyzer.AnalyzerDataRepository;
import com.mostovoy_company.expirement.table_view.stats.sub_block.BlackBlockData;
import com.mostovoy_company.expirement.table_view.stats.sub_block.WhiteBlockDataColumn;
import com.mostovoy_company.expirement.table_view.stats.sub_block.WhiteBlockDataRow;
import org.springframework.stereotype.Component;

import java.util.function.ToDoubleFunction;

@Component
public class StatisticModule {
    public StatisticBlockData gatherStatistic(AnalyzerDataRepository repository){
        return new StatisticBlockData(getBlackBlockData(repository),
                getWhiteBlockDataColumn(repository),
                getWhiteBlockDataRow(repository));
    }

    private BlackBlockData getBlackBlockData(AnalyzerDataRepository repository){
        double totalBlackCellsAverage = average(repository, AnalyzerData::getSumBlackCell);
        double totalBlackCellsDispersion = standartDeviation(repository, totalBlackCellsAverage, AnalyzerData::getSumBlackCell);

        double emptyRowsAverage = average(repository, AnalyzerData::getEmptyRowCounter);
        double emptyRowsDispersion = standartDeviation(repository, emptyRowsAverage, AnalyzerData::getEmptyRowCounter);

        double blackCellsPerRowAverage = average(repository, AnalyzerData::getAverageBlackCellsPerRow);
        double blackCellsPerRowDispersion = standartDeviation(repository, blackCellsPerRowAverage, AnalyzerData::getAverageBlackCellsPerRow);

        return new BlackBlockData(totalBlackCellsAverage, totalBlackCellsDispersion,
                emptyRowsAverage, emptyRowsDispersion,
                blackCellsPerRowAverage, blackCellsPerRowDispersion);
    }


    private WhiteBlockDataColumn getWhiteBlockDataColumn(AnalyzerDataRepository repository){
        double whiteCellsPerColumnAverage = average(repository, AnalyzerData::getWhileCellsAveragePerColumn);
        double whiteCellsPerColumnDispersion = standartDeviation(repository, whiteCellsPerColumnAverage, AnalyzerData::getWhileCellsAveragePerColumn);

        double maxWhiteCellsColumnAverage = average(repository, AnalyzerData::getMaxWhiteCellsColumn);
        double maxWhiteCellsColumnDispersion = standartDeviation(repository, maxWhiteCellsColumnAverage, AnalyzerData::getMaxWhiteCellsColumn);

        double minWhiteCellsColumnAverage = average(repository, AnalyzerData::getMinWhiteCellsColumn);
        double minWhiteCellsColumnDispersion = standartDeviation(repository, minWhiteCellsColumnAverage, AnalyzerData::getMinWhiteCellsColumn);

        return new WhiteBlockDataColumn(whiteCellsPerColumnAverage, whiteCellsPerColumnDispersion,
                maxWhiteCellsColumnAverage, maxWhiteCellsColumnDispersion,
                minWhiteCellsColumnAverage, minWhiteCellsColumnDispersion);
    }

    private WhiteBlockDataRow getWhiteBlockDataRow(AnalyzerDataRepository repository){
        double whiteCellsPerRowAverage = average(repository, AnalyzerData::getWhiteCellsAveragePerRow);
        double whiteCellsPerRowDispersion = standartDeviation(repository, whiteCellsPerRowAverage, AnalyzerData::getWhiteCellsAveragePerRow);

        double maxWhiteCellsRowAverage = average(repository, AnalyzerData::getMaxWhiteCellsRow);
        double maxWhiteCellsRowDispersion = standartDeviation(repository, maxWhiteCellsRowAverage, AnalyzerData::getMaxWhiteCellsRow);

        double minWhiteCellsRowAverage = average(repository, AnalyzerData::getMinWhiteCellsRow);
        double minWhiteCellsRowDispersion = standartDeviation(repository, minWhiteCellsRowAverage, AnalyzerData::getMinWhiteCellsRow);

        return new WhiteBlockDataRow(whiteCellsPerRowAverage, whiteCellsPerRowDispersion,
                maxWhiteCellsRowAverage, maxWhiteCellsRowDispersion,
                minWhiteCellsRowAverage, minWhiteCellsRowDispersion);
    }

    private double average(AnalyzerDataRepository repository, ToDoubleFunction<? super AnalyzerData> mapper){
        return repository.stream().mapToDouble(mapper).average().getAsDouble();
    }

    private double standartDeviation(AnalyzerDataRepository repository, double average, ToDoubleFunction<? super AnalyzerData> mapper){
        return Math.sqrt(repository.stream().mapToDouble(mapper)
                .map(d -> (d - average) * (d - average))
                .average().getAsDouble());

    }
}
