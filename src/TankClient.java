import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame {
    public static final int GAME_X = 400, GAME_Y = 100;
    public static final int HEIGHT = 600, WIDTH = 800;
    public static final int ENEMY_SIZE = 6;

    private Color backColor = new Color(0x65, 0xc2, 0x94);
    private Image offScreenImage = null;
    public Tank heroTank = new Tank(GAME_X + WIDTH - Tank.WIDTH, GAME_Y + HEIGHT - Tank.HEIGHT, true, Direction.STOP, this);
    public List<Missile> missiles = new ArrayList<Missile>();
    public List<Explode> explodes = new ArrayList<Explode>();
    public List<Tank> enemyTanks = new ArrayList<Tank>();
    public List<Wall> walls = new ArrayList<Wall>();
    public Blood blood = new Blood();


    /**
     * default constructor, generate walls
     */
    public TankClient() {
        Wall w1 = new Wall(300, 200, 10, 150);
        Wall w2 = new Wall(300, 200, 200, 15);
        this.walls.add(w1);
        this.walls.add(w2);
    }

    /**
     * generate enemy tanks
     */
    public void generateEnemys() {
        for (int i = 0; i < ENEMY_SIZE; i++) {
            int x, y;
            y = 50; 
            x = (i + 10) * Tank.WIDTH;      
            Tank enemyTank = new Tank(x, y, false, Direction.D, this);
            enemyTanks.add(enemyTank);
        }
    }
    
    /**
     * double-buffer applied to avert screen flicker and flash
     */
    public void update(Graphics g) {
        if (this.offScreenImage == null) {
            this.offScreenImage = this.createImage(WIDTH, HEIGHT);
        }

        Graphics goff = offScreenImage.getGraphics();
        Color c = goff.getColor();
        goff.setColor(this.backColor);
        goff.fillRect(0, 0, WIDTH, HEIGHT);
        goff.setColor(c);
        paint(goff);
        g.drawImage(this.offScreenImage, 0, 0, null);
    }

    public void paint(Graphics g) {
        // record
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("Missiles Count: " + this.missiles.size(), 10, 20);
        g.drawString("Explodes Count: " + this.explodes.size(), 10, 40);
        g.drawString("Tanks Count: " + this.enemyTanks.size(), 10, 60);
        g.drawString("HeroTank Life: " + this.heroTank.getLife(), 10, 80);
        g.setColor(c);

        // walls
        for (int i = 0; i < this.walls.size(); i++) {
            Wall w = this.walls.get(i);
            w.draw(g);
        }

        // my tank
        heroTank.draw(g);
        heroTank.eatBlood(blood);

        // enemy Tanks
        if (this.enemyTanks.size() <= 0) {
            this.generateEnemys();
        }
        for (int i = 0; i < this.enemyTanks.size(); i++) {
            Tank enemyTank = this.enemyTanks.get(i);
            enemyTank.againstTanks(this.enemyTanks);
            for (int j = 0; j < this.walls.size(); j++) {
                enemyTank.againstWall(this.walls.get(j));
            }
            enemyTank.draw(g);
        }

        // missiles
        for (int i = 0; i < this.missiles.size(); i++) {
            Missile m = this.missiles.get(i);
            m.hitTanks(this.enemyTanks);
            m.hitTank(heroTank);
            for (int j = 0; j < this.walls.size(); j++) {
                m.againstWall(this.walls.get(j));
            }
            m.draw(g);
        }

        // explodes
        for (int i = 0; i < this.explodes.size(); i++) {
            Explode ex = this.explodes.get(i);
            ex.draw(g);
        }
        
        // blood
        blood.draw(g);
    }

    /**
     * run frame
     */
    public void launchFrame() {
        // generate enemy enemyTanks
        this.generateEnemys();

        // set screen
        this.setTitle("TankWar");
        this.setBackground(this.backColor);
        this.setLocation(GAME_X, GAME_Y);
        this.setSize(WIDTH, HEIGHT);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.addKeyListener(new KeyMonitor());
        this.setResizable(false);
        this.setVisible(true);

        // slave start
        Thread tankthread = new Thread(new PaintThread());
        tankthread.start();
    }

    /**
     * repaint slave class
     * 
     * @author wzy
     * 
     */
    private class PaintThread implements Runnable {
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * keyboard monitor class
     * 
     * @author wzy
     * 
     */
    private class KeyMonitor extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
            heroTank.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            heroTank.keyPressed(e);
        }
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }
}
