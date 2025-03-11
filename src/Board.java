import java.util.stream.IntStream;

public class Board extends BoardHelper {

    public Board() {
        super();
    }

    private void clearBlock(Block block) {
        int[][] shape = block.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int boardX = block.getX() + col;
                    int boardY = block.getY() + row;
                    if (boardX >= 0 && boardX < Game.COLS && boardY >= 0 && boardY < Game.ROWS)
                        getMatrix()[boardY][boardX] = 0;
                }
            }
        }
    }

    private void placeBlock(Block block) {
        int[][] shape = block.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int boardX = block.getX() + col;
                    int boardY = block.getY() + row;
                    if (boardX >= 0 && boardX < Game.COLS && boardY >= 0 && boardY < Game.ROWS)
                        getMatrix()[boardY][boardX] = block.getId();
                }
            }
        }
    }

    private void clearRow(int row) {
        IntStream.range(0, Game.COLS).forEach(col -> getMatrix()[row][col] = 0);
    }

    private void shiftDown(int fromRow) {
        IntStream.iterate(fromRow, row -> row > 0, row -> row - 1).forEach(row -> System.arraycopy(getMatrix()[row - 1], 0, getMatrix()[row], 0, Game.COLS));
        IntStream.range(0, Game.COLS).forEach(col -> getMatrix()[0][col] = 0);
    }

    public synchronized void dropBlock() {
        if (getCurrentBlock() != null) {
            while (true) {
                clearBlock(getCurrentBlock());
                getCurrentBlock().setY(getCurrentBlock().getY() + 1);
                if (!canMove(getCurrentBlock().getShape(), getCurrentBlock().getX(), getCurrentBlock().getY())) {
                    getCurrentBlock().setY(getCurrentBlock().getY() - 2);
                    break;
                }
                placeBlock(getCurrentBlock());
            }
        }
    }

    public void clearFullRows() {
        for (int row = 0; row < Game.ROWS; row++) {
            boolean isFull = true;
            for (int col = 0; col < Game.COLS; col++) {
                if (getMatrix()[row][col] == 0) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                clearRow(row);
                shiftDown(row);
                SoundManager.playClearRowSound();
                setScore(getScore() + 10);
            }
        }
    }

    public void moveBlock(int dx, int dy) {
        if (getCurrentBlock() != null) {
            clearBlock(getCurrentBlock());
            getCurrentBlock().setX(getCurrentBlock().getX() + dx);
            getCurrentBlock().setY(getCurrentBlock().getY() + dy);
            updateGhostBlockPosition();
            if (!canMove(getCurrentBlock().getShape(), getCurrentBlock().getX(), getCurrentBlock().getY())) {
                getCurrentBlock().setX(getCurrentBlock().getX() - dx);
                getCurrentBlock().setY(getCurrentBlock().getY() - dy);
                updateGhostBlockPosition();
                if (dy == 1) {
                    placeBlock(getCurrentBlock());
                    setScore(getScore() + 5);
                    clearFullRows();
                    spawnNewBlock();
                }
            }
            placeBlock(getCurrentBlock());
        }
    }

    public synchronized void rotateBlock() {
        if (getCurrentBlock() != null) {
            clearBlock(getCurrentBlock());
            getCurrentBlock().rotate(this);
            updateGhostBlockPosition();
            placeBlock(getCurrentBlock());
        }
    }
}