package company.lightning;

public class Pair<V,T> {
    private V x;
    private T y;

    public Pair(V x, T y) {
        this.x = x;
        this.y = y;
    }

    public V getX() {
        return x;
    }

    public void setX(V x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" + "end=" + x + ", cost=" + y + '}';
    }
}
