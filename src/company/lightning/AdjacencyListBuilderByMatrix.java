package company.lightning;

import company.entity.Cell;
import company.entity.CellType;
import company.entity.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyListBuilderByMatrix {

    private Map<Integer, List<Pair<Integer,Integer>>> map = new HashMap<>();

    private int setCostUsingRules(Cell startCell, Cell endCell, int shiftedSize){
        int cheapCost = 1;
        int expensiveCost = shiftedSize * shiftedSize + 1;
        int cost = 0;

        if(startCell.isBlack() && endCell.isBlack())  //black -> black - cheap
            cost = cheapCost;
        if(startCell.isBlack() && endCell.isWhite()) // black -> white - expensive
            cost = expensiveCost;
        if(startCell.isWhite() && endCell.isBlack()) // white -> black - expensive
            cost = expensiveCost;
        if(startCell.isWhite() && endCell.isWhite()) // white -> white - expensive
            cost = expensiveCost;

        return cost;
    }

    void add(Cell startCell, int startShiftedPosition, int i, int j, Matrix matrix){
        int shiftedSize = matrix.getSize() - 2*Matrix.OFFSET;
        Cell endCell = matrix.getCell(i,j);
        if(!endCell.isEmpty()){
            int endShiftedPosition = shiftedSize * (i - Matrix.OFFSET) + (j - Matrix.OFFSET);
            int cost = setCostUsingRules(startCell, endCell, shiftedSize);
            map.get(startShiftedPosition).add(new Pair<>(endShiftedPosition, cost));
        }
    }

    public Map<Integer, List<Pair<Integer,Integer>>> build(Matrix matrix){
        int size = matrix.getSize();
        for (int i = 0; i < size ; i++) {
            for (int j = 0; j < size; j++) {
                Cell currentCell = matrix.getCell(i,j);
                if (!currentCell.isEmpty()) {
                    int shiftedPosition = (size - 2*Matrix.OFFSET) * (i - Matrix.OFFSET) + (j - Matrix.OFFSET);
                    map.put(shiftedPosition, new ArrayList<>());
                    add(currentCell,shiftedPosition, i-1,j, matrix); //up
                    add(currentCell,shiftedPosition, i+1,j, matrix); //down
                    add(currentCell,shiftedPosition, i,j+1, matrix); //right
                    add(currentCell,shiftedPosition, i,j-1, matrix); //left
                }
            }
        }
        return map;
    }
}
