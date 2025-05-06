package com.tetris.utils;

import com.tetris.model.Tetromino;

import java.awt.Color;

public class GameUtils {
    private static final int[][][] SHAPES = {
            {{1, 1, 1, 1}},                  // I
            {{1, 1}, {1, 1}},                // O
            {{0, 1, 0}, {1, 1, 1}},          // T
            {{0, 0, 1}, {1, 1, 1}},          // L
            {{1, 0, 0}, {1, 1, 1}},          // J
            {{0, 1, 1}, {1, 1, 0}},          // S
            {{1, 1, 0}, {0, 1, 1}},          // Z
    };

    private static final Color[] COLORS = {
            new Color(255, 170, 170), // I
            new Color(255, 255, 170), // O
            new Color(170, 255, 170), // T
            new Color(170, 255, 255), // L
            new Color(170, 170, 255), // J
            new Color(255, 170, 255), // S
            new Color(255, 210, 170)  // Z
    };

    public static Tetromino createRandomTetromino() {
        int index = (int)(Math.random() * Tetromino.SHAPES.length);
        int[][] shape = Tetromino.SHAPES[index];
        Color color = Tetromino.COLORS[index];
        return new Tetromino(shape, color, index + 1); // id 1-7 arası olur

    }


}
