
public class Game {
    private static boolean FINISHED, PAUSED, SHOWED;
    public final static int CELL_SIZE = 20, ROWS = 23,COLS = 10, // change cols to 20
            WIDTH = COLS * CELL_SIZE,
            HEIGHT = ROWS * CELL_SIZE;
    private static Board BOARD;

    public static void START() {
        BOARD = new Board();
        SHOWED = false;
        PAUSED = false;
        FINISHED = false;
        System.out.println("THE GAME HAS STARTED!");
        SoundManager.rewindBackgroundMusic();
        SoundManager.playBackgroundMusic();
    }

    public static void END() {
        SHOWED = false;
        PAUSED = true;
        FINISHED = true;
        System.out.println("THE GAME IS FINISHED!");
        SoundManager.stopBackgroundMusic();
    }

    public static void PAUSE() {
        PAUSED = true;
    }

    public static void CONTINUE() {
        PAUSED = false;
    }


    public static boolean isFINISHED() {
        return FINISHED;
    }

    public static boolean isPAUSED() {
        return PAUSED;
    }

    public static Board CONTROL_BOARD() {
        return BOARD;
    }

    public static boolean isSHOWED() {
        return SHOWED;
    }

    public static void setSHOW(boolean state) {
        Game.SHOWED = state;
    }
}