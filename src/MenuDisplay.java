import javax.swing.*;
import java.awt.*;

public class MenuDisplay extends JPanel {
    private boolean started;
    private String level = "1";
    private final int buttonsSize = 40;
    public MenuDisplay() {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(Game.getWIDTH(),Game.getHEIGHT()));
        setBackground(Color.blue.brighter().brighter().brighter());
        SpecialButton pause = new SpecialButton();
        pause.set("File/continue.png",buttonsSize,buttonsSize);
        pause.addActionListener(e->{
            if (Game.isPAUSED())
                Game.CONTINUE();
            else
                Game.PAUSE();
        });

        SpecialButton start = getStartButton(pause);
        add(start);

        new Timer(100,e->{
            if (Game.isPAUSED()) pause.set("File/pause.png", buttonsSize,buttonsSize);
            else pause.set("File/continue.png", buttonsSize,buttonsSize);
            repaint();
            revalidate();
        }).start();
    }

    private SpecialButton getStartButton(SpecialButton pause) {
        Panel scorePanel = getScorePanel();

        Panel blockPanel = getBlockPanel();

        SpecialButton restart = new SpecialButton();
        restart.set("File/restart.png",buttonsSize,buttonsSize);
        restart.addActionListener(e-> Game.setRESTARTING(true));

        SpecialButton show = new SpecialButton();
        show.set("File/settings.png",buttonsSize,buttonsSize);
        show.addActionListener(e-> Game.setSHOWED(!Game.isSHOWED()));

        SpecialButton music = new SpecialButton();
        music.set("File/music.png",buttonsSize,buttonsSize);
        music.addActionListener(e-> Game.setMusicPlayed(!Game.isMusicPlayed()));

        SpecialButton start = new SpecialButton();
        start.set("File/start.png",80,60);
        start.addActionListener(e->{
            remove(start);
            started = true;
            add(blockPanel);
            add(scorePanel);
            add(show);
            add(music);
            add(restart);
            add(pause);
            Game.START();
            SoundManager.rewindBackgroundMusic();
            if (Game.isMusicPlayed()) {
                SoundManager.playBackgroundMusic();
            }
            revalidate();
            repaint();
        });
        return start;
    }

    private Panel getScorePanel(){
        final boolean[][] once = {new boolean[0]};
        Panel scorePanel = new Panel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int score = Game.getCONTROL().getScore();
                int highScore = FileManager.readNumberFromFile("File/Score.txt");
                g.setColor(Color.white);
                g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
                g.drawString("level: "+level,3,24);
                if (Game.getCONTROL().getScore() == 0)
                    once[0] = new boolean[]{true,true,true,true,true,true,true,true};
                boolean[] finalOnce = once[0];
                if (finalOnce[0] && score < 100) {
                    level = "1";
                    finalOnce[0] = false;
                }
                else if (finalOnce[1] && score >= 100 && score < 250 ){
                    level = "2";
                    Game.PAUSE();
                    Game.getANIMATOR().setDelay(450);
                    Game.CONTINUE();
                    finalOnce[1] = false;
                }
                else if (finalOnce[2] && score >= 250 && score < 500){
                    level = "3";
                    Game.PAUSE();
                    Game.getANIMATOR().setDelay(400);
                    Game.CONTINUE();
                    finalOnce[2] = false;
                }
                else if (finalOnce[3] && score >= 500 && score < 750){
                    level = "4";
                    Game.PAUSE();
                    Game.getANIMATOR().setDelay(350);
                    Game.CONTINUE();
                    finalOnce[3] = false;
                }
                else if (finalOnce[4] && score >= 750 && score < 1000){
                    level = "5";
                    Game.PAUSE();
                    Game.getANIMATOR().setDelay(300);
                    Game.CONTINUE();
                    finalOnce[4] = false;
                }
                else if (finalOnce[5] && score >= 1000 &&  score < 1250){
                    level = "6";
                    Game.PAUSE();
                    Game.getANIMATOR().setDelay(250);
                    Game.CONTINUE();
                    finalOnce[5] = false;
                }
                else if (finalOnce[6] && score >= 1250){
                    level = "MAX";
                    Game.PAUSE();
                    Game.getANIMATOR().setDelay(200);
                    Game.CONTINUE();
                    finalOnce[6] = false;
                }
                g.drawString("score: "+score,3,39);
                if (score > highScore) {
                    highScore = score;
                    FileManager.writeNumberToFile("File/Score.txt", highScore);
                    if (finalOnce[7]) {
                        SoundManager.showGifWithSound("File/highScore.gif", "File/success.wav");
                        finalOnce[7] = false;
                    }
                }
                g.drawString("high score: "+highScore,3,54);
            }
        };
        scorePanel.setBackground(Color.black);
        scorePanel.setPreferredSize(new Dimension(100,70));
        return scorePanel;
    }
    private Panel getBlockPanel(){
        Panel blockPanel = new Panel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.white);
                g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
                g.drawString("next block:",15,20);
                Block block = Game.getCONTROL().getNextBlock();
                Game.getCONTROL().drawBlock(block, Block.resizeImage(block.getImage(), 10, 10), 30, 30, 10, 10, g);
            }
        };
        blockPanel.setBackground(Color.black);
        blockPanel.setPreferredSize(new Dimension(90,70));
        return blockPanel;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (started) {
            g.setColor(Color.WHITE);
            int y = 200;
            for (String line : FileManager.readFileContent("File/instructions.txt").split("\n")) {
                g.drawString(line, 10, y);
                y += 15;
            }
        }
    }
}
