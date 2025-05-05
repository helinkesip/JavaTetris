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

    public GamePanel() {
        setFocusable(true);
        setPreferredSize(new Dimension(640, 520));  // Yüksekliği artırdık (520px)
        addKeyListener(this);

        GameBoard board1 = new GameBoard();
        GameBoard board2 = new GameBoard();

        gameEngine = new GameEngine(board1, board2);

        animationTimer = new Timer(50, this);  // Daha hızlı animasyon
        animationTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Arka Plan Rengi: Siyah arka plan
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 80, getWidth(), getHeight() - 80);  // Oyun alanını üstteki yazılardan ayırdık

        // Yazı alanı (üst ve alt panel) için renk
        g.setColor(new Color(40, 40, 40));  // Daha farklı bir renk
        g.fillRect(0, 0, getWidth(), 80);  // Üst panel
        g.fillRect(0, getHeight() - 80, getWidth(), 80);  // Alt panel

        // Oyuncu 1'in ve Oyuncu 2'nin oyun alanlarını çiziyoruz
        drawBoard(g, gameEngine.getPlayer1(), 0);
        drawBoard(g, gameEngine.getPlayer2(), 320);  // Oyuncu 2 için sağda daha fazla boşluk (320px)

        // Skor Yazıları
        g.setColor(Color.CYAN); // Canlı renk (modern)
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("P1 Skor: " + gameEngine.getPlayer1().getScore(), 10, 40);
        g.drawString("P2 Skor: " + gameEngine.getPlayer2().getScore(), 340, 40);  // Skor daha belirgin

        // Oyuncu başlıkları
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("OYUNCU 1", 10, 60);
        g.drawString("OYUNCU 2", 340, 60);

        // Alt panelde "OYUN BİTTİ" veya başka bir bilgi yazısı eklemek isteyebilirsiniz
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Oyun Başladı!", 10, getHeight() - 20);

        // İki ekranı ayıran daha belirgin çizgi
        g.setColor(Color.WHITE);
        g.drawLine(320, 0, 320, getHeight() - 80);  // Alt yazı kısmını çizgiden ayırdık
    }

    private void drawBoard(Graphics g, Player player, int offsetX) {
        int[][] grid = player.getBoard().getBoard();
        for (int row = 0; row < GameBoard.HEIGHT; row++) {
            for (int col = 0; col < GameBoard.WIDTH; col++) {
                if (grid[row][col] != 0) {
                    g.setColor(new Color(grid[row][col]));
                    g.fillRoundRect(offsetX + col * 25, 80 + row * 25, 25, 25, 5, 5);  // Bloklar için daha büyük boyut
                    g.setColor(Color.DARK_GRAY);  // Shadow effect
                    g.fillRoundRect(offsetX + col * 25 + 2, 80 + row * 25 + 2, 25, 25, 5, 5); // Shadow
                }
            }
        }

        // Parçanın animasyonu ve daha parlak renklerle çizimi
        Tetromino piece = player.getCurrentPiece();
        g.setColor(piece.getColor());
        for (int i = 0; i < piece.getShape().length; i++) {
            for (int j = 0; j < piece.getShape()[i].length; j++) {
                if (piece.getShape()[i][j] != 0) {
                    // İlk gelen bloğun zemin üzerine oturması için Y koordinatını doğru ayarlıyoruz
                    int px = offsetX + (player.getCurrentX() + j) * 25;
                    int py = 80 + (player.getCurrentY() + i) * 25; // Bloklar zemin ile aynı yükseklikte olacak
                    g.fillRoundRect(px, py, 25, 25, 5, 5);  // Rounded corners for falling pieces
                    g.setColor(piece.getColor().darker());
                    g.fillRoundRect(px + 2, py + 2, 25, 25, 5, 5); // Shadow effect for falling pieces
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

        // Player 1: WASD
        if (key == KeyEvent.VK_A) gameEngine.getPlayer1().movePieceLeft();
        if (key == KeyEvent.VK_D) gameEngine.getPlayer1().movePieceRight();
        if (key == KeyEvent.VK_S) gameEngine.getPlayer1().movePieceDown();
        if (key == KeyEvent.VK_W) gameEngine.getPlayer1().rotatePiece();

        // Player 2: Arrow Keys
        if (key == KeyEvent.VK_LEFT) gameEngine.getPlayer2().movePieceLeft();
        if (key == KeyEvent.VK_RIGHT) gameEngine.getPlayer2().movePieceRight();
        if (key == KeyEvent.VK_DOWN) gameEngine.getPlayer2().movePieceDown();
        if (key == KeyEvent.VK_UP) gameEngine.getPlayer2().rotatePiece();

        repaint();
    }
}
