package com.tetris;

import javax.swing.*;
import com.tetris.view.GamePanel;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("2 Player Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new GamePanel()); // GamePanel ile pencereyi ekliyoruz
        frame.pack();
        frame.setLocationRelativeTo(null); // Ekranın ortasına yerleştiriyoruz
        frame.setVisible(true);
    }
}
