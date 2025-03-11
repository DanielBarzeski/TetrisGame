import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Picture {
    public static final BufferedImage
            BLOCK = getBufImage("block.png"),
            TETRIS_LOGO = getBufImage("tetrisLogo.png"),
            GAME_OVER = getBufImage("gameOver.png");
    public static final BufferedImage[] BLOCKS = new BufferedImage[]{
            blockWithColor(Color.cyan),
            blockWithColor(Color.yellow),
            blockWithColor(Color.magenta),
            blockWithColor(Color.green),
            blockWithColor(Color.red),
            blockWithColor(Color.blue),
            blockWithColor(Color.orange),
            blockWithColor(Color.white)
    };

    private static BufferedImage getBufImage(String pathname) {
        try {
            return ImageIO.read(new File("images/" + pathname));
        } catch (IOException e) {
            return null;
        }
    }

    private static BufferedImage blockWithColor(Color colorFactor) {
        if (Picture.BLOCK == null)
            return null;
        int width = Picture.BLOCK.getWidth();
        int height = Picture.BLOCK.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, Picture.BLOCK.getType());
        Graphics2D g = newImage.createGraphics();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = Picture.BLOCK.getRGB(x, y);
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

}
