package company.programminPercolation;

import company.entity.Cell;
import company.entity.Matrix;
import company.lightning.Pair;
import company.programminPercolation.boundaryGenerators.BoundaryGenerator;
import company.programminPercolation.boundaryGenerators.RhombusBoundaryGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PercolationProgramming {

    private List<Cell> path;
    private Matrix matrix;
    private List<Cell> usedPercolationObjects;
    private int neighborhood;

    public PercolationProgramming(Matrix matrix, List<Cell> path) {
        this.matrix = matrix;
        this.path = path;
        this.usedPercolationObjects = new ArrayList<>();
        this.neighborhood = 3;
    }

    public PercolationProgramming setNeighborhoodValue(int neighborhood){
        this.neighborhood = neighborhood;
        return this;
    }

    public PercolationProgramming setNeighborhoodDefaultValue(){
        this.neighborhood = 3;
        return this;
    }

    private double getDistance(Cell a, Cell b){
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }

    public List<Pair<Long,Long>> getRatio(){
        List<Pair<Long,Long>> ratio = new ArrayList<>();
        for(Cell percolationCell: this.path){
            long blackCellsCounter = 0;
            long redCellsCounter = 0;
            if(percolationCell.isWhite())
                redCellsCounter++;
            for (int boundary = 1; boundary <= this.neighborhood ; boundary++) {
                RhombusBoundaryGenerator generator = new RhombusBoundaryGenerator(boundary, percolationCell, matrix);
                List<Cell> potentialCells = generator.generate();
                blackCellsCounter += streamBlackCells(potentialCells).count();
                redCellsCounter += streamRedCells(potentialCells).count();
                boundary++;
            }
            ratio.add(new Pair<>(blackCellsCounter,redCellsCounter));
        }
        return ratio;
    }

    private Stream<Cell> streamRedCells(List<Cell> cells){
        return cells.stream().filter(Cell::isWhite).filter(cell -> path.contains(cell));
    }
    private Stream<Cell> streamBlackCells(List<Cell> cells){
       return cells.stream().filter(Cell::isBlack).filter(cell -> !path.contains(cell));
    }

    public List<PercolationRelation> getProgrammingPercolationList(){
        List<PercolationRelation> percolationRelations = new ArrayList<>();
        for(Cell percolationCell: this.path){
            if(percolationCell.isWhite()){
                for (int boundary = 1; boundary <= this.neighborhood ; boundary++) {
                    Optional<PercolationRelation> relationForCurrentCell = findRelationForCurrentCell(percolationCell, boundary);
                    if(relationForCurrentCell.isPresent()){
                        percolationRelations.add(relationForCurrentCell.get());
                        break;
                    }
                }
            }
        }
        return percolationRelations;
    }

    private Optional<PercolationRelation> findRelationForCurrentCell(Cell percolationCell, int boundary){
        RhombusBoundaryGenerator generator = new RhombusBoundaryGenerator(boundary,percolationCell,matrix);
        List<Cell> optimalCellsCollection = getOptimalCellsCollection(generator,percolationCell);
        if(!optimalCellsCollection.isEmpty()){
            Optional<Cell> optionalCell = findOptionalCell(optimalCellsCollection);
            if(optionalCell.isPresent()){
                Cell goodCell = optionalCell.get();
                double distance = getDistance(percolationCell,goodCell);
                return Optional.of(new PercolationRelation(goodCell,percolationCell,distance));
            }
        }
        return Optional.empty();
    }

    private Optional<Cell> findOptionalCell(List<Cell> optimalCellsCollection){
        for (Cell optimalCell : optimalCellsCollection){
            if(!this.usedPercolationObjects.contains(optimalCell)){
                this.usedPercolationObjects.add(optimalCell);
                return Optional.of(optimalCell);
            }
        }
        return Optional.empty();
    }

    private List<Cell> getOptimalCellsCollection(BoundaryGenerator generator, Cell percolationCell){
        return streamBlackCells(generator.generate())
                .sorted(Comparator.comparingDouble(a -> getDistance(a, percolationCell)))
                .collect(Collectors.toList());
    }

}
