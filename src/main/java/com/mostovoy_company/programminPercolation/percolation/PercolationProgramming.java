package com.mostovoy_company.programminPercolation.percolation;

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
    private DistanceCalculator calculator;
    private RhombusBoundaryGenerator generator;


    public PercolationProgramming(Matrix matrix, List<Cell> path) {
        this.path = path;
        this.usedPercolationObjects = new ArrayList<>();
        this.calculator = new PythagoreanTheoremCalculator();
        this.generator = new RhombusBoundaryGenerator(matrix);
    }

    public PercolationProgramming setDistanceCalculator(DistanceCalculator calculator){
        this.calculator = calculator;
        return this;
    }

    private Stream<Cell> streamBlackCells(List<Cell> cells){
       return cells.stream().filter(Cell::isBlack).filter(cell -> !path.contains(cell));
    }

//    public double getRatio(){
//        List<Cell> tape = this.tapeGenerator.generateWideTape(this.neighborhood, this.path);
//        Stream<Cell> cellStream = streamBlackCells(tape);
//        double countBlackCells = cellStream.count();
//        double countDarkRedCells = cellStream.filter(cell -> usedPercolationObjects.contains(cell)).count();
//        return countDarkRedCells/countBlackCells;
//    }

    public List<PercolationRelation> getProgrammingPercolationList(int neighborhood){
        List<PercolationRelation> percolationRelations = new ArrayList<>();
        this.path.stream().map(cell -> findRelationForCurrentCell(cell, neighborhood)).
                forEach(element -> element.ifPresent(percolationRelations::addAll));
//        this.path.stream().filter(Cell::isWhite).map(cell -> findRelationForCurrentCell(cell, neighborhood))
//                .forEach(element -> element.ifPresent(percolationRelations::addAll));
        return percolationRelations;
    }

    private Optional<List<PercolationRelation>> findRelationForCurrentCell(Cell percolationCell,int neighborhood){
        List<Cell> optimalCellsCollection = getOptimalCellsCollection(neighborhood, percolationCell);
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

    private List<Cell> getOptimalCellsCollection(int neighborhood, Cell percolationCell){
        return streamBlackCells(generator.generateFilledArea(neighborhood, percolationCell))
                .sorted(Comparator.comparingDouble(a -> calculator.calculateDistance(a, percolationCell)))
                .collect(Collectors.toList());
    }

}
