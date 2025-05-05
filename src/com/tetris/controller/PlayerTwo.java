package com.tetris.controller;

import com.tetris.Player;
import java.awt.event.KeyEvent;
import com.tetris.model.GameBoard;

public class PlayerTwo extends Player {
    public PlayerTwo(String name, GameBoard board) {
        super(name, board);
    }

    @Override
    public void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT: movePieceLeft(); break;
            case KeyEvent.VK_RIGHT: movePieceRight(); break;
            case KeyEvent.VK_DOWN: movePieceDown(); break;
            case KeyEvent.VK_UP: rotatePiece(); break;
        }
    }
}
