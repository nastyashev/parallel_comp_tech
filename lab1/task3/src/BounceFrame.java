import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;
    public static final int BLUE_BALLS_COUNT = 50;
    public static final int RED_BALLS_COUNT = 200;

    public BounceFrame () {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce programm");
        this.canvas = new BallCanvas();
        //System.out.println("In Frame Thread name = " + Thread.currentThread().getName());
        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        buttonStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < BLUE_BALLS_COUNT; i++){
                    Ball b = new Ball(canvas);
                    canvas.addBlue(b);

                    BallThread thread = new BallThread(b);
                    thread.start();
                    thread.setPriority(Thread.MIN_PRIORITY);
                    //System.out.println("Thread name = " + thread.getName());
                }

                for (int i = 0; i < RED_BALLS_COUNT; i++){
                    Ball b = new Ball(canvas);
                    canvas.addRed(b);

                    BallThread thread = new BallThread(b);
                    thread.start();
                    thread.setPriority(Thread.MAX_PRIORITY);
                    //System.out.println("Thread name = " + thread.getName());
                }
            }
        });
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}
