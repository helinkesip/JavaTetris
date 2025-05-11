package com.tetris;

import com.tetris.view.GamePanel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        String player1Name = JOptionPane.showInputDialog(null, "Player 1 adını girin:");  //Player 1 ve Player2 için input girişi alır
        String player2Name = JOptionPane.showInputDialog(null, "Player 2 adını girin:");

        if (player1Name == null || player1Name.isBlank()) player1Name = "Player 1";
        if (player2Name == null || player2Name.isBlank()) player2Name = "Player 2";

        JFrame frame = new JFrame("2 Player Tetris");  // Ana pencere oluşturulur
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel(player1Name, player2Name);  //GamePanel oluşturularak pencereye eklenir
        frame.add(gamePanel);
        frame.setSize(760, 840);  //Pencere boyut ve konumu ayarlanır
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
