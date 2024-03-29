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
    private boolean isInPocket = false;

    public Ball (Component c){
        this.canvas = c;

        x = new Random().nextInt(this.canvas.getWidth());
        y = new Random().nextInt(this.canvas.getHeight());
    }

    public void draw (Graphics2D g2){
        g2.setColor(Color.darkGray);
        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
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

        for (int i = 0; i < ((BallCanvas)this.canvas).pockets.size(); i++){
            Pocket p = ((BallCanvas)this.canvas).pockets.get(i);
            if (x >= p.getX() && x <= p.getX() + p.getWidth() && y >= p.getY() && y <= p.getY() + p.getHeight()){
                this.isInPocket = true;
            }
        }

        this.canvas.repaint();
    }

    public boolean isInPocket(){
        return this.isInPocket;
    }

    public void delete(){
        ((BallCanvas)this.canvas).removePocketedBalls();
    }
}
