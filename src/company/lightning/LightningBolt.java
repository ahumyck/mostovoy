package company.lightning;

import company.entity.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LightningBolt {



    public static List<Integer> findShortestWays(Matrix matrix){
        Map<Integer, List<Pair>> adjacencyList = new LightWeightAdjacencyListBuilderByMatrix().build(matrix);
        int start_pos = 0;
        return findDistances(start_pos, adjacencyList, matrix.getSize());
    }


    private static List<Integer> findDistances(int start_pos,  Map<Integer, List<Pair>> adjacencyList,int actualMatrixSize){
        int shiftedSize = actualMatrixSize - 2 * Matrix.OFFSET;
        int stepCost = shiftedSize * shiftedSize + 1;
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
                Pair pair = adjacencyList.get(v).get(j);
                int to = pair.getX();
                int len = pair.getY();
                if (distanceToOtherNeighbors.get(v) + len < distanceToOtherNeighbors.get(to)) {
                    distanceToOtherNeighbors.set(to,distanceToOtherNeighbors.get(v) + len);
                    parents.set(to,v);
                }
            }
        }


        List<Pair> path = new ArrayList<>();
        int t = distanceToOtherNeighbors.size() - shiftedSize;
        for (int v = t; v!= start_pos ; v = parents.get(v)) {
            int j = v % shiftedSize;
            int i = (v-j)/shiftedSize;
            path.add(0,new Pair(i,j));
        }
        int j = start_pos % shiftedSize;
        int i = (start_pos - j) / shiftedSize;
        path.add(0, new Pair(i,j));
        System.out.println("path = " + path);

        return distanceToOtherNeighbors.subList(distanceToOtherNeighbors.size() - shiftedSize, distanceToOtherNeighbors.size());
    }


}
