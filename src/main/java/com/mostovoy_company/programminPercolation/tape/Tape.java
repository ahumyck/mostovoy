package com.mostovoy_company.programminPercolation.tape;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;

import java.util.List;
import java.util.stream.Collectors;

public class Tape {
    private TapeGenerator generator;
    private List<Cell> path;

    public Tape(Matrix matrix, List<Cell> path) {
        this.generator = new TapeGenerator(matrix);
        this.path = path;
    }

    public List<Cell> generateTape(int bound){
    return generator.generateTape(bound, path)
            .stream()
            .filter(cell -> !path.contains(cell))
            .collect(Collectors.toList());
    }

    public List<Cell> generateWideTape(int bound){
        return generator.generateWideTape(bound,path)
                .stream()
                .filter(cell -> !path.contains(cell))
                .collect(Collectors.toList());
    }
}
