package company.entity;


import java.text.MessageFormat;

public class Cell {
    private CellType type;
    private int clusterMark = 0;
    private int x;
    private int y;
    public int getX()
    {
        return x - Matrix.OFFSET;
    }

    public int getY()
    {
        return y - Matrix.OFFSET;
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

    public boolean isWhite(){ return type.equals(CellType.WHITE); }

    public boolean isBlack(){ return type.equals(CellType.BLACK); }

    public boolean isEmpty(){ return type.equals(CellType.EMPTY); }

    public boolean isNotEmpty(){ return !type.equals(CellType.EMPTY); }

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

    @Override
    public String toString() {
        return "Cell [" + getX() + "][" + getY() + "]";
    }
}