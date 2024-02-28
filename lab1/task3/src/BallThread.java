public class BallThread extends Thread {
    private Ball b;

    public BallThread (Ball ball) {
        b = ball;
    }

    @Override
    public void run(){
        try{
            while (!Thread.currentThread().isInterrupted()) {
                b.move();
                //System.out.println("Thread name = " + Thread.currentThread().getName());
                Thread.sleep(5);

            }
        } catch (InterruptedException ex){
            System.out.println("Thread was interrupted");
        }
    }
}
