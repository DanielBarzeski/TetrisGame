import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Window extends JFrame {
    public Window() {
        setTitle("TETRIS GAME");
        setIconImage(Picture.TETRIS_LOGO);
        getContentPane().setBackground(Color.blue);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel display = new JPanel();
        display.setBackground(Color.orange);
        display.setPreferredSize(new Dimension(Game.WIDTH+220,Math.max(Game.HEIGHT+15,410)));
        display.setLayout(new FlowLayout());
        Game.START();
        display.add(new GameDisplay());
        display.add(new MenuDisplay(204, 400));
        add(display);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
