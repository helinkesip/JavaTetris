package com.tetris.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameBoard {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    private int[][] board;

    public GameBoard() {
        board = new int[HEIGHT][WIDTH];
    }

    public boolean isValidPosition(com.tetris.model.Tetromino tetromino, int x, int y) {
        int[][] shape = tetromino.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int newX = x + j;
                    int newY = y + i;
                    if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT)
                        return false;
                    if (board[newY][newX] != 0)
                        return false;
                }
            }
        }
        return true;
    }

    public void mergePiece(com.tetris.model.Tetromino tetromino, int x, int y) {
        int[][] shape = tetromino.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    board[y + i][x + j] = tetromino.getColor().getRGB();
                }
            }
        }
    }

    public Set<Integer> clearLines() {
        Set<Integer> cleared = new HashSet<>();
        for (int row = HEIGHT - 1; row >= 0; row--) {
            boolean full = true;
            for (int col = 0; col < WIDTH; col++) {
                if (board[row][col] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                cleared.add(row);
                for (int i = row; i > 0; i--) {
                    board[i] = Arrays.copyOf(board[i - 1], WIDTH);
                }
                Arrays.fill(board[0], 0);
                row++;
            }
        }
        return cleared;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}
