import java.awt.*;
import java.awt.event.KeyEvent;


public class Tank {
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;
    public static final int TANK_WIDTH = 30;
    public static final int TANK_HEIGHT = 30;

    private int x;
    private int y;
    private boolean bL = false, bU = false, bR = false, bD = false;
    private TankClient tc = null;
    private Direction dir = Direction.STOP;
    private Direction gunDir = Direction.D;

    /**
     * 构造函数1
     * 
     * @param x
     * @param y
     */
    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 构造函数2
     * 
     * @param x
     * @param y
     * @param tc
     */
    public Tank(int x, int y, TankClient tc) {
        this(x, y);
        this.tc = tc;
    }

    public void move() {
        switch (this.dir) {
            case U:
                this.y -= YSPEED;
                break;
            case LU:
                this.x -= XSPEED;
                this.y -= YSPEED;
                break;
            case L:
                this.x -= XSPEED;
                break;
            case LD:
                this.x -= XSPEED;
                this.y += YSPEED;
                break;
            case D:
                this.y += YSPEED;
                break;
            case RD:
                this.x += XSPEED;
                this.y += YSPEED;
                break;
            case R:
                this.x += XSPEED;
                break;
            case RU:
                this.x += XSPEED;
                this.y -= YSPEED;
                break;
            default:
                break;
        }

        if (this.dir != Direction.STOP) {
            this.gunDir = this.dir;
        }
        
        this.preventCrossOver();
    }

    
    public void preventCrossOver() {
        if (this.x < 0) {
            this.x = 0;
        }
        
        if (this.x + TANK_WIDTH > TankClient.GAME_WIDTH) {
            this.x = TankClient.GAME_WIDTH - TANK_WIDTH;
        }
        
        if (this.y < 0) {
            this.y = 0;
        }
        
        if (this.y + TANK_HEIGHT > TankClient.GAME_HEIGHT) {
            this.y = TankClient.GAME_HEIGHT - TANK_HEIGHT;
        }
    }
    
    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.YELLOW);
        g.fillOval(this.x, this.y, TANK_WIDTH, TANK_HEIGHT);
        g.setColor(Color.BLUE);

        switch (this.gunDir) {
            case U:
                g.drawLine(this.x + TANK_WIDTH / 2, this.y, this.x + TANK_WIDTH / 2,
                        this.y + TANK_HEIGHT / 2);
                break;
            case LU:
                g.drawLine(this.x, this.y, this.x + TANK_WIDTH / 2, this.y + TANK_HEIGHT
                        / 2);
                break;
            case L:
                g.drawLine(this.x, this.y + TANK_HEIGHT / 2, this.x + TANK_WIDTH / 2,
                        this.y + TANK_HEIGHT / 2);
                break;
            case LD:
                g.drawLine(this.x, this.y + TANK_HEIGHT, this.x + TANK_WIDTH / 2, this.y
                        + TANK_HEIGHT / 2);
                break;
            case D:
                g.drawLine(this.x + TANK_WIDTH / 2, this.y + TANK_HEIGHT, this.x
                        + TANK_WIDTH / 2, this.y + TANK_HEIGHT / 2);
                break;
            case RD:
                g.drawLine(this.x + TANK_WIDTH, this.y + TANK_HEIGHT, this.x
                        + TANK_WIDTH / 2, this.y + TANK_HEIGHT / 2);
                break;
            case R:
                g.drawLine(this.x + TANK_WIDTH, this.y + TANK_HEIGHT / 2, this.x
                        + TANK_WIDTH / 2, this.y + TANK_HEIGHT / 2);
                break;
            case RU:
                g.drawLine(this.x + TANK_WIDTH, this.y, this.x + TANK_WIDTH / 2, this.y
                        + TANK_HEIGHT / 2);
                break;
            default:
                break;
        }

        g.setColor(c);

        this.move();
    }

    public void locateDirection() {
        int a, b, c, d, total;

        a = this.bU ? 1 : 0;
        b = this.bD ? 2 : 0;
        c = this.bL ? 4 : 0;
        d = this.bR ? 7 : 0;

        total = a + b + c + d;

        switch (total) {
            case 1:
                this.dir = Direction.U;
                break;
            case 2:
                this.dir = Direction.D;
                break;
            case 4:
                this.dir = Direction.L;
                break;
            case 5:
                this.dir = Direction.LU;
                break;
            case 6:
                this.dir = Direction.LD;
                break;
            case 7:
                this.dir = Direction.R;
                break;
            case 8:
                this.dir = Direction.RU;
                break;
            case 9:
                this.dir = Direction.RD;
                break;
            default:
                this.dir = Direction.STOP;
                break;
        }
    }

    /**
     * 按键效果
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                this.bU = true;
                break;
            case KeyEvent.VK_DOWN:
                this.bD = true;
                break;
            case KeyEvent.VK_LEFT:
                this.bL = true;
                break;
            case KeyEvent.VK_RIGHT:
                this.bR = true;
                break;
        }

        this.locateDirection();
    }
    
    /**
     * 松键效果
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_SPACE:
                this.fire();
                break;
            case KeyEvent.VK_UP:
                this.bU = false;
                break;
            case KeyEvent.VK_DOWN:
                this.bD = false;
                break;
            case KeyEvent.VK_LEFT:
                this.bL = false;
                break;
            case KeyEvent.VK_RIGHT:
                this.bR = false;
                break;
        }

        this.locateDirection();
    }

    /**
     * 开火
     */
    public void fire() {
        int missileX = this.x + TANK_WIDTH / 2 - Missile.MISSLE_WIDTH / 2;
        int missileY = this.y + TANK_HEIGHT / 2 - Missile.MISSLE_HEIGHT / 2;
        Missile missile = new Missile(missileX, missileY, this.gunDir, this.tc);
        this.tc.missiles.add(missile);
    }
}
