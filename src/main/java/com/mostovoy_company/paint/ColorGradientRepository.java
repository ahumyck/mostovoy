package com.mostovoy_company.paint;

import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
class ColorGradientRepository implements ColorRepository{

    private Map<Integer, Color> colors = new HashMap<>();

    ColorGradientRepository() {
        int index = 0;
        int start = 30;
        int stop = 255;
        int step = 15;
        this.colors.put(index++, Color.WHITE);
        for (int i = start; i < stop; i+=step) {
            for (int j = start; j < stop; j+=step) {
                for (int k = start; k < stop; k+=step) {
                    this.colors.put(index++,Color.rgb(i,j,k));
                }
            }
        }
    }

    public Color getColorForCell(int clusterMark) {
        return clusterMark > 0 ? Color.BLACK : Color.WHITE;
    }

    public Color getRandomColorForCluster(int clusterMark) { return colors.get(clusterMark); }
}