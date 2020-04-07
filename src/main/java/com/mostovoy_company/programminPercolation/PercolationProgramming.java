package com.mostovoy_company.programminPercolation;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.programminPercolation.boundaryGenerators.RhombusBoundaryGenerator;
import com.mostovoy_company.programminPercolation.distance.calculator.DistanceCalculator;
import com.mostovoy_company.programminPercolation.distance.calculator.PythagoreanTheoremCalculator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PercolationProgramming {

    private List<Cell> path;
    private List<Cell> usedPercolationObjects;
    private int neighborhood;
    private DistanceCalculator calculator;
    private RhombusBoundaryGenerator generator;
    private TapeGenerator tapeGenerator;


    public PercolationProgramming(Matrix matrix, List<Cell> path) {
        this.path = path;
        this.usedPercolationObjects = new ArrayList<>();
        this.neighborhood = 3;
        this.calculator = new PythagoreanTheoremCalculator();
        this.generator = new RhombusBoundaryGenerator(matrix);
        this.tapeGenerator = new TapeGenerator(matrix);
    }

    public PercolationProgramming setDistanceCalculator(DistanceCalculator calculator){
        this.calculator = calculator;
        return this;
    }

    public PercolationProgramming setNeighborhoodValue(int neighborhood){
        this.neighborhood = neighborhood;
        return this;
    }

    public PercolationProgramming setNeighborhoodDefaultValue(){
        this.neighborhood = 3;
        return this;
    }

    private Stream<Cell> streamBlackCells(List<Cell> cells){
       return cells.stream().filter(Cell::isBlack).filter(cell -> !path.contains(cell));
    }

    public double getRatio(){
        List<Cell> tape = this.tapeGenerator.generateWideTape(this.neighborhood, this.path);
        Stream<Cell> cellStream = streamBlackCells(tape);
        double countBlackCells = cellStream.count();
        double countDarkRedCells = cellStream.filter(cell -> usedPercolationObjects.contains(cell)).count();
        return countDarkRedCells/countBlackCells;
    }

    public List<PercolationRelation> getProgrammingPercolationList(){
        List<PercolationRelation> percolationRelations = new ArrayList<>();
        for(Cell percolationCell: this.path){
            Optional<List<PercolationRelation>> relationForCurrentCell = findRelationForCurrentCell(percolationCell);
            relationForCurrentCell.ifPresent(percolationRelations::addAll);
        }
        return percolationRelations;
    }

    private Optional<List<PercolationRelation>> findRelationForCurrentCell(Cell percolationCell){
        List<Cell> optimalCellsCollection = getOptimalCellsCollection(percolationCell);
        if(!optimalCellsCollection.isEmpty()){
            return Optional.of(getBestOptimalCells(optimalCellsCollection).stream()
                    .map(goodCell -> new PercolationRelation(goodCell, percolationCell,
                            calculator.calculateDistance(percolationCell, goodCell)))
                    .collect(Collectors.toList()));
        }
        return Optional.empty();
    }

    private List<Cell> getBestOptimalCells(List<Cell> optimalCellsCollection){
        List<Cell> bestOptimalCells = new ArrayList<>();
        for (Cell optimalCell : optimalCellsCollection) {
            if (!this.usedPercolationObjects.contains(optimalCell)) {
                this.usedPercolationObjects.add(optimalCell);
                bestOptimalCells.add(optimalCell);
                break;
            }
        }
        return bestOptimalCells;
    }

    private List<Cell> getOptimalCellsCollection(Cell percolationCell){
        return streamBlackCells(generator.generateFilledArea(this.neighborhood,percolationCell))
                .sorted(Comparator.comparingDouble(a -> calculator.calculateDistance(a, percolationCell)))
                .collect(Collectors.toList());
    }

}
