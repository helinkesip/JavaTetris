package com.tetris.utils;

import com.tetris.model.Tetromino;
import java.awt.Color;

public class GameUtils {
    private static final int[][][] TETROMINO_SHAPES = {
            {{1, 1, 1, 1}}, // I
            {{1, 1}, {1, 1}}, // O
            {{0, 1, 0}, {1, 1, 1}}, // T
            {{0, 0, 1}, {1, 1, 1}}, // L
            {{1, 0, 0}, {1, 1, 1}}, // J
            {{0, 1, 1}, {1, 1, 0}}, // S
            {{1, 1, 0}, {0, 1, 1}}  // Z
    };

    private static final Color[] COLORS = {
            new Color(0, 255, 255), // I - Cyan
            new Color(255, 255, 0), // O - Yellow
            new Color(170, 0, 255), // T - Purple
            new Color(255, 165, 0), // L - Orange
            new Color(0, 0, 255),   // J - Blue
            new Color(0, 255, 0),   // S - Green
            new Color(255, 0, 0)    // Z - Red
    };

    public static Tetromino createRandomTetromino() {
        int randomIndex = (int)(Math.random() * TETROMINO_SHAPES.length);
        return new Tetromino(TETROMINO_SHAPES[randomIndex], COLORS[randomIndex]);
    }
}
