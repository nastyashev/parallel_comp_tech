import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;
    public static final int BALLS_COUNT = 100;

    public BounceFrame () {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce programm");
        this.canvas = new BallCanvas();
        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());
        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        JLabel labelScore = new JLabel(String.format("Score: %d", Score.getScore()));

        buttonStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < BALLS_COUNT; i++){
                    Ball b = new Ball(canvas);
                    canvas.add(b);

                    BallThread thread = new BallThread(b);
                    thread.start();
                    System.out.println("Thread name = " + thread.getName());
                }
            }
        });
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
            }
        });
        Score.setListener(new ScoreListener() {
            @Override
            public void actionPerformed() {
                labelScore.setText(String.format("Score: %d", Score.getScore()));
                labelScore.repaint();
            }
        });


        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        buttonPanel.add(labelScore);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}
