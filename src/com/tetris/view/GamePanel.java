package com.tetris.view;

import com.tetris.Player;
import com.tetris.controller.GameEngine;
import com.tetris.controller.PlayerOne;
import com.tetris.controller.PlayerTwo;
import com.tetris.model.GameBoard;
import com.tetris.model.Tetromino;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel implements KeyListener, ActionListener {


    private GameEngine gameEngine;
    private final Timer animationTimer;

    private final Player player1;
    private final Player player2;

    private boolean gameEnded = false;
    private String winnerText = "";

    private final Set<Integer> animatingRows = new HashSet<>();
    private int animationStep = 0;
    private final int ANIMATION_STEPS = 5;

    private final Font SCORE_FONT = new Font("Consolas", Font.BOLD, 18);
    private final Font GAMEOVER_FONT = new Font("Consolas", Font.BOLD, 28);

    private final int BLOCK_SIZE = 24;
    private final int BOARD_TOP_OFFSET = 100;
    private final int BOARD_WIDTH = GameBoard.WIDTH * BLOCK_SIZE;
    private final int BOARD_HEIGHT = GameBoard.HEIGHT * BLOCK_SIZE;
    private final int MARGIN = 30;

    private final int PREVIEW_BLOCK_SIZE = 18;
    private final int PREVIEW_BOX_WIDTH = PREVIEW_BLOCK_SIZE * 5;
    private final int PREVIEW_BOX_HEIGHT = PREVIEW_BLOCK_SIZE * 4;

    private final JButton restartButton;

    public GamePanel(String player1Name, String player2Name) {
        setFocusable(true);
        int totalWidth = BOARD_WIDTH * 2 + MARGIN * 3 + PREVIEW_BOX_WIDTH * 2;
        int totalHeight = BOARD_TOP_OFFSET + BOARD_HEIGHT + MARGIN + 60;
        setPreferredSize(new Dimension(totalWidth, totalHeight));
        addKeyListener(this);

        GameBoard board1 = new GameBoard();
        GameBoard board2 = new GameBoard();

        this.player1 = new PlayerOne(player1Name, board1);
        this.player2 = new PlayerTwo(player2Name, board2);

        gameEngine = new GameEngine(player1, player2);
        gameEngine.setGamePanel(this);


        animationTimer = new Timer(120, this);
        animationTimer.start();

        restartButton = new JButton("Yeniden Oyna");  //Yeniden oyna butonu için özellikler
        restartButton.setFont(new Font("Verdana", Font.BOLD, 16));
        restartButton.setBackground(Color.decode("#D32F2F"));
        restartButton.setForeground(Color.white);
        restartButton.setOpaque(true);
        restartButton.setFocusPainted(false);
        restartButton.setVisible(false);
        restartButton.setBorderPainted(false);
        restartButton.setSize(180, 45);
        restartButton.addActionListener(e -> restartGame());

        setLayout(null);
        add(restartButton);
    }

    public void triggerRowAnimation(Set<Integer> rows) {  //Satır temizleme animasyonu başlatır
        animatingRows.clear();
        animatingRows.addAll(rows);
        animationStep = 0;
    }

    public void restartGame() {
        gameEnded = false;
        winnerText = "";
        stopMusicIfEngineIsValid();
        GameBoard board1 = new GameBoard(); //Yeni tahtalar oluşturur
        GameBoard board2 = new GameBoard();

        Player player1 = new PlayerOne(this.player1.getName(), board1); //Yeni oyuncular oluşturur
        Player player2 = new PlayerTwo(this.player2.getName(), board2);

        gameEngine = new GameEngine(player1, player2);
        gameEngine.setGamePanel(this);

        restartButton.setVisible(false); //Başlangıçta gizli
        requestFocusInWindow();
        repaint();
    }

    private void stopMusicIfEngineIsValid() {
        if (gameEngine != null) {
            gameEngine.getPlayer1().stopMusic();
            gameEngine.getPlayer2().stopMusic();
        }
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
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int centerX = getWidth() / 2;
        int player1X = centerX - BOARD_WIDTH - MARGIN / 2;
        int player2X = centerX + MARGIN / 2;

        drawFramedBoard(g2d, gameEngine.getPlayer1(), player1X);
        drawFramedBoard(g2d, gameEngine.getPlayer2(), player2X);

        drawNextPiece(g2d, gameEngine.getPlayer1().getNextPiece(), player1X - PREVIEW_BOX_WIDTH - 10, BOARD_TOP_OFFSET);
        drawNextPiece(g2d, gameEngine.getPlayer2().getNextPiece(), player2X + BOARD_WIDTH + 10, BOARD_TOP_OFFSET);

        int buttonX = (getWidth() - restartButton.getWidth()) / 2;
        int buttonY = getHeight() - restartButton.getHeight() - 80;
        restartButton.setLocation(buttonX, buttonY);
        restartButton.setVisible(gameEnded);

        Player p1 = gameEngine.getPlayer1();
        Player p2 = gameEngine.getPlayer2();


        if (!gameEnded && (p1.isGameOver() || p2.isGameOver())) { //Puan hesaplayarak kazananı belirleme
            gameEnded = true;

            p1.stopMusic();
            p2.stopMusic();

            if (p1.getScore() > p2.getScore()) {
                winnerText = p1.getName() + " Wins!";
            } else if (p2.getScore() > p1.getScore()) {
                winnerText = p2.getName() + " Wins!";
            } else {
                winnerText = "It's a Tie!";
            }

            p1.checkGameOverOrTie();
        }

        if (gameEnded) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Verdana", Font.BOLD, 32));
            int textWidth = g2d.getFontMetrics().stringWidth(winnerText);
            g2d.drawString(winnerText, (getWidth() - textWidth) / 2, getHeight() / 2 + 40);

            for (int i = 0; i < 150; i++) {
                int x = (int) (Math.random() * getWidth());
                int y = (int) (Math.random() * getHeight());
                int size = 4 + (int) (Math.random() * 6);
                g2d.setColor(new Color(
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255)
                ));
                g2d.fillOval(x, y, size, size);
            }
        }
    }





    private void drawNextPiece(Graphics2D g, Tetromino piece, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 14));
        g.drawString("Next", x + 10, y - 10);

        g.setColor(new Color(60, 60, 60));
        g.fillRoundRect(x, y, PREVIEW_BOX_WIDTH, PREVIEW_BOX_HEIGHT, 12, 12);

        if (piece == null) return;

        int[][] shape = piece.getShape();
        int shapeWidth = shape[0].length;
        int shapeHeight = shape.length;

        int offsetX = (PREVIEW_BOX_WIDTH - shapeWidth * PREVIEW_BLOCK_SIZE) / 2;
        int offsetY = (PREVIEW_BOX_HEIGHT - shapeHeight * PREVIEW_BLOCK_SIZE) / 2;

        g.setColor(piece.getColor());
        for (int i = 0; i < shapeHeight; i++) {
            for (int j = 0; j < shapeWidth; j++) {
                if (shape[i][j] != 0) {
                    int px = x + offsetX + j * PREVIEW_BLOCK_SIZE;
                    int py = y + offsetY + i * PREVIEW_BLOCK_SIZE;
                    g.fillRoundRect(px, py, PREVIEW_BLOCK_SIZE, PREVIEW_BLOCK_SIZE, 6, 6);
                }
            }
        }
    }

    private void drawFramedBoard(Graphics2D g, Player player, int offsetX) {
        g.setColor(new Color(100, 100, 100));
        g.fillRoundRect(offsetX - 8, BOARD_TOP_OFFSET - 50, BOARD_WIDTH + 16, BOARD_HEIGHT + 60, 20, 20);

        g.setColor(new Color(60, 60, 60));
        g.fillRoundRect(offsetX - 4, BOARD_TOP_OFFSET - 42, BOARD_WIDTH + 8, 36, 15, 15);

        g.setColor(Color.WHITE);
        g.setFont(SCORE_FONT);
        g.drawString(player.getName() + ": " + player.getScore(), offsetX + 8, BOARD_TOP_OFFSET - 20);

        drawBoard(g, player, offsetX);
    }

    private void drawBoard(Graphics g, Player player, int offsetX) {
        int[][] grid = player.getBoard().getBoard();

        g.setColor(new Color(20, 20, 20, 160));
        g.fillRect(offsetX, BOARD_TOP_OFFSET, BOARD_WIDTH, BOARD_HEIGHT);

        for (int row = 0; row < GameBoard.HEIGHT; row++) {
            for (int col = 0; col < GameBoard.WIDTH; col++) {
                if (grid[row][col] > 0) {
                    boolean isAnimating = animatingRows.contains(row);
                    int alpha = isAnimating ? 255 - (animationStep * 50) : 255;
                    alpha = Math.max(0, alpha);

                    Color baseColor = Tetromino.getColorById(grid[row][col]);
                    Color faded = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);
                    g.setColor(faded);
                    g.fillRoundRect(offsetX + col * BLOCK_SIZE, BOARD_TOP_OFFSET + row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, 8, 8);
                }
            }
        }

        if (!player.isGameOver()) {
            Tetromino piece = player.getCurrentPiece();
            int ghostY = player.getGhostY();
            g.setColor(new Color(255, 255, 255, 50));
            for (int i = 0; i < piece.getShape().length; i++) {
                for (int j = 0; j < piece.getShape()[i].length; j++) {
                    if (piece.getShape()[i][j] != 0) {
                        int px = offsetX + (player.getCurrentX() + j) * BLOCK_SIZE;
                        int py = BOARD_TOP_OFFSET + (ghostY + i) * BLOCK_SIZE;
                        g.fillRoundRect(px, py, BLOCK_SIZE, BLOCK_SIZE, 8, 8);
                    }
                }
            }

            g.setColor(piece.getColor());
            for (int i = 0; i < piece.getShape().length; i++) {
                for (int j = 0; j < piece.getShape()[i].length; j++) {
                    if (piece.getShape()[i][j] != 0) {
                        int px = offsetX + (player.getCurrentX() + j) * BLOCK_SIZE;
                        int py = BOARD_TOP_OFFSET + (player.getCurrentY() + i) * BLOCK_SIZE;
                        g.fillRoundRect(px, py, BLOCK_SIZE, BLOCK_SIZE, 8, 8);
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameEngine.getPlayer1().isGameOver() || gameEngine.getPlayer2().isGameOver()) return;

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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


}
