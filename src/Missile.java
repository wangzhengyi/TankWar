import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {
    public static final int WIDTH = 10, HEIGHT = 10;
    public static final int SPEED = 7;
    public static final int KILL = 10;

    private int x;
    private int y;
    private Direction dir;
    private TankClient tc;
    private boolean live = true;
    private boolean role;
    private Color color;

    /**
     * default constructor
     * 
     * @param x coordinate x
     * @param y coordinate y
     * @param dir missile direction
     * @param role missile role
     * @param tc reference of controller
     */
    public Missile(int x, int y, Direction dir, boolean role, TankClient tc) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.tc = tc;
        this.role = role;
        this.color = this.role ? new Color(0xff, 0xce, 0x0e) : new Color(0x9d, 0x90, 0x87);
    }

    /**
     * draw missile
     * 
     * @param g graphics class
     */
    public void draw(Graphics g) {
        if (!this.live) {
            tc.missiles.remove(this);
            return;
        }

        Color c = g.getColor();
        g.setColor(this.color);
        g.fillOval(this.x, this.y, WIDTH, HEIGHT);
        g.setColor(c);

        this.move();
    }

    /**
     * cross boundary check
     */
    public void crossCheck() {
        if (this.x < 0 || this.x > TankClient.WIDTH || this.y < 0 || this.y > TankClient.HEIGHT) {
            this.live = false;
        }
    }

    /**
     * missile move
     */
    public void move() {
        switch (this.dir) {
            case U:
                this.y -= SPEED;
                break;
            case LU:
                this.x -= SPEED;
                this.y -= SPEED;
                break;
            case L:
                this.x -= SPEED;
                break;
            case LD:
                this.x -= SPEED;
                this.y += SPEED;
                break;
            case D:
                this.y += SPEED;
                break;
            case RD:
                this.x += SPEED;
                this.y += SPEED;
                break;
            case R:
                this.x += SPEED;
                break;
            case RU:
                this.x += SPEED;
                this.y -= SPEED;
                break;
            default:
                this.x += SPEED;
                break;
        }

        this.crossCheck();
    }

    /**
     * get rectangle of missile
     * 
     * @return rectangle
     */
    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, WIDTH, HEIGHT);
    }

    /**
     * hit tank
     * 
     * @param t tank
     * @return boolean
     */
    public boolean hitTank(Tank t) {
        if (this.live && this.getRect().intersects(t.getRect()) && t.isLive()
                && this.role != t.getRole()) {
            t.setLife();
            
            // life calculate
            if (t.getLife() <= 0) {
                t.setLive();
            }
            this.live = false;
            // explode effect
            Explode e = new Explode(this.x, this.y, this.tc);
            tc.explodes.add(e);
            return true;
        }

        return false;
    }


    /**
     * hit a lot of tanks
     * 
     * @param tanks
     * @return hit or not hit
     */
    public boolean hitTanks(List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            if (hitTank(tanks.get(i))) {
                return true;
            }
        }
        return false;
    }


    /**
     * hit against the wall
     * 
     * @param w the wall
     * @return hit or not hit
     */
    public boolean againstWall(Wall w) {
        if (this.getRect().intersects(w.getRect())) {
            this.live = false;
            return true;
        } else {
            return false;
        }
    }
}
