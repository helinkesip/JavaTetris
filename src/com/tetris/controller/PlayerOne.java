package com.tetris.controller;

import com.tetris.Player;
import java.awt.event.KeyEvent;
import com.tetris.model.GameBoard;

public class PlayerOne extends Player {
    public PlayerOne(String name, GameBoard board) {
        super(name, board);
    }

    @Override
    public void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_A: movePieceLeft(); break;
            case KeyEvent.VK_D: movePieceRight(); break;
            case KeyEvent.VK_S: movePieceDown(); break;
            case KeyEvent.VK_W: rotatePiece(); break;
        }
    }
}
