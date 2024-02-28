import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

class Ball {
    private Component canvas;
    private static final int XSIZE = 20;
    private static final int YSIZE = 20;
    private int x;
    private int y;
    private int dx = 2;
    private int dy = 2;

    public Ball (Component c){
        this.canvas = c;

        x = 0;
        y = 0;
    }

    public void draw (Graphics2D g2, Color color){
        g2.setColor(color);
        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
        canvas.repaint();
    }

    public void move (){
        x += dx;
        y += dy;
        if (x < 0){
            x = 0;
            dx = -dx;
        }
        if (x + XSIZE >= this.canvas.getWidth()){
            x = this.canvas.getWidth() - XSIZE;
            dx = -dx;
        }
        if (y < 0){
            y = 0;
            dy = -dy;
        }
        if (y + YSIZE >= this.canvas.getHeight()){
            y = this.canvas.getHeight() - YSIZE;
            dy = -dy;
        }
        this.canvas.repaint();
    }
}
