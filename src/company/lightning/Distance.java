package company.lightning;

import java.util.Objects;

public class Distance implements Comparable<Distance>{
    private Integer vertex;
    private Integer distance;

    public Distance(int vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    public Integer getVertex() {
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
        if(o.vertex.equals(this.vertex)) return 0;
        return o.distance < this.distance ? 1 : -1;
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
        return vertex.equals(distance.vertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex);
    }
}