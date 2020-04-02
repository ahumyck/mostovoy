package company.programminPercolation;

import company.entity.Cell;
import company.entity.Matrix;
import company.lightning.Pair;
import company.programminPercolation.boundaryGenerators.RhombusBoundaryGenerator;

import java.util.*;
import java.util.stream.Collectors;

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

    private double getDistance(int x0, int y0, int x, int y){
        int dx = x0 - x;
        int dy = y0 - y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    public List<Pair<Long,Long>> getRatio(){
        List<Pair<Long,Long>> ratio = new ArrayList<>();
        for(Cell percolationCell: this.path){
            int boundary = 1;
            long blackCellsCounter = 0;
            long redCellsCounter = 0;
            if(percolationCell.isWhite())
                redCellsCounter++;
            while (boundary <= this.neighborhood) {
                RhombusBoundaryGenerator generator = new RhombusBoundaryGenerator(boundary, percolationCell, matrix);
                List<Cell> potentialCells = generator.generate();
                blackCellsCounter += potentialCells.stream()
                        .filter(Cell::isBlack)
                        .filter(cell -> !path.contains(cell))
                        .count();
                redCellsCounter += potentialCells.stream()
                        .filter(Cell::isWhite)
                        .filter(cell -> path.contains(cell))
                        .count();
                boundary++;
            }
            ratio.add(new Pair<>(blackCellsCounter,redCellsCounter));
        }
        return ratio;
    }

    public List<PercolationRelation> getProgrammingPercolationList(){
        List<PercolationRelation> percolationRelations = new ArrayList<>();
        for(Cell percolationCell: this.path){
            int x0 = percolationCell.getX();
            int y0 = percolationCell.getY();
            if(percolationCell.isWhite()){
                int boundary = 1;
                while (boundary <= this.neighborhood){
                    RhombusBoundaryGenerator generator = new RhombusBoundaryGenerator(boundary,percolationCell,matrix);
                    List<Cell> potentialCells = generator.generate();
                    List<Cell> optimalCellsCollection = potentialCells.stream()
                            .filter(Cell::isBlack)
                            .filter(cell -> !path.contains(cell))
                            .collect(Collectors.toList());
                    if(!optimalCellsCollection.isEmpty()){
                        optimalCellsCollection.sort(Comparator.comparingDouble(a -> getDistance(x0, y0, a.getX(), a.getY())));
                        Optional<Cell> optionalCell = Optional.empty();
                        for (Cell optimalCell : optimalCellsCollection){
                            if(!this.usedPercolationObjects.contains(optimalCell)){
                                this.usedPercolationObjects.add(optimalCell);
                                optionalCell = Optional.of(optimalCell);
                                break;
                            }
                        }
                        if(optionalCell.isPresent()){
                            Cell goodCell = optionalCell.get();
                            double distance = getDistance(x0,y0,goodCell.getX(),goodCell.getY());
                            percolationRelations.add(new PercolationRelation(goodCell,percolationCell,distance));
                            break;
                        }
                    }
                    boundary++;
                }
            }
        }
        return percolationRelations;
    }

}
