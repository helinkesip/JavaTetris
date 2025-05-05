package com.tetris.model;

import java.util.Arrays;

public class GameBoard {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    private int[][] board;

    public GameBoard() {
        this.board = new int[HEIGHT][WIDTH];
    }

    public boolean isValidPosition(Tetromino tetromino, int newX, int newY) {
        int[][] shape = tetromino.getShape();
        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                if (shape[y][x] != 0) {
                    int boardX = newX + x;
                    int boardY = newY + y;

                    if (boardX < 0 || boardX >= WIDTH || boardY >= HEIGHT) {
                        return false;
                    }

                    if (boardY >= 0 && board[boardY][boardX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void mergePiece(Tetromino tetromino) {
        int[][] shape = tetromino.getShape();
        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                if (shape[y][x] != 0) {
                    int boardY = tetromino.getY() + y;
                    if (boardY >= 0) {
                        board[boardY][tetromino.getX() + x] = tetromino.getColor().getRGB();
                    }
                }
            }
        }
    }

    public int clearLines() {
        int linesCleared = 0;
        for (int y = HEIGHT - 1; y >= 0; y--) {
            boolean lineComplete = true;
            for (int x = 0; x < WIDTH; x++) {
                if (board[y][x] == 0) {
                    lineComplete = false;
                    break;
                }
            }

            if (lineComplete) {
                linesCleared++;
                for (int yy = y; yy > 0; yy--) {
                    System.arraycopy(board[yy - 1], 0, board[yy], 0, WIDTH);
                }
                Arrays.fill(board[0], 0);
                y++;
            }
        }
        return linesCleared;
    }

    public int[][] getBoard() {
        return board;
    }

    public void clear() {
        for (int y = 0; y < HEIGHT; y++) {
            Arrays.fill(board[y], 0);
        }
    }

    public int getWidth() { return WIDTH; }
    public int getHeight() { return HEIGHT; }
}
