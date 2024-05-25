import java.util.Random;

public class Producer implements Runnable {
    private final int SIZE = 5000;
    private Drop drop;

    public Producer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        int[] importantInfo = new int[SIZE];
        Random random = new Random();

        for (int i = 0; i < SIZE; i++) {
            importantInfo[i] = i + 1;
        }

        for (int i = 0; i < SIZE; i++) {
            drop.put(importantInfo[i]);
            try {
                Thread.sleep(random.nextInt(500));
            } catch (InterruptedException e) {
            }
        }
        drop.put(0);
    }
}
