package com.mostovoy_company.programminPercolation;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.programminPercolation.boundaryGenerators.BoundaryGenerator;
import com.mostovoy_company.programminPercolation.boundaryGenerators.SquareBoundaryGenerator;

import java.util.ArrayList;
import java.util.List;

class TapeGenerator {
    private BoundaryGenerator generator;

    TapeGenerator(Matrix matrix) {
        generator = new SquareBoundaryGenerator(matrix);
    }

    List<Cell> generateWideTape(int bound, List<Cell> path){
        List<Cell> wideTape = new ArrayList<>();
        for (Cell current: path) {
            List<Cell> square = generator.generateFilledArea(bound, current);
            for (Cell block: square) {
                if(!wideTape.contains(block))
                    wideTape.add(block);
            }
        }
        return wideTape;
    }

}
