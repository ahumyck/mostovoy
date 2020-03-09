package sample.matrix;

import com.sun.org.apache.bcel.internal.generic.ARETURN;
import sun.security.krb5.internal.CredentialsUtil;

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
    private int clusterMark = 0;

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
