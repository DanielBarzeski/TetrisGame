import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class SpecialButton extends JButton {
    private boolean round;
    public void set(String filename,int width,int height){
        ImageIcon icon = new ImageIcon(filename);
        Image scaledImage = icon.getImage().getScaledInstance(width,height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        setIcon(resizedIcon);
        setRound(true);
        setFocusable(false);
    }
    public boolean isRound(){
        return round;
    }
    public void setRound(boolean round){
        this.round = round;
        if (isRound()){
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!isRound())
            super.paintComponent(g);
        if (isRound()){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Icon icon = getIcon();
            if (icon != null) {
                int iconX = (getWidth() - icon.getIconWidth()) / 2;
                int iconY = (getHeight() - icon.getIconHeight()) / 2;
                icon.paintIcon(this, g2, iconX, iconY);
            } else {
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - fm.getDescent();
                g2.drawString(getText(), x, y);
            }

            g2.dispose();
        }
    }
    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
    }
    @Override
    public boolean contains(int x, int y) {
        if (isRound()){
            Ellipse2D shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
            return shape.contains(x, y);
        }
        return super.contains(x, y);
    }

}


