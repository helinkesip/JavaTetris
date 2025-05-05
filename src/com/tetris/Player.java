package com.tetris;

import com.tetris.utils.GameUtils;
import com.tetris.model.Tetromino;
import com.tetris.model.GameBoard;

public class Player {
    private String name;
    private int score;
    private GameBoard board;
    private Tetromino currentPiece;
    private int currentX, currentY;

    public Player(String name, GameBoard board) {
        this.name = name;
        this.board = board;
        this.score = 0;
        spawnNewPiece();
    }

    public void spawnNewPiece() {
        currentPiece = GameUtils.createRandomTetromino();
        currentX = board.getWidth() / 2 - currentPiece.getShape()[0].length / 2;
        currentY = 0;

        if (!board.isValidPosition(currentPiece, currentX, currentY)) {
            System.out.println("Game Over for " + name);
        }
    }

    public void movePieceDown() {
        if (board.isValidPosition(currentPiece, currentX, currentY + 1)) {
            currentY++;
        } else {
            board.mergePiece(currentPiece, currentX, currentY);
            int lines = board.clearLines();
            score += lines * 100;
            spawnNewPiece();
        }
    }

    public void movePieceLeft() {
        if (board.isValidPosition(currentPiece, currentX - 1, currentY)) {
            currentX--;
        }
    }

    public void movePieceRight() {
        if (board.isValidPosition(currentPiece, currentX + 1, currentY)) {
            currentX++;
        }
    }

    public void rotatePiece() {
        currentPiece.rotate();
        if (!board.isValidPosition(currentPiece, currentX, currentY)) {
            currentPiece.rotate(); // Undo rotation if invalid
            currentPiece.rotate();
            currentPiece.rotate();
        }
    }

    public void handleKeyPress(int keyCode) {
        // This method will be overridden in PlayerOne and PlayerTwo
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public GameBoard getBoard() {
        return board;
    }

    public Tetromino getCurrentPiece() {
        return currentPiece;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }
}
