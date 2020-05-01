package com.mostovoy_company.expirement.chart_experiment.entity;


import com.google.gson.annotations.Expose;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Cell {
    @Expose
    private CellType type;
    @Expose
    private int clusterMark = 0;
    @Expose
    private int x;
    @Expose
    private int y;

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

    public Cell(Cell cell){
        this.x = cell.x;
        this.y = cell.y;
        this.type = cell.type;
        this.clusterMark = cell.clusterMark;
    }

    public int getX() { return x - Matrix.OFFSET; }

    public int getY() { return y - Matrix.OFFSET; }

    public boolean isWhite(){ return type.equals(CellType.WHITE); }

    public boolean isBlack(){ return type.equals(CellType.BLACK); }

    public boolean isEmpty(){ return type.equals(CellType.EMPTY); }

    public CellType getType() {
        return type;
    }

    int getIntType() {
        return type.getValue();
    }

    void setType(CellType type) {
        this.type = type;
    }

    public int getClusterMark() {
        return clusterMark;
    }

    public boolean hasClusterMark(){
        return clusterMark > 0;
    }

    void setClusterMark(int clusterMark) {
        this.clusterMark = clusterMark;
    }

    @Override
    public String toString() {
        return "Cell [" + getX() + "][" + getY() + "] " + type;
    }
}