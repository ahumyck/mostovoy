package com.mostovoy_company.paint;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ColorRepository {

    private Map<Integer, Color> colors = new HashMap<>();

    public ColorRepository() {
        this.colors.put(0, Color.WHITE);
    }

    public Color getRandomColorForCluster(int clusterMark) {
        colors.putIfAbsent(clusterMark, getRandomColor());
        return colors.get(clusterMark);
    }

    public Color getColorForCell(int clusterMark) {
        return clusterMark > 0 ? Color.BLACK : Color.WHITE;
    }

    private Color getRandomColor() {
        Random random = new Random();
        return Color.rgb(30 + random.nextInt(226), 30 + random.nextInt(226), 30 + random.nextInt(226));
    }
}
