import javax.swing.*;
import java.awt.*;

public class MenuDisplay extends JPanel {
    private final JButton pause, show;

    public MenuDisplay(int width, int height) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);
        pause = new JButton();
        pause.setBackground(Color.green);
        pause.setFocusable(false);
        pause.addActionListener(e -> {
            if (Game.isPAUSED())
                Game.CONTINUE();
            else
                Game.PAUSE();
        });
        show = new JButton("show");
        show.setBackground(Color.green);
        show.setFocusable(false);
        show.addActionListener(e -> {
            Game.setSHOW(!Game.isSHOWED());
        });
        JButton restart = new JButton("restart");
        restart.setBackground(Color.green);
        restart.setFocusable(false);
        restart.addActionListener(e -> {
            Game.START();
            add(pause);
            add(show);
            revalidate();
            repaint();
        });
        add(restart);
        add(pause);
        add(show);
        run();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        Game.CONTROL_BOARD().drawMenu(g);
    }

    public void run() {
        new Timer(100, e -> {
            if (Game.isFINISHED()) {
                remove(pause);
                remove(show);
            } else if (Game.isPAUSED()) {
                pause.setText("continue");
            } else {
                pause.setText("pause");
            }
            revalidate();
            repaint();
        }).start();
    }
}
