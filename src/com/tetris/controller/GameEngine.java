package com.tetris.controller;

import com.tetris.view.GamePanel;
import com.tetris.Player;

import java.util.Set;

public class GameEngine {
    private final Player player1;
    private final Player player2;
    private GamePanel panel;

    public GameEngine(Player player1, Player player2) { //Oyuncular birbirine referans verilir, rakip olarak görünür
        this.player1 = player1;
        this.player2 = player2;
        player1.setOtherPlayer(player2);
        player2.setOtherPlayer(player1);
    }

    public void setGamePanel(GamePanel panel) {
        this.panel = panel;
    }

    public void updateGame() {
        Player[] players = { player1, player2 };
        for (Player player : players) {
            player.movePieceDown(); //Parçayı aşağı hareket ettirir
            Set<Integer> cleared = player.getBoard().clearLines(); //Tamamlanan satırı temizler
            player.addScore(cleared.size() * 100); //Temizlenen satır için 100 puan eklendi
            if (!cleared.isEmpty() && panel != null) {
                panel.triggerRowAnimation(cleared);  //Satır temizleme animasyonu
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
