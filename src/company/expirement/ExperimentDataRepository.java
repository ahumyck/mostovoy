package company.expirement;

import java.util.HashMap;
import java.util.Map;

public class ExperimentDataRepository {

    private Map<String, Matrix> cache = new HashMap<>();

    public void add(String experimentKey, Matrix matrix)
    {
        cache.putIfAbsent(experimentKey, matrix);
    }
    public Matrix get(String experimentKey)
    {
        return cache.get(experimentKey);
    }
    public void clear(){
        cache.clear();
    }
}
