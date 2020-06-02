package com.mostovoy_company.expirement.chart_experiment;

import com.mostovoy_company.expirement.chart_experiment.entity.HoshenKopelman;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import com.mostovoy_company.expirement.chart_experiment.filling.customs.CustomTestFillingType;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class MatrixBuilderByPicture {

    public Matrix build(BufferedImage image) {
        CustomTestFillingType imageFillingType = new CustomTestFillingType("image") {
            @Override
            public int[][] getMatrix() {
                int rows = image.getHeight();
                int columns = image.getWidth();
                int[][] pixels = new int[rows][columns];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        if (image.getRGB(i, j) != -1) pixels[j][i] = 1;
                        else pixels[j][i] = 0;
                    }
                }
                return pixels;
            }
        };
        Matrix matrix = new Matrix(imageFillingType);
        new HoshenKopelman().clusterization(matrix);
        return matrix;
    }
}
