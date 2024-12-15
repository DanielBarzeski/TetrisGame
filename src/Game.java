import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Game {
    private static boolean FINISHED, PAUSED, RESTARTING, SHOWED, MUSIC_PLAYED = true;
    private final static int WIDTH = 204, HEIGHT = 404, ROWS = 20, COLS = 10, EXTRA_ROWS = 2;
    // try to change the WIDTH to 404 and in addition try to change the cols to 20
    private static Timer ANIMATOR;
    private static Board CONTROL;
    public static void START(){
        setPAUSED(false);
        setFINISHED(false);
        ANIMATOR.start();
        getCONTROL().spawnNewBlock();
        System.out.println("THE GAME STARTED!");
    }
    public static void END(){
        setPAUSED(true);
        setFINISHED(true);
        ANIMATOR.setDelay(500);
        ANIMATOR.stop();
        System.out.println("GAME OVER!");
    }
    public static void CONTINUE(){
        setPAUSED(false);
    }
    public static void PAUSE(){
        setPAUSED(true);
    }

    public static boolean isFINISHED() {
        return FINISHED;
    }

    private static void setFINISHED(boolean FINISHED) {
        Game.FINISHED = FINISHED;
    }

    public static boolean isPAUSED() {
        return PAUSED;
    }

    private static void setPAUSED(boolean PAUSED) {
        Game.PAUSED = PAUSED;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static Timer getANIMATOR() {
        return ANIMATOR;
    }

    public static void setANIMATOR(int delay, ActionListener listener) {
        Game.ANIMATOR = new Timer(delay,listener);
    }

    public static boolean isRESTARTING() {
        return RESTARTING;
    }

    public static void setRESTARTING(boolean RESTARTING) {
        Game.RESTARTING = RESTARTING;
        if (RESTARTING) {
            Game.changeCONTROL(new Board(ROWS+EXTRA_ROWS, COLS,
                    Game.getWIDTH()-2, Game.getHEIGHT()-2+EXTRA_ROWS*ROWS,
                    Color.WHITE, true // try to change to false
            ));
            if (!Game.isFINISHED() && getANIMATOR() != null) {
                Game.END();
            }
            if (Game.isFINISHED()) {
                Game.getANIMATOR().setDelay(500);
                Game.START();
            }
        }
    }
    public static Board getCONTROL() {
        return CONTROL;
    }
    public static void changeCONTROL(Board CONTROL) {
        Game.CONTROL = CONTROL;
    }

    public static boolean isSHOWED() {
        return SHOWED;
    }

    public static void setSHOWED(boolean SHOWED) {
        Game.SHOWED = SHOWED;
    }

    public static boolean isMusicPlayed() {
        return MUSIC_PLAYED;
    }

    public static void setMusicPlayed(boolean state) {
        MUSIC_PLAYED = state;
        if (isMusicPlayed())
            SoundManager.playBackgroundMusic();
        else SoundManager.stopBackgroundMusic();
    }
}
