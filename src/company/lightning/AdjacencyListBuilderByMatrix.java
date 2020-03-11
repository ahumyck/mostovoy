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

    private boolean isEmpty(Cell cell){
        return cell.getType().equals(CellType.EMPTY);
    }
    private boolean isBlack(Cell cell) { return cell.getType().equals(CellType.BLACK); }

    void add(Cell startCell, int startShiftedPosition, int i, int j, Matrix matrix){
        int size = matrix.getSize();
        int dis = (size - 2*Matrix.OFFSET) * (size - 2*Matrix.OFFSET) + 1;

        Cell endCell = matrix.getCell(i,j);
        if(!isEmpty(endCell)){
            int endShiftedPosition = (size - 2*Matrix.OFFSET) * (i - Matrix.OFFSET) + (j - Matrix.OFFSET);
            if(isBlack(endCell) && isBlack(startCell))
                map.get(startShiftedPosition).add(new Pair<>(endShiftedPosition,1));
            else
                map.get(startShiftedPosition).add(new Pair<>(endShiftedPosition,dis));
        }
    }

    public Map<Integer, List<Pair<Integer,Integer>>> build(Matrix matrix){
        int size = matrix.getSize();
        for (int i = 0; i < size ; i++) {
            for (int j = 0; j < size; j++) {
                Cell currentCell = matrix.getCell(i,j);
                if (!isEmpty(currentCell)) {
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
