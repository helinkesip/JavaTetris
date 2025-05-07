package com.tetris.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class MusicPlayer {

    private Clip clip;

    public void playMusic(String musicFile) {
        URL resource = getClass().getResource("/com/tetris/sounds/" + musicFile);
        if (resource == null) {
            System.err.println("Could not find music file: " + musicFile);
            return;
        }

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(resource);

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}

