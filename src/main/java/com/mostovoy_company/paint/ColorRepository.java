package com.mostovoy_company.paint;

import javafx.scene.paint.Color;

public interface ColorRepository {
    Color getRandomColorForCluster(int clusterMark);
    Color getColorForCell(int clusterMark);
}