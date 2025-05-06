package com.tetris;

import com.tetris.utils.GameUtils;
import com.tetris.model.Tetromino;
import com.tetris.model.GameBoard;

import java.util.Set;

public abstract class Player {
    private String name;
    private int score;
    private GameBoard board;
    private Tetromino currentPiece;
    private Tetromino nextPiece;
    private int currentX, currentY;
    private boolean gameOver = false;

    public Player(String name, GameBoard board) {
        this.name = name;
        this.board = board;
        this.score = 0;
        this.nextPiece = GameUtils.createRandomTetromino();
        spawnNewPiece();
    }

    public void spawnNewPiece() {
        currentPiece = nextPiece;
        nextPiece = GameUtils.createRandomTetromino();

        // Özel tetromino spawn konumları
        switch(currentPiece.getId()) {
            case 0: // I tetromino (4x1)
                currentX = board.getWidth() / 2 - 2; // Ortalamak için -2
                currentY = 0;
                break;
            case 1: // O tetromino (2x2)
                currentX = board.getWidth() / 2 - 1; // Ortalamak için -1
                currentY = 0;
                break;
            default: // Diğer tetrominolar
                currentX = board.getWidth() / 2 - currentPiece.getShape()[0].length / 2;
                currentY = 0;
        }

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

        // Mevcut rotasyonu geçici olarak uygula
        currentPiece.rotate();

        // Eğer yeni pozisyon geçersizse, rotasyonu geri al
        if (!board.isValidPosition(currentPiece, currentX, currentY)) {
            // I Tetrominosu için özel düzeltme (kenara yapışmasın)
            if (currentPiece.getId() == 0) {
                int newX = currentX;

                // Sola taşı (sağa kaymışsa)
                while (!board.isValidPosition(currentPiece, newX, currentY) && newX > 0) {
                    newX--;
                }

                // Sağa taşı (sola kaymışsa)
                if (!board.isValidPosition(currentPiece, newX, currentY)) {
                    newX = currentX;
                    while (!board.isValidPosition(currentPiece, newX, currentY) && newX < board.getWidth() - currentPiece.getShape()[0].length) {
                        newX++;
                    }
                }

                if (board.isValidPosition(currentPiece, newX, currentY)) {
                    currentX = newX; // Geçerli bir pozisyon bulundu
                } else {
                    // Rotasyon mümkün değil, eski haline dön
                    currentPiece.rotate();
                    currentPiece.rotate();
                    currentPiece.rotate();
                }
            } else {
                // Diğer tetrominolar için normal davranış
                currentPiece.rotate();
                currentPiece.rotate();
                currentPiece.rotate();
            }
        }
    }
    public boolean isValidPosition(Tetromino piece, int x, int y) {
        int[][] shape = piece.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int newX = x + j;
                    int newY = y + i;

                    if (newX < 0 || newX >= board.getWidth() || newY >= board.getHeight()) {
                        return false;
                    }

                    if (newY >= 0 && board.getBoard()[newY][newX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public void addScore(int value) {
        this.score += value;
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

    public Tetromino getNextPiece() {
        return nextPiece;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public int getGhostY() {
        int ghostY = currentY;
        while (board.isValidPosition(currentPiece, currentX, ghostY + 1)) {
            ghostY++;
        }
        return ghostY;
    }

    public abstract void handleKeyPress(int keyCode);
}
