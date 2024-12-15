import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
public class FileManager {
    public static int readNumberFromFile(String filePath) {
        int number = 0;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
            }
            scanner.close();
        } catch (FileNotFoundException ignored) {}
        return number;
    }
    public static void writeNumberToFile(String filePath, int newNumber) {
        try {
            PrintWriter writer = new PrintWriter(filePath);
            writer.println(newNumber);
            writer.close();
        } catch (FileNotFoundException ignored) {}
    }
    public static BufferedImage loadImageFromFile(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            return null;
        }
    }
    public static String readFileContent(String filePath) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException e) {
                content.append("Error reading file: ").append(e.getMessage());
            }
            return content.toString();
        }
}
