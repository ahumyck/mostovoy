package company.lightning;

import company.entity.Cell;
import company.entity.Matrix;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LightningBolt {
    private Matrix matrix;
    private Map<Integer, List<Pair<Integer, Integer>>> adjacencyList;
    private int[] redCellCounters;
    private int shiftedSize;
    private int indexOfShortestPath;
    private List<Pair<List<Cell>, Integer>> paths;
    private Pair<List<Cell>, Integer> shortestPath = null;

    public LightningBolt(Matrix matrix) {
        this.matrix = matrix;
        this.adjacencyList = new AdjacencyListBuilderByMatrix().build(matrix);
        this.shiftedSize = matrix.getSize() - 2 * Matrix.OFFSET;
        this.redCellCounters = new int[this.shiftedSize];
        this.indexOfShortestPath = -1;
        this.paths = new ArrayList<>(matrix.getSize());
    }

    public int getRedCellCounterForShortestPath() {
        return this.indexOfShortestPath == -1 ? -1 /*error code*/ : this.redCellCounters[this.indexOfShortestPath];
    }

    public int getDistanceForShortestPath() {
        return this.shortestPath == null ? -1 /*error code */ : this.shortestPath.getFirst().size();
    }

    public int getRedCellCounterByIndex(int index) {
        return this.redCellCounters[index] == 0 ? -1 /*error code*/ : this.redCellCounters[index];
    }

    public int getDistanceForPathByIndex(int index) {
        return this.paths.size() == 0 ? -1 /*error code */ : this.paths.get(index).getFirst().size();
    }

    public Optional<Pair<List<Cell>, Integer>> getShortestPath() {
        return Optional.of(this.shortestPath);
    }

    public Optional<Pair<List<Cell>, Integer>> getPathByIndex(int index) {
        return this.paths.size() != 0 ? Optional.of(this.paths.get(index)) : Optional.empty();
    }


    public LightningBolt calculateShortestPaths() {
        for (int currentPos = 0; currentPos < this.shiftedSize; currentPos++) {
            Pair<List<Integer>, List<Integer>> inf = findShortestPaths(currentPos);
            List<Integer> distances = inf.getSecond();
            List<Integer> parents = inf.getFirst();

            int shortest = distances.indexOf(distances.stream().min(Integer::compareTo).get());
            int endPos = this.shiftedSize * (this.shiftedSize - 1) + shortest;
            List<Cell> path = getPath(currentPos, endPos, parents);
            paths.add(new Pair<>(path, distances.get(shortest)));

        }
        this.shortestPath = this.paths.stream().min(Comparator.comparingInt(Pair::getSecond)).get();
        this.indexOfShortestPath = this.paths.indexOf(shortestPath);
        return this;
    }

    private Pair<List<Integer>, List<Integer>> findShortestPaths(int start_pos) {
        int[] distanceToOtherNeighbors = new int[this.adjacencyList.size()];
        for (int i = 0; i < this.adjacencyList.size(); i++) {
            distanceToOtherNeighbors[i] = Integer.MAX_VALUE;
        }
        TreeSet<Distance> distanceToOtherNeighborsMap = new TreeSet<>();
        int[] parents = new int[this.adjacencyList.size()];

        if(this.matrix.getCell(Matrix.OFFSET, Matrix.OFFSET + start_pos).isWhite()) {
            distanceToOtherNeighborsMap.add(new Distance(start_pos, this.shiftedSize * this.shiftedSize + 1));
            distanceToOtherNeighbors[start_pos] = this.shiftedSize * this.shiftedSize + 1;
        }
        else{
            distanceToOtherNeighborsMap.add(new Distance(start_pos, 0));
            distanceToOtherNeighbors[start_pos] = 0;
        }

        int n = this.adjacencyList.keySet().size();
        for (int i = 0; i < n ; i++) {
            final Distance distance = distanceToOtherNeighborsMap.first();
            final int v = distance.getVertex();
            adjacencyList.get(v).forEach(pair -> {
                int to = pair.getFirst();
                int len = pair.getSecond();
                int newValue = distanceToOtherNeighbors[v] + len;
                if (newValue < distanceToOtherNeighbors[to]) {
                    distanceToOtherNeighborsMap.remove(new Distance(to, distanceToOtherNeighbors[to]));
                    distanceToOtherNeighborsMap.add(new Distance(to, newValue));
                    distanceToOtherNeighbors[to] = newValue;
                    parents[to] = v;
                }
            });
            distanceToOtherNeighborsMap.remove(distance);
        }
        return new Pair<>(Arrays.stream(parents).boxed().collect(Collectors.toList()), Arrays.stream(distanceToOtherNeighbors).skip(distanceToOtherNeighbors.length - this.shiftedSize).boxed().collect(Collectors.toList()));
    }

    private List<Cell> getPath(int start, int end, List<Integer> parents) {
        List<Cell> path = new ArrayList<>();
        for (int v = end; ; v = parents.get(v)) {
            smartPathBuilder(v, path, start);
            if (v == start) break;
        }
        return path;
    }

    private Pair<Integer, Integer> getIndecies(int currentPosition) {
        int j = currentPosition % this.shiftedSize;
        int i = (currentPosition - j) / this.shiftedSize;
        return new Pair<>(i, j);
    }

    private void smartPathBuilder(int currentPosition, List<Cell> path, int startPosition) {
        Pair<Integer, Integer> indecies = getIndecies(currentPosition);
        int i = indecies.getFirst();
        int j = indecies.getSecond();
        path.add(0, this.matrix.getCell(i + Matrix.OFFSET, j + Matrix.OFFSET));
        if (this.matrix.getCell(i + Matrix.OFFSET, j + Matrix.OFFSET).isWhite())
            this.redCellCounters[startPosition]++;
    }
}
