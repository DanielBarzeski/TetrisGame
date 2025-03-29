import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameDisplay extends JPanel {

    public GameDisplay() {
        setPreferredSize(new Dimension(Game.WIDTH+4, Game.HEIGHT+4));
        setBackground(Color.darkGray);
        run();
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        Object[][] keyActions = {
                {"LEFT", "moveLeft", (Runnable) () -> Game.CONTROL_BOARD().moveBlock(-1, 0)},
                {"RIGHT", "moveRight", (Runnable) () -> Game.CONTROL_BOARD().moveBlock(1, 0)},
                {"DOWN", "moveDown", (Runnable) () -> Game.CONTROL_BOARD().moveBlock(0, 1)},
                {"UP", "rotate", (Runnable) () -> Game.CONTROL_BOARD().rotateBlock()},
                {"SPACE", "drop", (Runnable) () -> Game.CONTROL_BOARD().dropBlock()}
        };

        for (Object[] keyAction : keyActions) {
            String key = (String) keyAction[0];
            String actionName = (String) keyAction[1];
            Runnable action = (Runnable) keyAction[2];
            inputMap.put(KeyStroke.getKeyStroke(key), actionName);
            actionMap.put(actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!Game.isFINISHED() && !Game.isPAUSED()) {
                        action.run();
                        repaint();
                    }
                }
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game.CONTROL_BOARD().drawGame(g);
    }

    public void run() {
        new Timer(300, e -> {
            if (!Game.isFINISHED() && !Game.isPAUSED()) {
                Game.CONTROL_BOARD().moveBlock(0, 1);
                repaint();
                if (Game.isSHOWED())
                    System.out.println(Game.CONTROL_BOARD().toString());
            }
        }).start();
    }
}