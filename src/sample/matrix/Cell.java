package sample.matrix;

enum CellType{
    WHITE(0),
    BLACK(1),
    RED(2),
    BURGUNDY(3),
    EMPTY(4);

    private final int value;
    CellType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}

public class Cell {
    private CellType type;

    public Cell(){
        this.type = CellType.EMPTY;
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
}
