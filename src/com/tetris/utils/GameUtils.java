package com.tetris.utils;

import java.util.Random;

public class GameUtils {
    private static Random random = new Random();

    public static int getRandomInt(int min, int max){
        return random.nextInt(max - min + 1) + min;
    }

    public static boolean isEven(int num) {
        return num % 2 == 0;
    }



}
