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
    private Player otherPlayer; // Diğer oyuncuyu tutacak

    public Player(String name, GameBoard board) {
        this.name = name;
        this.board = board;
        this.score = 0;
        this.nextPiece = GameUtils.createRandomTetromino();
        this.musicPlayer = new MusicPlayer();
        this.musicPlayer.stopMusic();
        this.musicPlayer.playMusic("game-music.wav");
        spawnNewPiece();
    }

    public void spawnNewPiece() {
        currentPiece = nextPiece;
        nextPiece = GameUtils.createRandomTetromino();

        switch (currentPiece.getId()) {
            case 0:
                currentX = board.getWidth() / 2 - 2;
                currentY = 0;
                break;
            case 1:
                currentX = board.getWidth() / 2 - 1;
                currentY = 0;
                break;
            default:
                currentX = board.getWidth() / 2 - currentPiece.getShape()[0].length / 2;
                currentY = 0;
        }

        if (!board.isValidPosition(currentPiece, currentX, currentY)) {
            System.out.println("Game Over for " + name);
            gameOver = true;
            musicPlayer.stopMusic();
            checkGameOverOrTie();
        }
    }

    public void movePieceDown() {
        if (gameOver) return;
        if (board.isValidPosition(currentPiece, currentX, currentY + 1)) {
            currentY++;
        } else {
            board.mergePiece(currentPiece, currentX, currentY);
            spawnNewPiece();
        }
    }

    public void movePieceLeft() {
        if (gameOver) return;
        if (board.isValidPosition(currentPiece, currentX - 1, currentY)) {
            currentX--;
        }
    }

    public void movePieceRight() {
        if (gameOver) return;
        if (board.isValidPosition(currentPiece, currentX + 1, currentY)) {
            currentX++;
        }
    }

    public void rotatePiece() {
        if (gameOver) return;
        currentPiece.rotate();

        if (!board.isValidPosition(currentPiece, currentX, currentY)) {
            if (currentPiece.getId() == 0) {
                int newX = currentX;

                while (!board.isValidPosition(currentPiece, newX, currentY) && newX > 0) {
                    newX--;
                }

                if (!board.isValidPosition(currentPiece, newX, currentY)) {
                    newX = currentX;
                    while (!board.isValidPosition(currentPiece, newX, currentY)
                            && newX < board.getWidth() - currentPiece.getShape()[0].length) {
                        newX++;
                    }
                }

                if (board.isValidPosition(currentPiece, newX, currentY)) {
                    currentX = newX;
                } else {
                    currentPiece.rotate(); // 3 kez daha döndürerek eski haline getir
                    currentPiece.rotate();
                    currentPiece.rotate();
                }
            } else {
                currentPiece.rotate(); // 3 kez daha döndürerek eski haline getir
                currentPiece.rotate();
                currentPiece.rotate();
            }
        }
    }

    public boolean isValidPosition(Tetromino piece, int x, int y) {
        int[][] shape = piece.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int newX = x + j;
                    int newY = y + i;

                    if (newX < 0 || newX >= board.getWidth() || newY >= board.getHeight()) {
                        return false;
                    }

                    if (newY >= 0 && board.getBoard()[newY][newX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setOtherPlayer(Player otherPlayer) {
        this.otherPlayer = otherPlayer;
    }

    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stopMusic();
        }
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

    // Müzik geçişlerini kontrol et
    public void checkGameOverOrTie() {
        // Eğer diğer oyuncu henüz ayarlanmamışsa, bekle
        if (otherPlayer == null) {
            System.err.println("Bekleyen diğer oyuncu yok. Oyun sonu veya beraberlik kontrolü atlandı.");
            return;
        }
        if (isGameOver() && otherPlayer.isGameOver()) {
            // Skorları karşılaştır
            if (this.getScore() == otherPlayer.getScore()) {
                // Beraberlik durumu
                musicPlayer.stopMusic();
                musicPlayer.playMusic("game-over-tie.wav");
            } else {
                // Kazanan oyuncu
                musicPlayer.stopMusic();
                musicPlayer.playMusic("game-win.wav");
            }
        }

    }

    public abstract void handleKeyPress(int keyCode);

}
