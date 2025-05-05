package com.tetris.model;

import java.awt.Color;

public class Tetromino {
    private int[][] shape;
    private int x;
    private int y;
    private Color color;

    public Tetromino(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        this.x = 0;
        this.y = 0;
    }

    public void rotate() {
        int[][] newShape = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                newShape[j][shape.length-1-i] = shape[i][j];
            }
        }
        shape = newShape;
    }

    public int[][] getShape() { return shape; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public Color getColor() { return color; }
}
