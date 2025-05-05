package com.tetris.model;
import com.tetris.model.GameBoard;
import com.tetris.model.Tetromino;
import com.tetris.utils.GameUtils;

import java.util.Arrays;



public class GameBoard {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    private int[][] board;

    public GameBoard() {
        board = new int[HEIGHT][WIDTH];
    }

    public boolean isValidPosition(Tetromino tetromino, int x, int y) {
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

    public void mergePiece(Tetromino tetromino, int x, int y) {
        int[][] shape = tetromino.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    board[y + i][x + j] = tetromino.getColor().getRGB();
                }
            }
        }
    }

    public int clearLines() {
        int cleared = 0;
        for (int row = HEIGHT - 1; row >= 0; row--) {
            boolean full = true;
            for (int col = 0; col < WIDTH; col++) {
                if (board[row][col] == 0) {
                    full = false;
                    break;
                }
            }

            if (full) {
                for (int i = row; i > 0; i--) {
                    board[i] = Arrays.copyOf(board[i - 1], WIDTH);
                }
                Arrays.fill(board[0], 0);
                cleared++;
                row++; // check same row again after shifting
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
