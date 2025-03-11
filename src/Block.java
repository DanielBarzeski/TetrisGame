import java.awt.image.BufferedImage;

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

    public static void setNextId() {
        nextId = 1;
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
        int[][][] IBlockKicks = {
                {{0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2}}, // 0 -> 1
                {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}}, // 1 -> 2
                {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}}, // 2 -> 3
                {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}}  // 3 -> 0
        };
        if (getShape()[0].length == 4) { // I SHAPE is in index 0
            return IBlockKicks;
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

class DataBlock {
    private final BufferedImage image;
    private int[][] shape;
    /// you can add and remove any shape:
    /// (make sure that if you add any matrix that the shape matrix is 3 x 3 or 2 x 2 or 1 x 1)
    private static final int[][][] SHAPES = {
            {   /// I shape
                    {0, 0, 0, 0},
                    {1, 1, 1, 1},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
            },
            {   /// O shape
                    {1, 1},
                    {1, 1}
            },
            {   /// T shape
                    {1, 1, 1},
                    {0, 1, 0},
                    {0, 0, 0}
            },
            {   /// S shape
                    {0, 1, 1},
                    {1, 1, 0},
                    {0, 0, 0}
            },
            {   /// Z shape
                    {1, 1, 0},
                    {0, 1, 1},
                    {0, 0, 0}
            },
            {   /// J shape
                    {1, 0, 0},
                    {1, 1, 1},
                    {0, 0, 0}
            },
            {   /// L shape
                    {0, 0, 1},
                    {1, 1, 1},
                    {0, 0, 0}
            }
    };

    protected DataBlock(int index) {
        if (index >= Picture.BLOCKS.length)
            index = Picture.BLOCKS.length - 1;
        image = Picture.BLOCKS[index];
        shape = SHAPES[index];
    }

    protected static int[][][] getSHAPES() {
        return SHAPES;
    }

    protected int[][] getShape() {
        return shape;
    }

    protected void setShape(int[][] shape) {
        this.shape = shape;
    }

    public BufferedImage getImage() {
        return image;
    }
}