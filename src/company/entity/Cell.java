package company.entity;

import company.entity.CellType;

public class Cell {
    private CellType type;
    private int clusterMark = 0;
    private int x;
    private int y;
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = CellType.EMPTY;
    }

    public Cell(int x, int y, CellType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Cell(CellType type) {
        this.type = type;
    }

    public CellType getType() {
        return type;
    }

    public int getIntType() {
        return type.getValue();
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public int getClusterMark() {
        return clusterMark;
    }

    public boolean hasClusterMark(){
        return clusterMark > 0;
    }

    public void setClusterMark(int clusterMark) {
        this.clusterMark = clusterMark;
    }
}