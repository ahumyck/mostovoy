package company.paint;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ColorRepository {
    private Map<Integer, Color> colors = new HashMap<>();

    public ColorRepository() {
        this.colors.put(0, Color.WHITE);
    }

    public Color getColorForCluster(int clusterMark) {
        colors.putIfAbsent(clusterMark, getRandomColor());
        return colors.get(clusterMark);
    }

    private Color getRandomColor(){
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
