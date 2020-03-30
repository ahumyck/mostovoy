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

public class PercolationPathDistances {

    private List<Cell> path;
    private Matrix matrix;
    private List<Cell> usedPercolationObjects;

    public PercolationPathDistances(Matrix matrix, List<Pair<Integer,Integer>> path) {
        this.matrix = matrix;
        this.path = pathConverter(path);
        this.usedPercolationObjects = new ArrayList<>();
    }

    private double getDistance(int x0, int y0, int x, int y){
        int dx = x0 - x;
        int dy = y0 - y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    private List<Cell> pathConverter(List<Pair<Integer,Integer>> path){
        List<Cell> cells = new ArrayList<>();
        for (Pair brick: path){
            cells.add(this.matrix.getCell(brick));
        }
        return cells;
    }


    public List<Double> getDistancesToSetPercolationBlock(){
        List<Double> distances = new ArrayList<>();
//        System.out.println("begin");
        for(Cell percolationCell: this.path){
//            System.out.println("current cell" + percolationCell);
            int x0 = percolationCell.getX();
            int y0 = percolationCell.getY();
            if(percolationCell.isWhite()){
                int boundary = 1;
                while(true){
//                    System.out.println("boundary " + boundary);
                    BoundaryCellsGenerator generator = new BoundaryCellsGenerator(boundary,percolationCell,matrix);
                    List<Cell> potentialCells = generator.generate();
//                    System.out.println("potential: " + potentialCells);
                    List<Cell> optimalCellsCollection = potentialCells.stream()
                            .filter(Cell::isBlack)
                            .filter(cell -> !path.contains(cell))
                            .collect(Collectors.toList());
//                    System.out.println("after filter: " + optimalCellsCollection);
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
//                            String message = MessageFormat.format("Distance added: from [{0}][{1}] to [{2}][{3}] = {4}",
//                                    x0, y0, goodCell.getX(), goodCell.getY(), distance);
//                            System.out.println(message);
                            distances.add(distance);
                            break;
                        }
                    }
                    boundary++;
//                    System.out.println();
//                    System.out.println();
                }
            }
        }
        return distances;
    }

}
