public class Synchronize {
    private boolean isRunning = true;
    private int count = 0;
    private boolean stop = false;

    public synchronized boolean getStop() {
        return stop;
    }

    public synchronized void waitThread(boolean isRunning, String character) {
        while (this.isRunning != isRunning) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.print(character);
        count++;
        this.isRunning = !isRunning;
        if (count%100 == 0) {
            System.out.println();
        }
        if (count + 1 == 10000) {
            stop = true;
        }
        notifyAll();
    }
}
