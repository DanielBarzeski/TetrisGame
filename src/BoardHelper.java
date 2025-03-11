import java.awt.*;
import java.util.stream.IntStream;

public class BoardHelper extends BoardFactory {
    private final int bound;

    public BoardHelper() {
        super();
        this.bound = Math.min(Game.ROWS, Game.COLS);
        spawnNewBlock();
    }

    protected void updateGhostBlockPosition() {
        int dropY = getCurrentBlock().getY();
        int originalX = getCurrentBlock().getX();
        while (canMove(getCurrentBlock().getShape(), originalX, dropY + 1)) {
            dropY++;
        }
        setGhostBlockPosition(new Point(originalX, dropY));
    }

    public synchronized void spawnNewBlock() {
        if (getNextBlock() != null) {
            if (!isPlaceClear()) {
                Game.END();
                SoundManager.playGameOverSound();
                return;
            }
            setCurrentBlock(new Block(getNextBlock()));
        } else setCurrentBlock(Block.createRandomBlock(bound));
        blocks.add(getCurrentBlock()); // Add new block to the list
        updateGhostBlockPosition();
        setNextBlock(Block.createRandomBlock(bound));
    }


    private boolean isPlaceClear() {
        return IntStream.range(0, getMatrix()[1].length).noneMatch(i -> IntStream.range(0, 3).anyMatch(j -> getMatrix()[j][i] != 0));
    }

    public boolean canMove(int[][] shape, int x, int y) {
        if (shape == null)
            return false;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int newX = x + col;
                    int newY = y + row;
                    if (newX < 0 || newX >= Game.COLS || newY < 0 || newY >= Game.ROWS || getMatrix()[newY][newX] != 0)
                        return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("The matrix is:");
        String blue = "\u001B[34m";
        String reset = "\u001B[0m";
        str.append("\n");
        for (int row = 0; row < Game.ROWS; row++) {
            str.append(row).append(":");
            if (row < 10)
                str.append(" ");
            str.append("[");
            for (int col = 0; col < Game.COLS; col++) {
                if (getMatrix()[row][col] > 0)
                    str.append(blue).append(getMatrix()[row][col] % 100).append(reset);
                    // In the game the id can go up to 100 and more, but it doesn't look good when printing it
                    // so that's why I use -> % 100.
                    // if the zero is blue it means that the real id can be 100 or even 1000 or even higher, but not zero.
                else
                    str.append(0);
                if (col < Game.COLS - 1)
                    str.append(",");
                if (getMatrix()[row][col] % 100 < 10)
                    str.append(" ");
            }
            str.append("]\n");
        }
        str.append("   |");
        for (int col = 0; col < Game.COLS; col++) {
            str.append(col);
            if (col < 10)
                str.append(" ");
            str.append("|");
        }
        if (Game.isFINISHED())
            str.append("\nGAME OVER!");
        return str.toString();
    }

}
