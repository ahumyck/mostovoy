package company.paint;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class ColorGradientRepository {

    private Map<Integer, Color> colors = new HashMap<>();

    public ColorGradientRepository() {
        this.colors.put(0, Color.WHITE);
        int index = 1;
        for (int i = 30; i < 255; i+=15) {
            for (int j = 30; j < 255; j+=15) {
                for (int k = 30; k < 255; k+=15) {
                    this.colors.put(index,Color.rgb(i,j,k));
                    index++;
                }
            }
        }
    }

    public Color getColorForCell(int clusterMark) {
        return clusterMark > 0 ? Color.BLACK : Color.WHITE;
    }

    public Color getRandomColorForCluster(int clusterMark) {
        return colors.get(clusterMark);
    }
}
