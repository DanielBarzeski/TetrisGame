import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;
public class Block extends DataBlock {
    private static int nextId;
    private final int id, index;
    private int x, y, rotationState;
    public Block(int shapeIndex, int x, int y) {
        super(shapeIndex);
        this.index = shapeIndex;
        this.id = nextId++;  // Assign unique id and increment for the next block
        this.rotationState = 0;
        this.x = x;
        this.y = y;
        replaceOnesWithId();  // Replace '1' in the shape with this block's id
    }
    public Block(Block other) {
        super(other.index);
        this.index = other.index;
        this.id = other.id;
        this.x = other.x;
        this.y = other.y;
        this.rotationState = other.rotationState;
        replaceOnesWithId();
    }
    private void replaceOnesWithId() {
        for (int row = 0; row < getShape().length; row++) {
            for (int col = 0; col < getShape()[row].length; col++) {
                if (getShape()[row][col] == 1) {
                    getShape()[row][col] = id;
                }
            }
        }
    }
    public static void setNextId(int newId) {
        nextId = newId;
    }

    private int[][] rotateMatrix(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] rotatedMatrix = new int[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rotatedMatrix[j][n - 1 - i] = matrix[i][j];
            }
        }
        return rotatedMatrix;
    }

    private int[][][] getKickOffsets() {
        int[][][] standardKicks = {
                {{0, 0}, {1, 0}, {-1, 0}, {0, -1}, {0, 1}},
                {{0, 0}, {-1, 0}, {1, 0}, {0, 1}, {0, -1}},
                {{0, 0}, {-1, 0}, {1, 0}, {0, 1}, {0, -1}},
                {{0, 0}, {1, 0}, {-1, 0}, {0, -1}, {0, 1}}
        };
        int[][][] iBlockKicks = {
                {{0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2}}, // 0 -> 1
                {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}}, // 1 -> 2
                {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}}, // 2 -> 3
                {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}}  // 3 -> 0
        };
        if (index == 0) { // I SHAPE is in index 0
            return iBlockKicks;
        }
        return standardKicks;
    }

    public synchronized void rotate(Board board) {
        int[][][] kicks = getKickOffsets();
        int[][] originalShape = getShape();
        int originalRotationState = rotationState;
        int originalX = x;
        int originalY = y;

        int[][] newShape = rotateMatrix(getShape());
        rotationState = (rotationState + 1) % 4;

        for (int[] kick : kicks[originalRotationState]) {
            x = originalX + kick[0];
            y = originalY + kick[1];

            if (board.canMove(newShape, x, y)) {
                setShape(newShape);
                return;
            }
        }
        setShape(originalShape);
        rotationState = originalRotationState;
        x = originalX;
        y = originalY;
    }
    public static Block createRandomBlock(int bound) {
        int shapeIndex = (int) (Math.random() * getSHAPES().length);
        int x = (shapeIndex == 0) ? (int) (Math.random() * (bound - 5)) : (int) (Math.random() * (bound - 4));
        return new Block(shapeIndex, x, 0);
    }
    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
class DataBlock{
    private final BufferedImage image;
    private int[][] shape;
    private static final Color[] COLORS = {Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.GREEN, Color.RED, Color.BLUE,Color.ORANGE, Color.WHITE};
    private static BufferedImage[] IMAGES = Arrays.stream(COLORS).map(item -> changeWhiteToColor(Objects.requireNonNull(loadImage()), item)).toArray(BufferedImage[]::new);
    // you can add and remove any shape:
    // (if you remove or add a shape make sure that the COLORS.length == IMAGES.length
    // by adding or removing a Color to the COLORS array)
    // (make sure that if you add any matrix that the matrix is 3 x 3 or 2 x 2)
    // (you cant change the "I shape" index and you cant remove him).
    private static final int[][][] SHAPES = {
            {   // I shape
                    {0, 0, 0, 0},
                    {1, 1, 1, 1},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
            },
            {   // O shape
                    {1, 1},
                    {1, 1}
            },
            {   // T shape
                    {1, 1, 1},
                    {0, 1, 0},
                    {0, 0, 0}
            },
            {   // S shape
                    {0, 1, 1},
                    {1, 1, 0},
                    {0, 0, 0}
            },
            {   // Z shape
                    {1, 1, 0},
                    {0, 1, 1},
                    {0, 0, 0}
            },
            {   // J shape
                    {1, 0, 0},
                    {1, 1, 1},
                    {0, 0, 0}
            },
            {   // L shape
                    {0, 0, 1},
                    {1, 1, 1},
                    {0, 0, 0}
            },
            {
                    {0, 1},
                    {0, 1},
            }
    };
    protected DataBlock(int index){
        image = IMAGES[index];
        shape = Arrays.stream(SHAPES[index]).map(int[]::clone).toArray(int[][]::new);
    }
    public static void UPDATE(int width, int height) {
        Block.setNextId(1); // must be higher than zero
        IMAGES = new BufferedImage[COLORS.length];
        IntStream.range(0, COLORS.length).forEach(i -> IMAGES[i] = changeWhiteToColor(resizeImage(Objects.requireNonNull(loadImage()), width, height), COLORS[i]));
    }
    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
    private static BufferedImage changeWhiteToColor(BufferedImage image, Color colorFactor) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        Graphics2D g = newImage.createGraphics();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                Color originalColor = new Color(rgb, true);
                int red = Math.min((originalColor.getRed() * colorFactor.getRed() / 255), 255);
                int green = Math.min((originalColor.getGreen() * colorFactor.getGreen() / 255), 255);
                int blue = Math.min((originalColor.getBlue() * colorFactor.getBlue() / 255), 255);
                Color newColor = new Color(red, green, blue, originalColor.getAlpha());
                newImage.setRGB(x, y, newColor.getRGB());
            }
        }
        g.dispose();
        return newImage;
    }
    private static BufferedImage loadImage(){
        try {
            return ImageIO.read(new File("File/block.png"));
        } catch (IOException ignored) {}
        return null;
    }
    protected static int[][][] getSHAPES() {
        return SHAPES;
    }
    protected BufferedImage getImage() {
        return image;
    }
    protected int[][] getShape() {
        return shape;
    }
    protected void setShape(int[][] shape) {
        this.shape = shape;
    }
}