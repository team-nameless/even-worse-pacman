package Background;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SoundPlayer {
    private Clip clip;

    private boolean loop;

    public Clip getClip() {
        return clip;
    }

    public boolean getLoop() {
        return loop;
    }

    public SoundPlayer(final String name, boolean loop) {
        this(name);
        this.loop = loop;
    }

    public SoundPlayer(final String name) {
        try {
            this.clip = AudioSystem.getClip();

            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Files.newInputStream(Paths.get("resources/sounds/" + name))));

            getClip().open(inputStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ignored) {
        }
    }

    public synchronized void start() {
        new Thread(() -> {
            try {
                if (getLoop()) getClip().loop(Clip.LOOP_CONTINUOUSLY);
                getClip().start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }

    public synchronized void stop() {
        new Thread(() -> {
            try {
                getClip().stop();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }
}