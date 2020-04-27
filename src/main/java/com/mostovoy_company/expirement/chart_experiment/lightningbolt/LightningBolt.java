package com.mostovoy_company.expirement.chart_experiment.lightningbolt;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.adjacency.AdjacencyListBuilderByMatrix;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.adjacency.neighborhood.NeighborhoodRules;

import java.util.*;
import java.util.stream.Collectors;

public class LightningBolt {
    private Matrix matrix;
    private Map<Integer, List<Paired<Integer, Integer>>> adjacencyList;
    private int shiftedSize;
    private List<Paired<List<Cell>, Integer>> paths;
    private Paired<List<Cell>, Integer> shortestPath = null;

    public LightningBolt(Matrix matrix, NeighborhoodRules rules) {
        this.matrix = matrix;
        this.adjacencyList = new AdjacencyListBuilderByMatrix().build(matrix, rules);
        this.shiftedSize = matrix.getSize() - 2 * Matrix.OFFSET;
        this.paths = new ArrayList<>(matrix.getSize());
    }

    public Optional<Paired<List<Cell>, Integer>> getShortestPath() {
        return Optional.of(this.shortestPath);
    }

    public int getDistanceForShortestPath() {
        return Optional.of(this.shortestPath).get().getFirst().size();
    }

    public LightningBolt calculateShortestPaths() {
        for (int currentPos = 0; currentPos < this.shiftedSize; currentPos++) {
            Paired<List<Integer>, List<Integer>> inf = findShortestPaths(currentPos);
            List<Integer> distances = inf.getSecond();
            List<Integer> parents = inf.getFirst();

            int shortest = distances.indexOf(distances.stream().min(Integer::compareTo).get());
            int endPos = this.shiftedSize * (this.shiftedSize - 1) + shortest;
            List<Cell> path = getPath(currentPos, endPos, parents);
            paths.add(new Paired<>(path, distances.get(shortest)));
        }
        this.shortestPath = this.paths.stream().min(Comparator.comparingInt(Paired::getSecond)).get();
        return this;
    }

    private Paired<List<Integer>, List<Integer>> findShortestPaths(int start_pos) {
        int[] distanceToOtherNeighbors = new int[this.adjacencyList.size()];
        for (int i = 0; i < this.adjacencyList.size(); i++) {
            distanceToOtherNeighbors[i] = Integer.MAX_VALUE;
        }
        boolean[] visited = new boolean[this.adjacencyList.size()];
        PriorityQueue<Distance> distanceToOtherNeighborsMap = new PriorityQueue<>();
        int[] parents = new int[this.adjacencyList.size()];

        if (this.matrix.getCell(Matrix.OFFSET, Matrix.OFFSET + start_pos).isWhite()) {
            distanceToOtherNeighborsMap.add(new Distance(start_pos, this.shiftedSize * this.shiftedSize + 1));
            distanceToOtherNeighbors[start_pos] = this.shiftedSize * this.shiftedSize + 1;
        } else {
            distanceToOtherNeighborsMap.add(new Distance(start_pos, 0));
            distanceToOtherNeighbors[start_pos] = 0;
        }

        int n = this.adjacencyList.keySet().size();
        for (int i = 0; i < n; i++) {
            int v = Integer.MAX_VALUE;
            Distance distance;
            do {
                distance = distanceToOtherNeighborsMap.poll();
                if (distance == null) break;
                v = distance.getVertex();
            } while (visited[v]);
            if (distance != null) {
                visited[v] = true;
                final int f_v = v;
                adjacencyList.get(v).forEach(pair -> {
                    int to = pair.getFirst();
                    int len = pair.getSecond();
                    int newValue = distanceToOtherNeighbors[f_v] + len;
                    if (newValue < distanceToOtherNeighbors[to]) {
                        distanceToOtherNeighborsMap.add(new Distance(to, newValue));
                        distanceToOtherNeighbors[to] = newValue;
                        parents[to] = f_v;
                    }
                });
            }
            else break;
        }
        return new Paired<>(Arrays.stream(parents).boxed().collect(Collectors.toList()), Arrays.stream(distanceToOtherNeighbors).skip(distanceToOtherNeighbors.length - this.shiftedSize).boxed().collect(Collectors.toList()));
    }

    private List<Cell> getPath(int start, int end, List<Integer> parents) {
        List<Cell> path = new ArrayList<>();
        for (int v = end; ; v = parents.get(v)) {
            smartPathBuilder(v, path);
            if (v == start) break;
        }
        return path;
    }

    private Paired<Integer, Integer> getIndecies(int currentPosition) {
        int j = currentPosition % this.shiftedSize;
        int i = (currentPosition - j) / this.shiftedSize;
        return new Paired<>(i, j);
    }

    private void smartPathBuilder(int currentPosition, List<Cell> path) {
        Paired<Integer, Integer> indecies = getIndecies(currentPosition);
        int i = indecies.getFirst();
        int j = indecies.getSecond();
        path.add(0, this.matrix.getCell(i + Matrix.OFFSET, j + Matrix.OFFSET));
    }
}
