package company.lightning;

import java.util.Objects;

public class Distance implements Comparable<Distance>{
    private int vertex;
    private int distance;

    Distance(int vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    Integer getVertex() {
        return vertex;
    }

    public void setVertex(Integer vertex) {
        this.vertex = vertex;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Distance o) {
//        return o.distance == distance ? vertex - o.vertex : o.distance < this.distance ? 1 : -1;
        return distance -o.distance;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "vertex=" + vertex +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return vertex == distance.vertex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex);
    }
}