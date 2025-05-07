package com.tetris.view;

import com.tetris.view.GamePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("2 Player Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();

        // Set slightly smaller dimensions to fit better on standard screens
        frame.setSize(760, 840); // adjust width and height to reduce overflow
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
