package com.tetris.model;

public class GameBoard {
    private final int rows = 20;
    private final int cols = 10;
    private int[][] board;

    public GameBoard(){
        board = new int[rows][cols];
    }

    public int[][] getBoard(){
        return board;
    }

    public void setCell(int row, int col, int value){
        board[row][col] = value;
    }

    public int getCell(int row, int col){
        return board[row][col];
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }
}