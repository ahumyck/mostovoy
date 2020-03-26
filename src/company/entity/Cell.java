package company.entity;



public class Cell {
    private CellType type;
    private int clusterMark = 0;
    private int x;
    private int y;
    public int getX()
    {
        return x - 1;
    }

    public int getY()
    {
        return y - 1;
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
        StringBuilder builder = new StringBuilder();
        builder.append("My cluster mark = ").append(clusterMark)
                .append(" and i am = ");
        switch (type){
            case EMPTY: builder.append("empty"); break;
            case WHITE: builder.append("white"); break;
            case BLACK: builder.append("black"); break;
        }
        return builder.toString();
    }
}