package com.tetris.utils;

import com.tetris.model.Tetromino;

import java.awt.Color;

public class GameUtils {
    public static Tetromino createRandomTetromino() {
        int index = (int)(Math.random() * Tetromino.SHAPES.length);
        int[][] shape = Tetromino.SHAPES[index];
        Color color = Tetromino.COLORS[index];
        return new Tetromino(shape, color, index + 1);

    }


}
