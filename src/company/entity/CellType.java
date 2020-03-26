package company.entity;

public enum CellType{
    WHITE(0),
    BLACK(1),
    EMPTY(4);

    private final int value;
    CellType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
