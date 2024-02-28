import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BallCanvas extends JPanel {
    private ArrayList<Ball> blueBalls = new ArrayList<>();
    private ArrayList<Ball> redBalls = new ArrayList<>();

    public void addBlue (Ball b){
        this.blueBalls.add(b);
    }
    public void addRed (Ball b){
        this.redBalls.add(b);
    }

    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        for (int i = 0; i < blueBalls.size(); i++){
            Ball b = blueBalls.get(i);
            b.draw(g2, Color.BLUE);
        }

        for (int i = 0; i < redBalls.size(); i++){
            Ball b = redBalls.get(i);
            b.draw(g2, Color.RED);
        }
    }
}
