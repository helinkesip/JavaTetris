package com.tetris.model;

import java.awt.*;

public abstract class Tetromino {
    protected Point[] blocks;
    protected Color color;

    public Tetromino(Color color){
        this.color = color;
        this.blocks = new Point[4];
    }

    public Point[] getBlocks(){
        return blocks;
    }

    public Color getColor(){
        return color;
    }

    public void move(int dx, int dy){
        for(Point p: blocks){
            p.x = dx;
            p.y = dy;
        }
    }

    public abstract void rotate();
}