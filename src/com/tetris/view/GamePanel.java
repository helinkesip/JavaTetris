package com.tetris.view;

import com.tetris.controller.GameEngine;
import com.tetris.model.GameBoard;
import com.tetris.model.Tetromino;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {
    private static final int BLOCK_SIZE = 30;
    private static final int SIDEBAR_WIDTH = 150;

    private GameBoard gameBoard;
    private Tetromino currentPiece;
    private Tetromino nextPiece;
    private int score;
    private int level;
    private int lines;

    private GameEngine gameEngine;

    public GamePanel() {
        this.gameBoard = new GameBoard(); // burada oluştur
        this.gameEngine = new GameEngine(this, gameBoard); // buraya gönder

        setPreferredSize(new Dimension(
                GameBoard.WIDTH * BLOCK_SIZE + SIDEBAR_WIDTH,
                GameBoard.HEIGHT * BLOCK_SIZE
        ));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameEngine.startGame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGameBoard(g);
        drawCurrentPiece(g);
        drawSidebar(g);
    }

    private void drawGameBoard(Graphics g) {
        int[][] board = gameBoard.getBoard();
        for (int y = 0; y < gameBoard.getHeight(); y++) {
            for (int x = 0; x < gameBoard.getWidth(); x++) {
                if (board[y][x] != 0) {
                    g.setColor(new Color(board[y][x]));
                    g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(Color.WHITE);
                    g.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    private void drawCurrentPiece(Graphics g) {
        if (currentPiece != null) {
            int[][] shape = currentPiece.getShape();
            Color color = currentPiece.getColor();

            for (int y = 0; y < shape.length; y++) {
                for (int x = 0; x < shape[y].length; x++) {
                    if (shape[y][x] != 0) {
                        g.setColor(color);
                        g.fillRect(
                                (currentPiece.getX() + x) * BLOCK_SIZE,
                                (currentPiece.getY() + y) * BLOCK_SIZE,
                                BLOCK_SIZE, BLOCK_SIZE
                        );
                        g.setColor(Color.WHITE);
                        g.drawRect(
                                (currentPiece.getX() + x) * BLOCK_SIZE,
                                (currentPiece.getY() + y) * BLOCK_SIZE,
                                BLOCK_SIZE, BLOCK_SIZE
                        );
                    }
                }
            }
        }
    }

    private void drawSidebar(Graphics g) {
        int sidebarX = gameBoard.getWidth() * BLOCK_SIZE;

        g.setColor(new Color(50, 50, 50));
        g.fillRect(sidebarX, 0, SIDEBAR_WIDTH, getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, sidebarX + 20, 30);
        g.drawString("Level: " + level, sidebarX + 20, 60);
        g.drawString("Lines: " + lines, sidebarX + 20, 90);

        g.drawString("Next:", sidebarX + 20, 150);
        if (nextPiece != null) {
            int[][] shape = nextPiece.getShape();
            Color color = nextPiece.getColor();
            int startX = sidebarX + 50;
            int startY = 180;

            for (int y = 0; y < shape.length; y++) {
                for (int x = 0; x < shape[y].length; x++) {
                    if (shape[y][x] != 0) {
                        g.setColor(color);
                        g.fillRect(
                                startX + x * BLOCK_SIZE / 2,
                                startY + y * BLOCK_SIZE / 2,
                                BLOCK_SIZE / 2, BLOCK_SIZE / 2
                        );
                        g.setColor(Color.WHITE);
                        g.drawRect(
                                startX + x * BLOCK_SIZE / 2,
                                startY + y * BLOCK_SIZE / 2,
                                BLOCK_SIZE / 2, BLOCK_SIZE / 2
                        );
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameEngine == null) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: gameEngine.moveLeft(); break;
            case KeyEvent.VK_RIGHT: gameEngine.moveRight(); break;
            case KeyEvent.VK_DOWN: gameEngine.moveDown(); break;
            case KeyEvent.VK_UP: gameEngine.rotate(); break;
            case KeyEvent.VK_SPACE: gameEngine.drop(); break;
            case KeyEvent.VK_P: gameEngine.pauseGame(); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public void setCurrentPiece(Tetromino currentPiece) {
        this.currentPiece = currentPiece;
    }

    public void setNextPiece(Tetromino nextPiece) {
        this.nextPiece = nextPiece;
    }

    public void updateScore(int score, int level, int lines) {
        this.score = score;
        this.level = level;
        this.lines = lines;
        repaint();
    }
}
