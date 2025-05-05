package com.tetris.controller;

import com.tetris.model.GameBoard;
import com.tetris.model.Tetromino;
import com.tetris.utils.GameUtils;
import com.tetris.view.GamePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameEngine {
    private GameBoard gameBoard;
    private Tetromino currentPiece;
    private Tetromino nextPiece;
    private GamePanel gamePanel;
    private Timer gameTimer;
    private int score;
    private int level;
    private int linesCleared;

    private static final int[] LEVEL_SPEEDS = {
            800, 720, 630, 550, 470, 380, 300, 220, 130, 100,
            80, 80, 80, 70, 70, 70, 50, 50, 30, 30
    };

    public GameEngine(GamePanel gamePanel, GameBoard gameBoard) {
        this.gamePanel = gamePanel;
        this.gameBoard = gameBoard;
        this.score = 0;
        this.level = 0;
        this.linesCleared = 0;

        spawnNewPiece();
        setupGameTimer();
    }

    private void setupGameTimer() {
        gameTimer = new Timer(LEVEL_SPEEDS[level], new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveDown();
            }
        });
        gameTimer.start();
    }

    public void startGame() {
        gameTimer.start();
    }

    public void pauseGame() {
        gameTimer.stop();
    }

    private void spawnNewPiece() {
        currentPiece = (nextPiece != null) ? nextPiece : GameUtils.createRandomTetromino();
        nextPiece = GameUtils.createRandomTetromino();

        currentPiece.setX(gameBoard.getWidth() / 2 - currentPiece.getShape()[0].length / 2);
        currentPiece.setY(0);

        if (!gameBoard.isValidPosition(currentPiece, currentPiece.getX(), currentPiece.getY())) {
            gameTimer.stop();
            JOptionPane.showMessageDialog(gamePanel, "Game Over! Score: " + score);
        }

        gamePanel.setCurrentPiece(currentPiece);
        gamePanel.setNextPiece(nextPiece);
        gamePanel.repaint();
    }

    public void moveLeft() {
        if (gameBoard.isValidPosition(currentPiece, currentPiece.getX() - 1, currentPiece.getY())) {
            currentPiece.setX(currentPiece.getX() - 1);
            gamePanel.repaint();
        }
    }

    public void moveRight() {
        if (gameBoard.isValidPosition(currentPiece, currentPiece.getX() + 1, currentPiece.getY())) {
            currentPiece.setX(currentPiece.getX() + 1);
            gamePanel.repaint();
        }
    }

    public void moveDown() {
        if (gameBoard.isValidPosition(currentPiece, currentPiece.getX(), currentPiece.getY() + 1)) {
            currentPiece.setY(currentPiece.getY() + 1);
            gamePanel.repaint();
        } else {
            lockPiece();
        }
    }

    public void rotate() {
        Tetromino tempPiece = new Tetromino(currentPiece.getShape(), currentPiece.getColor());
        tempPiece.setX(currentPiece.getX());
        tempPiece.setY(currentPiece.getY());
        tempPiece.rotate();

        if (gameBoard.isValidPosition(tempPiece, tempPiece.getX(), tempPiece.getY())) {
            currentPiece.rotate();
            gamePanel.repaint();
        }
    }

    public void drop() {
        while (gameBoard.isValidPosition(currentPiece, currentPiece.getX(), currentPiece.getY() + 1)) {
            currentPiece.setY(currentPiece.getY() + 1);
        }
        lockPiece();
        gamePanel.repaint();
    }

    private void lockPiece() {
        gameBoard.mergePiece(currentPiece);
        int lines = gameBoard.clearLines();
        if (lines > 0) {
            updateScore(lines);
        }
        spawnNewPiece();
        gamePanel.repaint();
    }

    private void updateScore(int lines) {
        linesCleared += lines;
        level = linesCleared / 10;

        switch (lines) {
            case 1: score += 100 * (level + 1); break;
            case 2: score += 300 * (level + 1); break;
            case 3: score += 500 * (level + 1); break;
            case 4: score += 800 * (level + 1); break;
        }

        if (level < LEVEL_SPEEDS.length) {
            gameTimer.setDelay(LEVEL_SPEEDS[level]);
        }

        gamePanel.updateScore(score, level, linesCleared);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Tetromino getNextPiece() {
        return nextPiece;
    }
}
