import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Wall Class
 * 
 * @author wzy
 * 
 */
public class Wall {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;

    /**
     * first constructor
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width wall-width
     * @param height wall-height
     */
    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = new Color(0xf1, 0x5a, 0x22);
    }

    /**
     * second constructor
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width wall-width
     * @param height wall-height
     * @param c wall-color
     */
    public Wall(int x, int y, int width, int height, Color c) {
        this(x, y, width, height);
        this.color = c;
    }

    /**
     * draw the wall for the tank war
     * 
     * @param g the graphics for draw
     */
    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(this.color);
        g.fillRect(this.x, this.y, this.width, this.height);
        g.setColor(c);
    }

    /**
     * generate rectangle for collision detect
     * 
     * @return the rectangle for collision detect
     */
    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }
}
