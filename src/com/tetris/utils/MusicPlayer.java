package com.tetris.utils;

import javax.sound.sampled.*;
import java.io.File;

public class MusicPlayer {

    private Clip clip;

    // Müzik dosyasını çalma
    public void playMusic(String musicFile) {
        try {
            // Müzik dosyasının tam yolunu belirtin
            File file = new File(getClass().getResource("/com/tetris/sounds/" + musicFile).toURI());

            if (file.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY); // Müzik sürekli çalsın
            } else {
                System.out.println("Müzik dosyası bulunamadı: " + musicFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Müzik durdurma
    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}

