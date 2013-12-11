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
import java.util.Random;

public class TankClient extends Frame {
    public static final int GAME_X = 400;
    public static final int GAME_Y = 300;
    public static final int GAME_HEIGHT = 600;
    public static final int GAME_WIDTH = 800;

    private Image offScreenImage = null;
    public Tank myTank = new Tank(50, 50, true, Direction.STOP, this);
    public List<Missile> missiles = new ArrayList<Missile>();
    public List<Explode> explodes = new ArrayList<Explode>();
    public List<Tank> tanks = new ArrayList<Tank>();


    public void update(Graphics g) {
        if (this.offScreenImage == null) {
            this.offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }

        Graphics gimage = offScreenImage.getGraphics();
        Color c = gimage.getColor();
        gimage.setColor(Color.BLACK);
        gimage.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gimage.setColor(c);

        paint(gimage);
        g.drawImage(this.offScreenImage, 0, 0, null);
    }

    public void paint(Graphics g) {
        // 记录
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("Missiles Count: " + this.missiles.size(), 10, 50);
        g.drawString("Explodes Count: " + this.explodes.size(), 10, 70);
        g.drawString("Tanks Count: " + this.tanks.size(), 10, 90);
        g.setColor(c);
        
        // 子弹
        for (int i = 0; i < this.missiles.size(); i++) {
            Missile m = this.missiles.get(i);
            m.hitTanks(this.tanks);
            m.hitTank(myTank);
            m.draw(g);          
        }

        // 爆炸
        for (int i = 0; i < this.explodes.size(); i++) {
            Explode ex = this.explodes.get(i);
            ex.draw(g);
        }

        myTank.draw(g);

        for (int i = 0; i < this.tanks.size(); i ++) {
            Tank enemyTank = this.tanks.get(i);
            enemyTank.draw(g);
        }
    }

    /**
     * 主功能函数
     */
    public void launchFrame() {
        Random r = new Random();
        for (int i = 0; i < 10; i ++) {
            int k = r.nextInt();
            if (k < 0) k *= -1;
            Tank enemyTank = new Tank((k + i) % GAME_X, (k + i) % GAME_Y, false, Direction.D, this);
            tanks.add(enemyTank);
        }
        
        this.setTitle("TankWar");
        this.setBackground(Color.BLACK);
        this.setLocation(GAME_X, GAME_Y);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.addKeyListener(new KeyMonitor());
        this.setResizable(false);
        this.setVisible(true);

        Thread tankthread = new Thread(new PaintThread());
        tankthread.start();
    }

    /**
     * Paint线程类
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
     * 键盘驱动监控内部类
     * 
     * @author wzy
     * 
     */
    private class KeyMonitor extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }
}
