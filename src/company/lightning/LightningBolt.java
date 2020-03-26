package company.lightning;

import company.entity.Cell;
import company.entity.Matrix;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LightningBolt {

    private Matrix matrix;
    private Map<Integer, List<Pair<Integer, Integer>>> adjacencyList;
    private int[] redCellCounters;
    private int shiftedSize;
    private int indexOfShortestPath;
    private boolean isShortedPathFound;

    public LightningBolt(Matrix matrix) {
        this.matrix = matrix;
        this.adjacencyList = new AdjacencyListBuilderByMatrix().build(matrix);
        this.shiftedSize = matrix.getSize() - 2 * Matrix.OFFSET;
        this.redCellCounters = new int[shiftedSize];
        this.isShortedPathFound = false;
        this.indexOfShortestPath = -1;
    }

    public int getRedCellCounter(){
        if(isShortedPathFound)
            return this.redCellCounters[this.indexOfShortestPath];
        return -1; // Error Code
    }

    public Pair<List<Pair<Integer,Integer>>,Integer> findShortestWay() {
        List<Pair<List<Pair<Integer,Integer>>,Integer>> paths = new ArrayList<>();
        for(int currentPos = 0 ; currentPos < this.shiftedSize; currentPos++){
            Pair<List<Integer>, List<Integer>> inf = findShortestWays(currentPos);
            List<Integer> distances = inf.getSecond();
            List<Integer> parents = inf.getFirst();

            int shortest = distances.indexOf(distances.stream().min(Integer::compareTo).get());
            int endPos = this.shiftedSize*(this.shiftedSize - 1) + shortest;
            List<Pair<Integer, Integer>> path = getPath(currentPos, endPos, parents);

            paths.add(new Pair<>(path,distances.get(shortest)));
        }
        Pair<List<Pair<Integer, Integer>>, Integer> shortestPath = paths.stream().min(Comparator.comparingInt(Pair::getSecond)).get();
        this.indexOfShortestPath = paths.indexOf(shortestPath);
        this.isShortedPathFound = true;
        return shortestPath;
    }


    private Pair<List<Integer>, List<Integer>> findShortestWays(int start_pos){
        List<Integer> distanceToOtherNeighbors = initWith(Integer.MAX_VALUE,this.adjacencyList.size());
        List<Integer> parents = initWith(0,this.adjacencyList.size());
        List<Boolean> visitedNeighbors = initWith(false,this.adjacencyList.size());

        distanceToOtherNeighbors.set(start_pos,0);
        int n = this.adjacencyList.keySet().size();
        for (int i = 0; i < n; i++) {
            int v = -1;
            for (int j = 0; j < n; j++) {
                if(!visitedNeighbors.get(j) && (v == -1 || distanceToOtherNeighbors.get(j) < distanceToOtherNeighbors.get(v)))
                    v = j;
            }

            if(distanceToOtherNeighbors.get(v) == Integer.MAX_VALUE)
                break;

            visitedNeighbors.set(v,true);
            for (int j = 0; j < this.adjacencyList.get(v).size(); j++) {
                Pair<Integer,Integer> pair = this.adjacencyList.get(v).get(j);
                int to = pair.getFirst();
                int len = pair.getSecond();
                if (distanceToOtherNeighbors.get(v) + len < distanceToOtherNeighbors.get(to)) {
                    distanceToOtherNeighbors.set(to,distanceToOtherNeighbors.get(v) + len);
                    parents.set(to,v);
                }
            }
        }
        return new Pair<>(parents,distanceToOtherNeighbors.subList(distanceToOtherNeighbors.size() - shiftedSize, distanceToOtherNeighbors.size()));
    }


    private List<Pair<Integer,Integer>> getPath(int start, int end, List<Integer> parents){
        List<Pair<Integer,Integer>> path = new ArrayList<>();
        for (int v = end; v!= start ; v = parents.get(v)) {
            buildPath(v,path);
            countRedCells(start,v);
        }
        buildPath(start, path);
        countRedCells(start,start);
        return path;
    }

    private Pair<Integer,Integer> getIndecies(int currentPosition){
        int j = currentPosition % this.shiftedSize;
        int i = (currentPosition-j)/this.shiftedSize;
        return new Pair<>(i,j);
    }

    private void buildPath(int currentPosition, List<Pair<Integer,Integer>> path){
        Pair<Integer, Integer> indecies = getIndecies(currentPosition);
        int i = indecies.getFirst();
        int j = indecies.getSecond();
        path.add(0,new Pair<>(i,j));
    }

    private void countRedCells(int startPosition, int currentPosition){
        Pair<Integer, Integer> indecies = getIndecies(currentPosition);
        int i = indecies.getFirst();
        int j = indecies.getSecond();
        if(matrix.getCell(i + Matrix.OFFSET, j + Matrix.OFFSET).isWhite())
            redCellCounters[startPosition]++;
    }


    private List<Integer> initWith(int initialValue, int size){
        return IntStream.generate(() -> initialValue)
                .limit(size)
                .boxed().collect(Collectors.toList());
    }

    private List<Boolean> initWith(boolean initialValue, int size){
        return IntStream.range(0, size)
                .mapToObj(object -> initialValue).collect(Collectors.toList());
    }




}
