package com.tetris;

import com.tetris.view.GamePanel;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GamePanel gamePanel = new GamePanel();
            gamePanel.createAndShowGUI();
        });
    }
}
