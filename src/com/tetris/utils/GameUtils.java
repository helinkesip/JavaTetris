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
            Color.CYAN, Color.YELLOW, new Color(128, 0, 128), Color.ORANGE,
            Color.BLUE, Color.GREEN, Color.RED
    };

    public static Tetromino createRandomTetromino() {
        int index = (int)(Math.random() * SHAPES.length);
        return new Tetromino(SHAPES[index], COLORS[index]);
    }
}
