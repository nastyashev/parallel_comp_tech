import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Pocket {
    private int x;
    private int y;
    private int width;
    private int height;

    public Pocket(int x, int y, int width, int height, Graphics2D g2){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.draw(g2);
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public void draw (Graphics2D g2){
        g2.setColor(Color.black);
        g2.fill(new Ellipse2D.Double(x, y, width, height));
    }
}
