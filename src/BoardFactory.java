import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BoardFactory {
    private final int[][] matrix;
    private int score;
    protected final ArrayList<Block> blocks;
    private Block currentBlock, nextBlock;
    private Point ghostBlockPosition;

    public BoardFactory() {
        Block.setNextId();
        this.matrix = new int[Game.ROWS][Game.COLS];
        this.blocks = new ArrayList<>();
    }

    public void drawGame(Graphics g) {
        drawBoard(g);
        drawGhostBlock(g);
        drawShow(g);
        drawBlocks(g);
        drawDelete(g);
        g.setColor(Color.black);
        g.fillRect(1,1,Game.WIDTH+2,Game.CELL_SIZE*3);
        if (Game.isFINISHED())
            g.drawImage(Picture.GAME_OVER, Game.WIDTH / 2 - 50, Game.HEIGHT / 2 - 60, 100, 100,null);
    }

    private void drawOutLine(int row, int col, int blockId, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        if (row == 0 || matrix[row - 1][col] != blockId)  // Top edge
            g2d.drawLine(col * Game.CELL_SIZE + 2, row * Game.CELL_SIZE + 2, (col + 1) * Game.CELL_SIZE + 2, row * Game.CELL_SIZE + 2);
        if (row == Game.ROWS - 1 || matrix[row + 1][col] != blockId)  // Bottom edge
            g2d.drawLine(col * Game.CELL_SIZE + 2, (row + 1) * Game.CELL_SIZE + 2, (col + 1) * Game.CELL_SIZE + 2, (row + 1) * Game.CELL_SIZE + 2);
        if (col == 0 || matrix[row][col - 1] != blockId)  // Left edge
            g2d.drawLine(col * Game.CELL_SIZE + 2, row * Game.CELL_SIZE + 2, col * Game.CELL_SIZE + 2, (row + 1) * Game.CELL_SIZE + 2);
        if (col == Game.COLS - 1 || matrix[row][col + 1] != blockId)  // Right edge
            g2d.drawLine((col + 1) * Game.CELL_SIZE + 2, row * Game.CELL_SIZE + 2, (col + 1) * Game.CELL_SIZE + 2, (row + 1) * Game.CELL_SIZE + 2);
    }
    private void drawBoard(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(0, 0, Game.WIDTH + 3, Game.HEIGHT + 3);
        g.setColor(Color.gray);
        g.drawRect(1, 1, Game.WIDTH + 1, Game.HEIGHT + 1);

        for (int row = 0; row < Game.ROWS; row++) {
            for (int col = 0; col < Game.COLS; col++) {
                g.drawRect(col * Game.CELL_SIZE + 2, row * Game.CELL_SIZE + 2, Game.CELL_SIZE - 1, Game.CELL_SIZE - 1);
            }
        }
    }
    private void drawShow(Graphics g) {
        g.setColor(Color.white);
        if (Game.isSHOWED() && currentBlock != null) {
            for (int i = 0; i < currentBlock.getShape().length; i++) {
                for (int j = 0; j < currentBlock.getShape()[0].length; j++) {
                    g.drawRoundRect((currentBlock.getX() + i) * Game.CELL_SIZE + 2, (currentBlock.getY() + j) * Game.CELL_SIZE + 2, Game.CELL_SIZE, Game.CELL_SIZE, 10, 10);
                }
            }
        }
    }
    private void drawBlocks(Graphics g) {
        for (int row = 0; row < Game.ROWS; row++) {
            for (int col = 0; col < Game.COLS; col++) {
                int blockId = matrix[row][col];
                if (blockId != 0) {
                    Block block = getBlockById(blockId);
                    if (block != null) {
                        g.drawImage(block.getImage(), col * Game.CELL_SIZE + 2, row * Game.CELL_SIZE + 2, Game.CELL_SIZE, Game.CELL_SIZE, null);
                        drawOutLine(row, col, blockId, g);
                    }
                }
            }
        }
    }
    private void drawDelete(Graphics g){
        g.setColor(Color.red);
        for (int row = 0; row < Game.ROWS; row++) {
            boolean isRowFull = true;
            for (int col = 0; col < Game.COLS; col++) {
                if (matrix[row][col] == 0) {
                    isRowFull = false;
                    break;
                }
            }
            if (isRowFull) {
                g.fillRect(1, row * Game.CELL_SIZE + 1, Game.COLS * Game.CELL_SIZE + 2, Game.CELL_SIZE + 2);
            }
        }
    }
    private void drawGhostBlock(Graphics g) {
        if (ghostBlockPosition != null) {
            g.setColor(new Color(255, 255, 255, 100));
            int[][] shape = currentBlock.getShape();
            int ghostX = ghostBlockPosition.x;
            int ghostY = ghostBlockPosition.y;
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] != 0)
                        g.fillRect((ghostX + col) * Game.CELL_SIZE + 2, (ghostY + row) * Game.CELL_SIZE + 2, Game.CELL_SIZE, Game.CELL_SIZE);
                }
            }
        }
    }
    public void drawMenu(Graphics g) {
        g.setColor(Color.black);
        g.fillRoundRect(10,70,130,80,10,10);
        g.fillRoundRect(10,160,130,50,10,10);
        g.fillRoundRect(10,215,160,180,10,10);
        g.setColor(Color.white);
        g.drawString("Next block: ", 20,90);
        g.drawString("Score: "+score, 20,190);
        g.drawString("TETRIS GAME: " ,20,230);
        g.drawString("Controls: " ,20,250);
        g.drawString("Move Left: Arrow Left" ,20,265);
        g.drawString("Move Right: Arrow Right" ,20,280);
        g.drawString("Rotate Block: Arrow Up" ,20,295);
        g.drawString("Drop Block: Arrow Down" ,20,310);
        g.drawString("Soft Drop: Space bar" ,20,325);
        g.drawString("clearing row = 10 points" ,20,345);
        g.drawString("block falls = 5 points" ,20,360);
        g.drawString("Good Luck and Have Fun!",20,375);
        drawBlock(nextBlock,nextBlock.getImage(),35,100,20,20,g);
    }
    public void drawBlock(Block block, BufferedImage image, int x, int y, int width, int height, Graphics g) {
        int[][] shape = block.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0)
                    g.drawImage(image, x + col * width, y + row * height,width,height, null);
            }
        }
    }

    private synchronized Block getBlockById(int id) {
        return blocks.stream().filter(block -> block.getId() == id).findFirst().orElse(null);
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getScore() {
        return score;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public void setCurrentBlock(Block currentBlock) {
        this.currentBlock = currentBlock;
    }

    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    public void setGhostBlockPosition(Point ghostBlockPosition) {
        this.ghostBlockPosition = ghostBlockPosition;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
