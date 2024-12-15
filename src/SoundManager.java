import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SoundManager {
    private static final Clip
            backgroundMusic = loadSound("File/tetris_theme.wav"),
            clearRowSound = loadSound("File/rowClearSound.wav"),
            gameOverSound = loadSound("File/gameOverSound.wav");
    public static Clip loadSound(String fileName) {
        try {
            File file = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (Exception ignored) {
        }
        System.out.println(fileName+" sound does no exist.");
        return null;
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
    public static void rewindBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0); // Rewind to the beginning
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
    public static void showGifWithSound(String gifPath, String soundPath) {
        stopBackgroundMusic();
        if (!Game.isFINISHED()){
            Game.PAUSE();
        }
        Clip clip = loadSound(soundPath);
        if (clip != null) {
            clip.start();
        }
        JLabel gifLabel = new JLabel(new ImageIcon(gifPath));
        String title = "";
        if (!Game.isFINISHED())
            title = "CONGRATS!!";
        JFrame frame = new JFrame(title);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setIconImage(FileManager.loadImageFromFile("File/TetrisLogo.png"));
        frame.getContentPane().setBackground(Color.black);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.add(gifLabel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Timer timer = new Timer(8500, e -> {
            frame.dispose();
            if (!Game.isFINISHED()) Game.CONTINUE();
            else rewindBackgroundMusic();
            if (Game.isMusicPlayed()) playBackgroundMusic();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
