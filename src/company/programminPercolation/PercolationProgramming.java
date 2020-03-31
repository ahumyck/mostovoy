package company.programminPercolation;

import company.entity.Cell;
import company.entity.Matrix;
import company.lightning.Pair;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PercolationProgramming {

    private List<Cell> path;
    private Matrix matrix;
    private List<Cell> usedPercolationObjects;

    public PercolationProgramming(Matrix matrix, List<Cell> path) {
        this.matrix = matrix;
        this.path = path;
        this.usedPercolationObjects = new ArrayList<>();
    }

    private double getDistance(int x0, int y0, int x, int y){
        int dx = x0 - x;
        int dy = y0 - y;
        return Math.sqrt(dx*dx + dy*dy);
    }


    public List<PercolationRelation> getProgrammingPercolationList(){
        List<PercolationRelation> percolationRelations = new ArrayList<>();
        for(Cell percolationCell: this.path){
            int x0 = percolationCell.getX();
            int y0 = percolationCell.getY();
            if(percolationCell.isWhite()){
                int boundary = 1;
                while(true){
                    if(boundary > 2*(matrix.getSize() - 2*Matrix.OFFSET))
                        break;
                    BoundaryCellsGenerator generator = new BoundaryCellsGenerator(boundary,percolationCell,matrix);
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
