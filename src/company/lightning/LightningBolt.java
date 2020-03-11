package company.lightning;

import company.entity.Matrix;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LightningBolt {

    public static Pair<List<Pair<Integer,Integer>>,Integer> findShortestWays(Matrix matrix){
        Map<Integer, List<Pair<Integer, Integer>>> adjacencyList = new AdjacencyListBuilderByMatrix().build(matrix);
        int shiftedSize = matrix.getSize() - 2 * Matrix.OFFSET;
        List<Pair<List<Pair<Integer,Integer>>,Integer>> paths = new ArrayList<>();
        for(int currentPos = 0 ; currentPos < shiftedSize; currentPos++){
            Pair<List<Integer>, List<Integer>> inf = findDistances(currentPos, adjacencyList, shiftedSize);
            List<Integer> distances = inf.getY();
            List<Integer> parents = inf.getX();

            int shortest = distances.indexOf(distances.stream().min(Integer::compareTo).get());
            int endPos = shiftedSize*(shiftedSize - 1) + shortest;
            List<Pair<Integer, Integer>> path = getPath(currentPos, endPos, parents, shiftedSize);

            paths.add(new Pair<>(path,distances.get(shortest)));
        }
        return paths.stream().min((a, b) -> Integer.compare(a.getY(), b.getY())).get();
    }


    private static Pair<List<Integer>, List<Integer>> findDistances(int start_pos,  Map<Integer, List<Pair<Integer, Integer>>> adjacencyList,int shiftedSize){
        List<Integer> distanceToOtherNeighbors = IntStream.generate(() -> Integer.MAX_VALUE)
                .limit(adjacencyList.size())
                .boxed().collect(Collectors.toList());
        List<Integer> parents = IntStream.generate(() -> 0)
                .limit(adjacencyList.size())
                .boxed().collect(Collectors.toList());

        List<Boolean> visitedNeighbors = IntStream.range(0, adjacencyList.size())
                .mapToObj(object -> false).collect(Collectors.toList());


        distanceToOtherNeighbors.set(start_pos,0);
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
                int to = pair.getX();
                int len = pair.getY();
                if (distanceToOtherNeighbors.get(v) + len < distanceToOtherNeighbors.get(to)) {
                    distanceToOtherNeighbors.set(to,distanceToOtherNeighbors.get(v) + len);
                    parents.set(to,v);
                }
            }
        }

        return new Pair<>(parents,distanceToOtherNeighbors.subList(distanceToOtherNeighbors.size() - shiftedSize, distanceToOtherNeighbors.size()));
    }

    private static List<Pair<Integer,Integer>> getPath(int start,int end, List<Integer> parents, int shiftedSize){
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


}
