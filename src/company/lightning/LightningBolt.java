package company.lightning;

import company.entity.Matrix;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LightningBolt {

    private int iteration = 0;
    private int shiftedSize = 0;
    private Map<Integer, List<Pair<Integer, Integer>>> adjacencyList;

    public LightningBolt(Matrix matrix) {
        adjacencyList = new AdjacencyListBuilderByMatrix().build(matrix);
        shiftedSize = (matrix.getSize() - 2 * Matrix.OFFSET);
    }

    public boolean isActive() {
        return iteration < shiftedSize;
    }

    public Pair<List<Pair<Integer,Integer>>,Integer> startNextIteration(){
        List<Integer> distanceToOtherNeighbors = initWith(Integer.MAX_VALUE,adjacencyList.size());
        List<Integer> parents = initWith(0,adjacencyList.size());
        List<Boolean> visitedNeighbors = initWith(false,adjacencyList.size());

        distanceToOtherNeighbors.set(iteration,0);
        int n = adjacencyList.keySet().size();
        for (int i = 0; i < n; i++) {
            int v = -1;
            for (int j = 0; j < n; j++) {
                if(!visitedNeighbors.get(j) && (v == -1 || distanceToOtherNeighbors.get(j) < distanceToOtherNeighbors.get(v)))
                    v = j;
            }
            if(distanceToOtherNeighbors.get(v) == Integer.MAX_VALUE) {
                break;
            }
            visitedNeighbors.set(v,true);
            for (int j = 0; j < adjacencyList.get(v).size(); j++) {
                Pair<Integer,Integer> pair = adjacencyList.get(v).get(j);
                int to = pair.getFirst();
                int len = pair.getSecond();
                if (distanceToOtherNeighbors.get(v) + len < distanceToOtherNeighbors.get(to)) {

                    distanceToOtherNeighbors.set(to,distanceToOtherNeighbors.get(v) + len);
                    parents.set(to,v);
                }
            }
        }

        List<Integer> distances = distanceToOtherNeighbors.subList(distanceToOtherNeighbors.size() - shiftedSize, distanceToOtherNeighbors.size());
        int shortest = distances.indexOf(distances.stream().min(Integer::compareTo).get());
        int endPos = shiftedSize*(shiftedSize - 1) + shortest;
        List<Pair<Integer, Integer>> path = getPath(iteration, endPos, parents, shiftedSize);

        iteration++;

        return new Pair<>(path,distances.get(shortest));
    }

    private List<Pair<Integer,Integer>> getPath(int start,int end, List<Integer> parents, int shiftedSize){
        List<Pair<Integer,Integer>> path = new ArrayList<>();
        for (int v = end; v!= start ; v = parents.get(v)) {
            int j = v % shiftedSize;
            int i = (v-j)/shiftedSize;
            path.add(0,new Pair<>(i,j));
        }
        int j = start % shiftedSize;
        int i = (start - j) / shiftedSize;
        path.add(0, new Pair<>(i,j));
        return path;
    }

    private List<Integer> initWith(int initialValue, int size){
        return IntStream.generate(() -> initialValue)
                .limit(size)
                .boxed().collect(Collectors.toList());
    }

    private List<Boolean> initWith(boolean value, int size){
        return IntStream.range(0, size)
                .mapToObj(object -> value).collect(Collectors.toList());
    }




}
