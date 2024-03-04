import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BallCanvas extends JPanel {
    private ArrayList<Ball> balls = new ArrayList<>();
    ArrayList<Pocket> pockets = new ArrayList<>();

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

    public synchronized void removePocketedBalls(){
        for (int i = 0; i < balls.size(); i++){
            Ball b = balls.get(i);
            if (b.isInPocket()){
                balls.remove(i);
            }
        }
    }
}
