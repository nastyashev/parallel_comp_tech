public class BallThread extends Thread {
    private Ball b;
    private static final int STEPS = 200;

    public BallThread (Ball ball){
        b = ball;
    }

    @Override
    public void run(){
        try{
            for(int i = 1; i < STEPS; i++){
                b.move();
                System.out.println("Thread name = " + Thread.currentThread().getName());
                Thread.sleep(5);

            }
        } catch (InterruptedException ex){
            System.out.println("Thread was interrupted");
        }
    }
}
