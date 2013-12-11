import java.awt.Color;
import java.awt.Graphics;


public class Explode {
    private int[] diameter = {10, 15, 20, 25, 30, 35, 49, 35, 30, 25};
    private int x, y;
    private boolean live = true;
    private int step = 0;
    private TankClient tc = null;
    
    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }
    
    public void draw(Graphics g) {
        if (! this.live) {
            tc.explodes.remove(this);
            return;
        }
        
        if (this.step == diameter.length) {
            this.live = false;
            this.step = 0;
            return;
        }
        
        Color c = g.getColor();
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, diameter[step], diameter[step]);
        g.setColor(c);
        this.step ++;
    }
}
