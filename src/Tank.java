import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;


public class Tank {
    /**
     * inner blood bar class
     * 
     * @author wzy
     * 
     */
    private class BloodBar {
        public void draw(Graphics g) {
            Color c = g.getColor();
            // background
            g.setColor(backBloodColor);
            g.drawRect(x, y - 10, WIDTH, 10);

            // foreground
            g.setColor(bloodColor);
            int width = role ? life * WIDTH / HEROBLOOD : life * WIDTH / ENEMYBLOOD;
            g.fillRect(x, y - 10, width, 10);

            g.setColor(c);
        }
    }

    public static final int SPEED = 5;
    public static final int WIDTH = 30, HEIGHT = 30;
    public static final int SCORE = 5;
    public static final int HEROBLOOD = 200;
    public static final int ENEMYBLOOD = 20;
    public static final int BLOODINCREASE = 50;

    public static Random r = new Random();
    private boolean role;
    private int x, y;
    private int oldX, oldY;
    private int life;
    private boolean bL = false, bU = false, bR = false, bD = false;
    private TankClient tc = null;
    private Direction dir;
    private Direction gunDir = Direction.D;
    private boolean live = true;
    private int step = r.nextInt(10) + 3;
    private Color tankColor;
    private Color bloodColor = new Color(0xed, 0x19, 0x41);
    private Color backBloodColor = new Color(0xff, 0xfe, 0xf9);
    private BloodBar bbar = new BloodBar();

    /**
     * default constructor
     * 
     * @param x coordinate x
     * @param y coordinate y
     * @param role tank role
     * @param dir tank direction
     * @param tc reference of controller
     */
    public Tank(int x, int y, boolean role, Direction dir, TankClient tc) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.role = role;
        this.dir = dir;
        this.tc = tc;
        this.life = this.role ? HEROBLOOD : ENEMYBLOOD;
        this.tankColor = this.role ? new Color(0xfc, 0xaf, 0x17) : new Color(0xf2, 0xea, 0xda);
    }

    /**
     * draw the tank
     * 
     * @param g
     */
    public void draw(Graphics g) {
        if (!this.live) {
            if (!this.role) {
                tc.enemyTanks.remove(this);
            } else {
                Color c = g.getColor();
                g.setColor(Color.MAGENTA);
                g.drawString("游戏结束，大侠请重新来过！", 100, 100);
                g.setColor(c);
            }
            return;
        }

        Color c = g.getColor();
        g.setColor(this.tankColor);
        g.fillOval(this.x, this.y, WIDTH, HEIGHT);
        g.setColor(Color.BLUE);

        switch (this.gunDir) {
            case U:
                g.drawLine(this.x + WIDTH / 2, this.y - HEIGHT / 2, this.x + WIDTH / 2, this.y
                        + HEIGHT / 2);
                break;
            case LU:
                g.drawLine(this.x - WIDTH / 2, this.y - HEIGHT / 2, this.x + WIDTH / 2, this.y
                        + HEIGHT / 2);
                break;
            case L:
                g.drawLine(this.x - WIDTH / 2, this.y + HEIGHT / 2, this.x + WIDTH / 2, this.y
                        + HEIGHT / 2);
                break;
            case LD:
                g.drawLine(this.x, this.y + HEIGHT + HEIGHT / 2, this.x + WIDTH / 2, this.y
                        + HEIGHT / 2);
                break;
            case D:
                g.drawLine(this.x + WIDTH / 2, this.y + HEIGHT + HEIGHT / 2, this.x + WIDTH / 2,
                        this.y + HEIGHT / 2);
                break;
            case RD:
                g.drawLine(this.x + WIDTH, this.y + HEIGHT, this.x + WIDTH / 2, this.y + HEIGHT / 2);
                break;
            case R:
                g.drawLine(this.x + WIDTH + WIDTH / 2, this.y + HEIGHT / 2, this.x + WIDTH / 2,
                        this.y + HEIGHT / 2);
                break;
            case RU:
                g.drawLine(this.x + WIDTH + WIDTH / 2, this.y - HEIGHT / 2, this.x + WIDTH / 2,
                        this.y + HEIGHT / 2);
                break;
            default:
                break;
        }

        g.setColor(c);

        bbar.draw(g);

        this.move();
    }


    /**
     * get tank role
     */
    public boolean getRole() {
        return this.role;
    }

    /**
     * set tank direction
     */
    public void setDirection() {
        Direction[] dirs = Direction.values();
        int k = r.nextInt(dirs.length);
        this.dir = dirs[k];
    }

    /**
     * recover the original tank location
     */
    private void recoverLoc() {
        this.x = this.oldX;
        this.y = this.oldY;
    }

    /**
     * tank move
     */
    public void move() {
        this.oldX = this.x;
        this.oldY = this.y;

        if (!this.role && this.step == 0) {
            this.step = r.nextInt(10) + 3;
            this.setDirection();
        } else {
            this.step -= 1;
        }

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
                break;
        }

        if (this.dir != Direction.STOP) {
            this.gunDir = this.dir;
        }

        this.preventCrossOver();

        if (!this.role && this.enemyAttack()) {
            this.fire();
        }
    }

    /**
     * prevent tank cross the boundary
     */
    public void preventCrossOver() {
        if (this.x < 0) {
            this.x = 0;
        }

        if (this.x + WIDTH > TankClient.WIDTH) {
            this.x = TankClient.WIDTH - WIDTH;
        }

        if (this.y < 0) {
            this.y = 0;
        }

        if (this.y + HEIGHT > TankClient.HEIGHT) {
            this.y = TankClient.HEIGHT - HEIGHT;
        }
    }

    /**
     * judge the direction, algorithm depend on Unix philosophy
     */
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
     * key press monitor
     * 
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
     * key release monitor
     * 
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_SPACE:
                this.fire();
                break;
            case KeyEvent.VK_CONTROL:
                this.superFire();
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
            case KeyEvent.VK_F2:
                this.live = true;
                this.life = HEROBLOOD;
                break;
        }

        this.locateDirection();
    }

    /**
     * tank attack
     */
    public void fire() {
        if (!this.live) {
            return;
        }
        int missileX = this.x + WIDTH / 2 - Missile.WIDTH / 2;
        int missileY = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile missile = new Missile(missileX, missileY, this.gunDir, this.role, this.tc);
        this.tc.missiles.add(missile);
    }

    /**
     * overload fire, fire with direction
     */
    public void fire(Direction dir) {
        if (!this.live) {
            return;
        }
        int missileX = this.x + WIDTH / 2 - Missile.WIDTH / 2;
        int missileY = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile missile = new Missile(missileX, missileY, dir, this.role, this.tc);
        this.tc.missiles.add(missile);
    }

    /**
     * super fire with press control
     */
    public void superFire() {
        Direction[] dirs = Direction.values();

        for (int i = 0; i < dirs.length - 1; i++) {
            this.fire(dirs[i]);
        }
    }

    /**
     * control the enemy tank fire
     * 
     * @return boolean
     */
    public boolean enemyAttack() {
        if (r.nextInt(1000) > 900) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * judge tank live
     * 
     * @return live
     */
    public boolean isLive() {
        return live;
    }

    /**
     * make tank die
     */
    public void setLive() {
        this.live = false;
    }

    /**
     * get rectangle of tank
     * 
     * @return rectangle
     */
    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, WIDTH, HEIGHT);
    }

    /**
     * against the wall
     * 
     * @param w
     * @return boolean
     */
    public boolean againstWall(Wall w) {
        if (this.getRect().intersects(w.getRect())) {
            this.recoverLoc();
            return true;
        } else {
            return false;
        }
    }

    public void setStep() {
        this.step = 0;
    }

    /**
     * against tanks
     * 
     * @param tanks
     * @return boolean
     */
    public boolean againstTanks(List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            if (this != t) {
                if (this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
                    this.recoverLoc();
                    t.recoverLoc();
                    this.setStep();

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * get tank life
     * 
     * @return life
     */
    public int getLife() {
        return life;
    }

    /**
     * decrease tank life
     */
    public void setLife() {
        this.life -= Missile.KILL;
    }

    /**
     * eat blood
     * 
     * @param blood
     */
    public void eatBlood(Blood blood) {
        if (this.role && this.live && blood.getLive() && this.getRect().intersects(blood.getRect())) {
            if (this.life + BLOODINCREASE > HEROBLOOD) {
                this.life = HEROBLOOD;
            } else {
                this.life += BLOODINCREASE;
            }
            blood.setLive();
        }
    }
}


