import java.awt.Color;
import java.awt.Graphics;


public class Explode {    
    public static final int DIAMETER = 25;
    public static final int LIMIT = 200;
    
    private int x;
    private int y;
    private Color color;
    private TankClient tc;
    private int step;
    
    /**
     * default constructor
     * 
     * @param x coordinate x
     * @param y coordinate y
     * @param tc the reference of controller
     */
    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
        this.color = Color.ORANGE;
        this.step = 1;
    }

    /**
     * draw the explosion effect
     * 
     * @param g Graphics class
     */
    public void draw(Graphics g) {
        if (this.step * DIAMETER > LIMIT) {
            tc.explodes.remove(this);
            return;
        }
        
        Color c = g.getColor();
        g.setColor(this.color);
        g.fillOval(x, y, DIAMETER * this.step, DIAMETER * this.step);
        g.setColor(c);
        
        this.step ++;
    }
}
