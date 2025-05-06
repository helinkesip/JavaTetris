package com.tetris.model;

import java.awt.Color;

public class Tetromino {
    public static final int[][][] SHAPES = {
            {{1, 1, 1, 1}},                // I
            {{1, 1}, {1, 1}},              // O
            {{0, 1, 0}, {1, 1, 1}},        // T
            {{1, 0, 0}, {1, 1, 1}},        // L
            {{0, 0, 1}, {1, 1, 1}},        // J
            {{0, 1, 1}, {1, 1, 0}},        // S
            {{1, 1, 0}, {0, 1, 1}}         // Z
    };

    public static final Color[] COLORS = {
            new Color(255, 170, 170), // I
            new Color(255, 255, 170), // O
            new Color(170, 255, 170), // T
            new Color(170, 255, 255), // L
            new Color(170, 170, 255), // J
            new Color(255, 170, 255), // S
            new Color(255, 210, 170)  // Z
    };

    private int[][] shape;
    private Color color;
    private int id;

    public Tetromino(int[][] shape, Color color, int id) {
        this.shape = shape;
        this.color = color;
        this.id = id;
    }

    public static Tetromino createRandomTetromino() {
        int index = (int)(Math.random() * SHAPES.length);
        return new Tetromino(SHAPES[index], COLORS[index], index + 1); // id = 1 to 7
    }

    public void rotate() {
        int row = shape.length;
        int col = shape[0].length;
        int[][] rotated = new int[col][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                rotated[j][row - 1 - i] = shape[i][j];
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

    public int getId() {
        return id;
    }

    public static Color getColorById(int id) {
        if (id <= 0 || id > COLORS.length) return Color.GRAY; // fallback
        return COLORS[id - 1];
    }
}
