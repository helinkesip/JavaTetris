package com.tetris;

import com.tetris.utils.GameUtils;
import com.tetris.model.Tetromino;
import com.tetris.model.GameBoard;

public abstract class Player {
    private String name;
    private int score;
    private GameBoard board;
    private Tetromino currentPiece;
    private int currentX, currentY;
    private boolean gameOver = false;

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
            gameOver = true;
        }
    }

    public void movePieceDown() {
        if (gameOver) return;
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
        if (gameOver) return;
        if (board.isValidPosition(currentPiece, currentX - 1, currentY)) {
            currentX--;
        }
    }

    public void movePieceRight() {
        if (gameOver) return;
        if (board.isValidPosition(currentPiece, currentX + 1, currentY)) {
            currentX++;
        }
    }

    public void rotatePiece() {
        if (gameOver) return;
        currentPiece.rotate();
        if (!board.isValidPosition(currentPiece, currentX, currentY)) {
            // Rotate back (3 times to undo)
            currentPiece.rotate();
            currentPiece.rotate();
            currentPiece.rotate();
        }
    }

    public boolean isGameOver() {
        return gameOver;
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
    public abstract void handleKeyPress(int keyCode);

}