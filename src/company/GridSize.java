package company;

public enum GridSize {
    SIZE_10x10(10),
    SIZE_30x30(30),
    SIZE_50x50(50),
    SIZE_100x100(100);


    GridSize(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
