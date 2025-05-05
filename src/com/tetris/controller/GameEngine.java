package com.tetris.controller;

import com.tetris.controller.PlayerOne;
import com.tetris.controller.PlayerTwo;
import com.tetris.model.GameBoard;
import com.tetris.view.GamePanel;
import com.tetris.Player;

import java.util.Set;

public class GameEngine {
    private Player player1;
    private Player player2;
    private GamePanel panel;

    public GameEngine(GameBoard board1, GameBoard board2) {
        this.player1 = new PlayerOne("Player 1", board1);
        this.player2 = new PlayerTwo("Player 2", board2);
    }

    public void setGamePanel(GamePanel panel) {
        this.panel = panel;
    }

    public void updateGame() {
        Player[] players = { player1, player2 };
        for (Player player : players) {
            player.movePieceDown(); // Tetrominoyu indirir ve gerekiyorsa yerleştirir
            Set<Integer> cleared = player.getBoard().clearLines(); // Satır silme
            player.addScore(cleared.size() * 100); // Skor artırma

            // Animasyonu tetikle
            if (!cleared.isEmpty() && panel != null) {
                panel.triggerRowAnimation(cleared);
            }
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
