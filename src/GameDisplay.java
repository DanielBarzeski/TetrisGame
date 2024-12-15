import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameDisplay extends JPanel {
    public GameDisplay(){
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(Game.getWIDTH(),Game.getHEIGHT()));
        setBackground(Color.darkGray);
        Game.setRESTARTING(true);
        Game.setANIMATOR(500, e ->{
            if (!Game.isFINISHED() && !Game.isPAUSED())
                updateGame();
        });
        setupKeyBindings();
    }

    private void updateGame() {
        if (Game.isRESTARTING()) {
            Game.setRESTARTING(false);
        }
        Game.getCONTROL().moveBlock(0,1);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.darkGray);
        g.drawRect(0,0,Game.getWIDTH()-1,Game.getHEIGHT()-1);
        if (Game.getCONTROL() != null) {
            Game.getCONTROL().draw(g, 2, -2 * 20 + 2);
            if (Game.isSHOWED())
                System.out.println(Game.getCONTROL());
        }
        g.setColor(Color.darkGray);
        g.drawLine(0,0,Game.getWIDTH(),0);
    }
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        // Define key bindings with their corresponding actions
        Object[][] keyActions = {
                {"LEFT", "moveLeft", (Runnable) () -> Game.getCONTROL().moveBlock(-1, 0)},
                {"RIGHT", "moveRight", (Runnable) () -> Game.getCONTROL().moveBlock(1, 0)},
                {"DOWN", "moveDown", (Runnable) () -> Game.getCONTROL().moveBlock(0, 1)},
                {"UP", "rotate", (Runnable) () -> Game.getCONTROL().rotateBlock()},
                {"SPACE", "drop", (Runnable) () -> Game.getCONTROL().dropBlock()}
        };
        // Loop through and bind keys to actions
        for (Object[] keyAction : keyActions) {
            String key = (String) keyAction[0];
            String actionName = (String) keyAction[1];
            Runnable action = (Runnable) keyAction[2];
            inputMap.put(KeyStroke.getKeyStroke(key), actionName);
            actionMap.put(actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Check if the game is finished or paused
                    if (!Game.isFINISHED() && !Game.isPAUSED()) {
                        action.run();  // Run the associated action
                        repaint();      // Repaint the game display
                    }
                }
            });
        }
    }
}
