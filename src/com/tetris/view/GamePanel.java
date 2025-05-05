// GamePanel.java (fixed color indexing and ensured each shape has unique color)
package com.tetris.view;

import com.tetris.controller.GameEngine;
import com.tetris.model.GameBoard;
import com.tetris.model.Tetromino;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.tetris.Player;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    private GameEngine gameEngine;
    private Timer animationTimer;
    private final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private final Color PANEL_COLOR = new Color(50, 50, 50);
    private final Font SCORE_FONT = new Font("Verdana", Font.BOLD, 28);
    private final Font LABEL_FONT = new Font("Verdana", Font.BOLD, 20);
    private final Font GAMEOVER_FONT = new Font("Verdana", Font.BOLD, 40);
    private final Color SCORE_COLOR = new Color(144, 238, 144);
    private final Color[] TETROMINO_COLORS = {
            new Color(255, 170, 170), // I
            new Color(255, 255, 170), // O
            new Color(170, 255, 170), // T
            new Color(170, 255, 255), // L
            new Color(170, 170, 255), // J
            new Color(255, 170, 255), // S
            new Color(255, 210, 170)  // Z
    };

    private final int BLOCK_SIZE = 28;
    private final int BOARD_TOP_OFFSET = 90;
    private final int BOARD_WIDTH = GameBoard.WIDTH * BLOCK_SIZE;

    public GamePanel() {
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH * 2 + 20, BOARD_TOP_OFFSET + GameBoard.HEIGHT * BLOCK_SIZE));
        addKeyListener(this);

        GameBoard board1 = new GameBoard();
        GameBoard board2 = new GameBoard();

        gameEngine = new GameEngine(board1, board2);

        animationTimer = new Timer(500, this);
        animationTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameEngine.updateGame();
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
        drawBoard(g2d, gameEngine.getPlayer2(), BOARD_WIDTH + 20);

        g2d.setColor(SCORE_COLOR);
        g2d.setFont(SCORE_FONT);
        g2d.drawString("P1 Skor: " + gameEngine.getPlayer1().getScore(), 10, 40);
        g2d.drawString("P2 Skor: " + gameEngine.getPlayer2().getScore(), BOARD_WIDTH + 30, 40);

        g2d.setFont(LABEL_FONT);
        g2d.drawString("OYUNCU 1", 10, 65);
        g2d.drawString("OYUNCU 2", BOARD_WIDTH + 30, 65);

        g2d.setColor(Color.GRAY);
        g2d.fillRect(BOARD_WIDTH + 10, BOARD_TOP_OFFSET, 10, GameBoard.HEIGHT * BLOCK_SIZE);

        g2d.setColor(Color.RED);
        g2d.setFont(GAMEOVER_FONT);
        FontMetrics fm = g2d.getFontMetrics(GAMEOVER_FONT);
        if (gameEngine.getPlayer1().isGameOver()) {
            int x = (BOARD_WIDTH - fm.stringWidth("GAME OVER")) / 2;
            g2d.drawString("GAME OVER", x, getHeight() / 2);
        }
        if (gameEngine.getPlayer2().isGameOver()) {
            int x = BOARD_WIDTH + 20 + (BOARD_WIDTH - fm.stringWidth("GAME OVER")) / 2;
            g2d.drawString("GAME OVER", x, getHeight() / 2);
        }
    }

    private void drawBoard(Graphics g, Player player, int offsetX) {
        int[][] grid = player.getBoard().getBoard();
        for (int row = 0; row < GameBoard.HEIGHT; row++) {
            for (int col = 0; col < GameBoard.WIDTH; col++) {
                if (grid[row][col] != 0) {
                    int colorIndex = Math.floorMod(grid[row][col], TETROMINO_COLORS.length);
                    g.setColor(TETROMINO_COLORS[colorIndex]);
                    g.fillRoundRect(offsetX + col * BLOCK_SIZE, BOARD_TOP_OFFSET + row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, 10, 10);
                }
            }
        }

        if (!player.isGameOver()) {
            Tetromino piece = player.getCurrentPiece();
            Color pieceColor = piece.getColor();
            int matchedIndex = -1;
            for (int i = 0; i < TETROMINO_COLORS.length; i++) {
                if (TETROMINO_COLORS[i].equals(pieceColor)) {
                    matchedIndex = i;
                    break;
                }
            }
            Color drawColor = matchedIndex >= 0 ? TETROMINO_COLORS[matchedIndex] : pieceColor;
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
