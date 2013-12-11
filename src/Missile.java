import java.awt.Color;
import java.awt.Graphics;

public class Missile {
    public static final int MISSLE_WIDTH = 10;
    public static final int MISSLE_HEIGHT = 10;
    public static final int XSPEED = 10;
    public static final int YSPEED = 10;
    
    private int x;
    private int y;
    private Direction dir;
    private boolean live = true;
    private TankClient tc = null;

    public Missile(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
    
    public Missile(int x, int y, Direction dir, TankClient tc) {
        this(x, y, dir);
        this.tc = tc;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(this.x, this.y, MISSLE_WIDTH, MISSLE_HEIGHT);
        g.setColor(c);
        
        this.move();
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
    }
    
    public boolean isLive() {
        return live;
    }
}
