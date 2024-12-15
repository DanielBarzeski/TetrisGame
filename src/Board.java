import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.IntStream;
public class Board {
    private final int[][] matrix;
    private final int cellWidth, cellHeight, rows, cols, bound;
    private int score;
    private final Color outLine;
    private final ArrayList<Block> blocks;
    private Block currentBlock, nextBlock;
    private final boolean justOutLine;
    private Point ghostBlockPosition;
    public Board(int rows, int cols, int width, int height, Color outLine, boolean justOutLine) {
        this.rows = rows;
        this.cols = cols;
        this.bound = Math.min(rows, cols);
        this.matrix = new int[rows][cols];
        this.outLine = outLine;
        this.justOutLine = justOutLine;
        this.cellWidth = width / cols;
        this.cellHeight = height / rows;
        Block.UPDATE(cellWidth, cellHeight);
        this.blocks = new ArrayList<>();
    }
    public void draw(Graphics g, int startX, int startY) {
        g.setColor(Color.gray);
        g.drawRect(1, 1, Game.getWIDTH() - 3, Game.getHEIGHT() - 3);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                g.drawRect(startX + col * cellWidth, startY + row * cellHeight, cellWidth - 1, cellHeight - 1);
            }
        }
        drawGhostBlock(g,startX,startY);
        g.setColor(Color.white);
        if (Game.isSHOWED() && currentBlock != null) {
            for (int i = 0; i < currentBlock.getShape().length; i++) {
                for (int j = 0; j < currentBlock.getShape()[0].length; j++) {
                    g.drawRoundRect(startX + (currentBlock.getX()+i) * cellWidth, startY + (currentBlock.getY()+j) * cellHeight, cellWidth, cellHeight, 10, 10);
                }
            }
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int blockId = matrix[row][col];
                if (blockId != 0) {
                    Block block = getBlockById(blockId);
                    if (block != null) {
                        g.drawImage(block.getImage(), startX + col * cellWidth, startY + row * cellHeight, null);
                        drawOutLine(row, col, blockId, g, startX, startY);
                    }
                }
            }
        }
        g.setColor(Color.red);
        for (int row = 0; row < rows; row++) {
            boolean isRowFull = true;
            for (int col = 0; col < cols; col++) {
                if (matrix[row][col] == 0) {
                    isRowFull = false;
                    break;
                }
            }
            if (isRowFull) {
                g.fillRect(startX-1, startY+row * cellHeight-1, cols * cellWidth+2, cellHeight+2);
            }
        }
    }
    private void drawOutLine(int row, int col, int blockId, Graphics g, int startX, int startY) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(outLine);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        if (justOutLine) {
            if (row == 0 || matrix[row - 1][col] != blockId) { // Top edge
                g2d.drawLine(startX + col * cellWidth, startY + row * cellHeight, startX + (col + 1) * cellWidth, startY + row * cellHeight);
            }
            if (row == rows - 1 || matrix[row + 1][col] != blockId) { // Bottom edge
                g2d.drawLine(startX + col * cellWidth, startY + (row + 1) * cellHeight, startX + (col + 1) * cellWidth, startY + (row + 1) * cellHeight);
            }
            if (col == 0 || matrix[row][col - 1] != blockId) { // Left edge
                g2d.drawLine(startX + col * cellWidth, startY + row * cellHeight, startX + col * cellWidth, startY + (row + 1) * cellHeight);
            }
            if (col == cols - 1 || matrix[row][col + 1] != blockId) { // Right edge
                g2d.drawLine(startX + (col + 1) * cellWidth, startY + row * cellHeight, startX + (col + 1) * cellWidth, startY + (row + 1) * cellHeight);
            }
        } else {
            g2d.drawRoundRect(startX + col * cellWidth, startY + row * cellHeight, cellWidth - 1, cellHeight - 1, 5, 5);
        }
    }
    private void updateGhostBlockPosition() {
        int dropY = currentBlock.getY();
        int originalX = currentBlock.getX();
        while (canMove(currentBlock.getShape(), originalX, dropY + 1)) {
            dropY++;
        }
        ghostBlockPosition = new Point(originalX,dropY);
    }


    private void drawGhostBlock(Graphics g, int startX, int startY) {
        if (ghostBlockPosition != null) {
            g.setColor(new Color(255, 255, 255, 100));
            int[][] shape = currentBlock.getShape();
            int ghostX = ghostBlockPosition.x;
            int ghostY = ghostBlockPosition.y;
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] != 0)
                        g.fillRect(startX + (ghostX + col) * cellWidth, startY + (ghostY + row) * cellHeight, cellWidth,cellHeight);
                }
            }
        }
    }
    public void drawBlock(Block block, BufferedImage image, int x, int y, int width, int height, Graphics g) {
        int[][] shape = block.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0)
                    g.drawImage(image, x + col * width, y + row * height, null);
            }
        }
    }
    public synchronized void spawnNewBlock() {
        if (nextBlock != null) {
            if (!isPlaceClear()) {
                Game.END();
                SoundManager.playGameOverSound();
                SoundManager.showGifWithSound("File/game-over.gif", "File/game-over- sound.wav");
                return;
            }
            currentBlock = new Block(nextBlock);
        } else currentBlock = Block.createRandomBlock(bound);
        blocks.add(currentBlock); // Add new block to the list
        updateGhostBlockPosition();
        nextBlock = Block.createRandomBlock(bound);
    }

    private void clearBlock(Block block) {
        int[][] shape = block.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int boardX = block.getX() + col;
                    int boardY = block.getY() + row;
                    if (boardX >= 0 && boardX < cols && boardY >= 0 && boardY < rows)
                        matrix[boardY][boardX] = 0;
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
                    if (boardX >= 0 && boardX < cols && boardY >= 0 && boardY < rows)
                        matrix[boardY][boardX] = block.getId();
                }
            }
        }
    }

    private boolean isPlaceClear() {
        return IntStream.range(0, matrix[1].length).noneMatch(i -> IntStream.range(0, 3).anyMatch(j -> matrix[j][i] != 0));
    }

    private void clearRow(int row) {
        IntStream.range(0, cols).forEach(col -> matrix[row][col] = 0);
    }

    private void shiftDown(int fromRow) {
        IntStream.iterate(fromRow, row -> row > 0, row -> row - 1).filter(row -> cols >= 0).forEach(row -> System.arraycopy(matrix[row - 1], 0, matrix[row], 0, cols));
        IntStream.range(0, cols).forEach(col -> matrix[0][col] = 0);
    }

    private synchronized Block getBlockById(int id) {
        return blocks.stream().filter(block -> block.getId() == id).findFirst().orElse(null);
    }

    public synchronized void dropBlock() {
        if (currentBlock != null) {
            while (true) {
                clearBlock(currentBlock);
                currentBlock.setY(currentBlock.getY() + 1);
                if (!canMove(currentBlock.getShape(),currentBlock.getX(),currentBlock.getY())) {
                    currentBlock.setY(currentBlock.getY() - 2);
                    break;
                }
                placeBlock(currentBlock);
            }
        }
    }

    public void clearFullRows() {
        for (int row = 0; row < rows; row++) {
            boolean isFull = true;
            for (int col = 0; col < cols; col++) {
                if (matrix[row][col] == 0) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                clearRow(row);
                shiftDown(row);
                SoundManager.playClearRowSound();
                score += 10;
            }
        }
    }
    public void moveBlock(int dx, int dy) {
        if (currentBlock != null) {
            clearBlock(currentBlock);
            currentBlock.setX(currentBlock.getX() + dx);
            currentBlock.setY(currentBlock.getY() + dy);
            updateGhostBlockPosition();
            if (!canMove(currentBlock.getShape(),currentBlock.getX(),currentBlock.getY())) {
                currentBlock.setX(currentBlock.getX() - dx);
                currentBlock.setY(currentBlock.getY() - dy);
                updateGhostBlockPosition();
                if (dy == 1) {
                    placeBlock(currentBlock);
                    score += 5;
                    clearFullRows();
                    spawnNewBlock();
                }
            }
            placeBlock(currentBlock);
        }
    }
    public synchronized void rotateBlock() {
        if (currentBlock != null ) {
            clearBlock(currentBlock);
            currentBlock.rotate(this);
            updateGhostBlockPosition();
            placeBlock(currentBlock);
        }
    }
    public boolean canMove(int[][] shape, int x, int y) {
        if (shape == null)
            return false;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int newX = x + col;
                    int newY = y + row;
                    if (newX < 0 || newX >= cols || newY < 0 || newY >= rows || matrix[newY][newX] != 0)
                        return false;
                }
            }
        }
        return true;
    }
    public Block getNextBlock() {
        return nextBlock;
    }

    public int getScore() {
        return score;
    }

    public String toString() {
        boolean hundred = false;
        StringBuilder str = new StringBuilder("The matrix is:");
        String blue = "\u001B[34m";
        String reset = "\u001B[0m";
        str.append("\n");
        for (int row = 0; row < rows; row++) {
            str.append(row).append(":");
            if (row < 10)
                str.append(" ");
            str.append("[");
            for (int col = 0; col < cols; col++) {
                if (matrix[row][col] > 0)
                    str.append(blue).append(matrix[row][col] % 100).append(reset);
                    // In the game the id can go up to 100 and more, but it doesn't look good when printing it
                    // so that's why I use -> % 100.
                    // if the zero is blue it means that the real id can be 100 or even 1000 or even higher, but not zero.
                else
                    str.append(0);
                if (col < cols - 1)
                    str.append(",");
                if (matrix[row][col] % 100 < 10)
                    str.append(" ");
                if (matrix[row][col] > 99)
                    hundred = true;
            }
            str.append("]\n");
        }
        str.append("   |");
        for (int col = 0; col < cols; col++) {
            str.append(col);
            if (col < 10)
                str.append(" ");
            str.append("|");
        }
        if (hundred)
            str.append("\nThe current block id is over a hundred!");
        if (Game.isFINISHED())
            str.append("\nGAME OVER!");
        return str.toString();
    }
}