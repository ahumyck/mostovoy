package company.programminPercolation.boundaryGenerators;

import company.entity.Cell;

import java.util.List;

public interface BoundaryGenerator {
    List<Cell> generate();
}
