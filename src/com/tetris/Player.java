package com.tetris;

import com.tetris.model.GameBoard;
import com.tetris.model.Tetromino;
import com.tetris.utils.GameUtils;
import com.tetris.utils.MusicPlayer;

public abstract class Player {
    private final String name;
    private int score;
    private final GameBoard board;
    private Tetromino currentPiece;
    private Tetromino nextPiece;
    private int currentX, currentY;
    private boolean gameOver = false;
    private final MusicPlayer musicPlayer;
    private Player otherPlayer;

    public Player(String name, GameBoard board) {
        this.name = name;
        this.board = board;
        this.score = 0; //Başlangıç puanı 0
        this.nextPiece = GameUtils.createRandomTetromino();
        this.musicPlayer = new MusicPlayer();
        this.musicPlayer.stopMusic();
        this.musicPlayer.playMusic("game-music.wav");
        spawnNewPiece();
    }

    public void spawnNewPiece() {
        if (this.isGameOver()) return;

        currentPiece = nextPiece; //Mevcut parça sonraki parça olur
        nextPiece = GameUtils.createRandomTetromino(); //Yeni rastgele parça

        switch (currentPiece.getId()) {  //Parça tipine göre başlangıç konumu belirlenir
            case 0 -> {
                currentX = board.getWidth() / 2 - 2;
                currentY = 0;
            }
            case 1 -> {
                currentX = board.getWidth() / 2 - 1;
                currentY = 0;
            }
            default -> {
                currentX = board.getWidth() / 2 - currentPiece.getShape()[0].length / 2;
                currentY = 0;
            }
        }

        if (!board.isValidPosition(currentPiece, currentX, currentY)) {
            System.out.println("Game Over for " + name);
            gameOver = true;
            musicPlayer.stopMusic();

            if (otherPlayer != null) {
                this.checkGameOverOrTie();
                otherPlayer.checkGameOverOrTie();
            }
        }
    }

    public void movePieceDown() {  //Aşağı hareket kontrolu
        if (gameOver) return;
        if (board.isValidPosition(currentPiece, currentX, currentY + 1)) {
            currentY++;
        } else {
            board.mergePiece(currentPiece, currentX, currentY);
            spawnNewPiece();
        }
    }

    public void movePieceLeft() { //Sol hareket kontrolu
        if (gameOver) return;
        if (board.isValidPosition(currentPiece, currentX - 1, currentY)) {
            currentX--;
        }
    }

    public void movePieceRight() { //Sağ hareket kontrolu
        if (gameOver) return;
        if (board.isValidPosition(currentPiece, currentX + 1, currentY)) {
            currentX++;
        }
    }

    public void rotatePiece() {
        if (gameOver) return;
        currentPiece.rotate();

        if (!board.isValidPosition(currentPiece, currentX, currentY)) {
            currentPiece.rotate();
            currentPiece.rotate();
            currentPiece.rotate();
        }
    }

    public boolean isValidPosition(Tetromino piece, int x, int y) {
        int[][] shape = piece.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int newX = x + j;
                    int newY = y + i;

                    if (newX < 0 || newX >= board.getWidth() || newY >= board.getHeight()) return false;  //Tahta sınırları içinde mi kontrol
                    if (newY >= 0 && board.getBoard()[newY][newX] != 0) return false; //Diğer parçalarla çarpışıyor mu kontrol
                }
            }
        }
        return true;
    }

    public void setOtherPlayer(Player otherPlayer) {
        this.otherPlayer = otherPlayer;
    }

    public void stopMusic() {
        if (musicPlayer != null) musicPlayer.stopMusic();
    }

    public void addScore(int value) {
        this.score += value;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public GameBoard getBoard() {
        return board;
    }

    public Tetromino getCurrentPiece() {
        return currentPiece;
    }

    public Tetromino getNextPiece() {
        return nextPiece;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public int getGhostY() {
        int ghostY = currentY;
        while (board.isValidPosition(currentPiece, currentX, ghostY + 1)) {
            ghostY++;
        }
        return ghostY;
    }

    public void checkGameOverOrTie() {  //Kazanma durumlarında çalınacak müzikler
        if (otherPlayer == null) return;
        if (!this.isGameOver() && !otherPlayer.isGameOver()) return;

        musicPlayer.stopMusic();

        if (this.getScore() == otherPlayer.getScore()) {
            System.out.println("🔔 Beraberlik! Skor: " + score);
            musicPlayer.playMusic("game-over-tie.wav");
        } else if (this.getScore() > otherPlayer.getScore()) {
            System.out.println("🏆 " + name + " kazandı!");
            musicPlayer.playMusic("game-win.wav");
            musicPlayer.playOnce("cheering.wav");
        } else {
            System.out.println("🏆 " + otherPlayer.getName() + " kazandı!");
            musicPlayer.playMusic("game-win.wav");
            musicPlayer.playOnce("cheering.wav");
        }
    }

    public abstract void handleKeyPress(int keyCode);
}
