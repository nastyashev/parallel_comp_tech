import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BallCanvas extends JPanel {
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Pocket> pockets = new ArrayList<>();

    public void add (Ball b){
        this.balls.add(b);
    }

    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        pockets.add(new Pocket(0, 0, 30, 30, g2));
        pockets.add(new Pocket(0, this.getHeight() - 30, 30, 30, g2));
        pockets.add(new Pocket(this.getWidth() - 30, 0, 30, 30, g2));
        pockets.add(new Pocket(this.getWidth() - 30, this.getHeight() - 30, 30, 30, g2));

        for (int i = 0; i < balls.size(); i++){
            Ball b = balls.get(i);
            b.draw(g2);
        }
    }

    public void checkPockets(){
        for (int i = 0; i < balls.size(); i++){
            Ball b = balls.get(i);
            for (int j = 0; j < pockets.size(); j++){
                Pocket p = pockets.get(j);
                if (b.getX() >= p.getX() && b.getX() <= p.getX() + p.getWidth() && b.getY() >= p.getY() && b.getY() <= p.getY() + p.getHeight()){
                    b.pocket();
                }
            }
        }
    }

    public void removePocketedBalls(){
        for (int i = 0; i < balls.size(); i++){
            Ball b = balls.get(i);
            if (b.isInPocket()){
                balls.remove(i);
            }
        }
    }
}
