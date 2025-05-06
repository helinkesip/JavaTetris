package com.tetris.view;

import com.tetris.controller.GameEngine;
import com.tetris.model.GameBoard;
import com.tetris.model.Tetromino;
import com.tetris.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    private GameEngine gameEngine;
    private Timer animationTimer;

    private final Set<Integer> animatingRows = new HashSet<>();
    private int animationStep = 0;
    private final int ANIMATION_STEPS = 5;

    private final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private final Color PANEL_COLOR = new Color(50, 50, 50);
    private final Font SCORE_FONT = new Font("Verdana", Font.BOLD, 28);
    private final Font LABEL_FONT = new Font("Verdana", Font.BOLD, 20);
    private final Font GAMEOVER_FONT = new Font("Verdana", Font.BOLD, 40);
    private final Color SCORE_COLOR = new Color(144, 238, 144);

    private final int BLOCK_SIZE = 28;
    private final int BOARD_TOP_OFFSET = 90;
    private final int BOARD_WIDTH = GameBoard.WIDTH * BLOCK_SIZE;

    public GamePanel() {
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH * 2, BOARD_TOP_OFFSET + GameBoard.HEIGHT * BLOCK_SIZE));
        addKeyListener(this);

        GameBoard board1 = new GameBoard();
        GameBoard board2 = new GameBoard();

        gameEngine = new GameEngine(board1, board2);
        gameEngine.setGamePanel(this);

        animationTimer = new Timer(120, this);
        animationTimer.start();
    }

    public void triggerRowAnimation(Set<Integer> rows) {
        animatingRows.clear();
        animatingRows.addAll(rows);
        animationStep = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        animationStep++;
        if (animationStep >= ANIMATION_STEPS) {
            animatingRows.clear();
            animationStep = 0;
            gameEngine.updateGame();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(PANEL_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        drawBoard(g2d, gameEngine.getPlayer1(), 0);
        drawBoard(g2d, gameEngine.getPlayer2(), BOARD_WIDTH);

        g2d.setColor(SCORE_COLOR);
        g2d.setFont(SCORE_FONT);
        g2d.drawString("P1 Skor: " + gameEngine.getPlayer1().getScore(), 10, 40);
        g2d.drawString("P2 Skor: " + gameEngine.getPlayer2().getScore(), BOARD_WIDTH + 10, 40);

        g2d.setFont(LABEL_FONT);
        g2d.drawString("OYUNCU 1", 10, 65);
        g2d.drawString("OYUNCU 2", BOARD_WIDTH + 10, 65);

        g2d.setColor(Color.GRAY);
        g2d.fillRect(BOARD_WIDTH - 1, BOARD_TOP_OFFSET, 2, GameBoard.HEIGHT * BLOCK_SIZE);

        g2d.setColor(Color.RED);
        g2d.setFont(GAMEOVER_FONT);
        FontMetrics fm = g2d.getFontMetrics(GAMEOVER_FONT);
        if (gameEngine.getPlayer1().isGameOver()) {
            int x = (BOARD_WIDTH - fm.stringWidth("GAME OVER")) / 2;
            g2d.drawString("GAME OVER", x, getHeight() / 2);
        }
        if (gameEngine.getPlayer2().isGameOver()) {
            int x = BOARD_WIDTH + (BOARD_WIDTH - fm.stringWidth("GAME OVER")) / 2;
            g2d.drawString("GAME OVER", x, getHeight() / 2);
        }
    }

    private void drawBoard(Graphics g, Player player, int offsetX) {
        int[][] grid = player.getBoard().getBoard();
        for (int row = 0; row < GameBoard.HEIGHT; row++) {
            for (int col = 0; col < GameBoard.WIDTH; col++) {
                if (grid[row][col] > 0) {
                    boolean isAnimating = animatingRows.contains(row);
                    int alpha = isAnimating ? 255 - (animationStep * 50) : 255;
                    alpha = Math.max(0, alpha);

                    Color baseColor = Tetromino.getColorById(grid[row][col]);
                    Color faded = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);
                    g.setColor(faded);
                    g.fillRoundRect(offsetX + col * BLOCK_SIZE, BOARD_TOP_OFFSET + row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, 10, 10);
                }
            }
        }

        if (!player.isGameOver()) {
            Tetromino piece = player.getCurrentPiece();
            int ghostY = player.getGhostY();
            g.setColor(new Color(200, 200, 200, 60));
            for (int i = 0; i < piece.getShape().length; i++) {
                for (int j = 0; j < piece.getShape()[i].length; j++) {
                    if (piece.getShape()[i][j] != 0) {
                        int px = offsetX + (player.getCurrentX() + j) * BLOCK_SIZE;
                        int py = BOARD_TOP_OFFSET + (ghostY + i) * BLOCK_SIZE;
                        g.fillRoundRect(px, py, BLOCK_SIZE, BLOCK_SIZE, 10, 10);
                    }
                }
            }

            Color drawColor = piece.getColor();
            g.setColor(drawColor);

            for (int i = 0; i < piece.getShape().length; i++) {
                for (int j = 0; j < piece.getShape()[i].length; j++) {
                    if (piece.getShape()[i][j] != 0) {
                        int px = offsetX + (player.getCurrentX() + j) * BLOCK_SIZE;
                        int py = BOARD_TOP_OFFSET + (player.getCurrentY() + i) * BLOCK_SIZE;
                        g.fillRoundRect(px, py, BLOCK_SIZE, BLOCK_SIZE, 10, 10);
                    }
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) gameEngine.getPlayer1().movePieceLeft();
        if (key == KeyEvent.VK_D) gameEngine.getPlayer1().movePieceRight();
        if (key == KeyEvent.VK_S) gameEngine.getPlayer1().movePieceDown();
        if (key == KeyEvent.VK_W) gameEngine.getPlayer1().rotatePiece();

        if (key == KeyEvent.VK_LEFT) gameEngine.getPlayer2().movePieceLeft();
        if (key == KeyEvent.VK_RIGHT) gameEngine.getPlayer2().movePieceRight();
        if (key == KeyEvent.VK_DOWN) gameEngine.getPlayer2().movePieceDown();
        if (key == KeyEvent.VK_UP) gameEngine.getPlayer2().rotatePiece();

        repaint();
    }
}
