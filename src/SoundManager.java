import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundManager {
    private static final Clip
            backgroundMusic = loadSound("tetris_theme"),
            clearRowSound = loadSound("rowClearSound"),
            gameOverSound = loadSound("gameOverSound");

    private static Clip loadSound(String fileName) {
        try {
            File file = new File("audio/" + fileName + ".wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (Exception ignored) {
        }
        System.out.println(fileName + " sound does no exist.");
        return null;
    }

    public static void rewindBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0); // Rewind to the beginning
        }
    }

    public static void playBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        }
    }

    public static void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public static void playClearRowSound() {
        if (clearRowSound != null) {
            clearRowSound.setFramePosition(0);
            clearRowSound.start();
        }
    }

    public static void playGameOverSound() {
        if (gameOverSound != null) {
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }
    }
}
