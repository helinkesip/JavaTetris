package com.tetris.controller;

import com.tetris.model.GameBoard;
import com.tetris.Player;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameEngine {
    private Player player1;
    private Player player2;
    private Timer timer;

    public GameEngine(GameBoard board1, GameBoard board2) {
        this.player1 = new PlayerOne("Player 1", board1);
        this.player2 = new PlayerTwo("Player 2", board2);


        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
            }
        });
        timer.start();
    }

    public void updateGame() {
        if (!player1.isGameOver()) player1.movePieceDown();
        if (!player2.isGameOver()) player2.movePieceDown();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}