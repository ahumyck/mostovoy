package com.mostovoy_company.programminPercolation.tape;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.programminPercolation.boundaryGenerators.BoundaryGenerator;
import com.mostovoy_company.programminPercolation.boundaryGenerators.SquareBoundaryGenerator;

import java.util.ArrayList;
import java.util.List;

public class TapeGenerator {
    private BoundaryGenerator generator;

    public TapeGenerator(Matrix matrix) {
        generator = new SquareBoundaryGenerator(matrix);
    }

    public List<Cell> generateWideTape(int bound, List<Cell> path){
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

    public List<Cell> generateTape(int bound, List<Cell> path){
        if(bound == 1){
            return generateWideTape(1,path);
        }
        else {
            List<Cell> outer = generateWideTape(bound,path);
            List<Cell> inner = generateWideTape(bound - 1, path);
            for (Cell in: inner)
                outer.remove(in);
            return outer;
        }
    }

}
