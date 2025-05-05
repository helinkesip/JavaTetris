package com.tetris.model;

import java.awt.Color;

public class Tetromino {
    private int[][] shape;
    private Color color;

    public Tetromino(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public void rotate() {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotated = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = shape[i][j];
            }
        }
        shape = rotated;
    }

    public int[][] getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
}
