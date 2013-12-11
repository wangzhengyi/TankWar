import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {
    public static final int MISSLE_WIDTH = 10;
    public static final int MISSLE_HEIGHT = 10;
    public static final int XSPEED = 2;
    public static final int YSPEED = 2;

    private int x;
    private int y;
    private Direction dir;
    private TankClient tc = null;
    private boolean live = true;
    private boolean role;
    
    public Missile(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile(int x, int y, Direction dir, boolean role, TankClient tc) {
        this(x, y, dir);
        this.tc = tc;
        this.role = role;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void draw(Graphics g) {
        if (! this.live) {
            tc.missiles.remove(this);
            return;
        }
        
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(this.x, this.y, MISSLE_WIDTH, MISSLE_HEIGHT);
        g.setColor(c);

        this.move();
    }

    public void overBoundary() {
        if (this.getX() < 0 || this.getX() > TankClient.GAME_WIDTH || this.getY() < 0 || this.getY() > TankClient.GAME_HEIGHT) {
            this.live = false;
        }
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
                this.x += XSPEED;
                break;
        }
        
        this.overBoundary();
    }

    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, MISSLE_WIDTH, MISSLE_HEIGHT);
    }

    public boolean hitTank(Tank t) {
        if (this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.role != t.getRole()) {
            this.live = false;
            t.setLive();
            Explode e = new Explode(this.x, this.y, this.tc);
            tc.explodes.add(e);
            return true;
        }
        
        return false;
    }
    
    public boolean hitTanks(List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i ++) {
            if (hitTank(tanks.get(i))) {
                return true;
            }
        }
        
        return false;
    }
}
