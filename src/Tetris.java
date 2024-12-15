import javax.swing.*;
import java.awt.*;
public class Tetris extends JFrame {
    public Tetris(){
        setTitle("TETRIS");
        setIconImage(FileManager.loadImageFromFile("File/TetrisLogo.png"));
        getContentPane().setBackground(Color.cyan);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Panel display = new Panel();
        display.setLayout(new FlowLayout(FlowLayout.LEFT, 10,10));
        display.setBackground(Color.yellow);
        Panel menu = new Panel();
        menu.setBackground(Color.black);
        menu.add(new MenuDisplay());
        Panel game = new Panel();
        game.setBackground(Color.black);
        game.add(new GameDisplay());
        display.add(game);
        display.add(menu);
        add(display);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
