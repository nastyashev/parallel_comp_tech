public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.decrement();
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Count: " + counter.getCount());

        counter.setCount(0);

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.synchronizedIncrementMethod();
            }
        });
        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.synchronizedDecrementMethod();
            }
        });
        t3.start();
        t4.start();
        try {
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Count (synchronised method): " + counter.getCount());

        counter.setCount(0);

        Thread t5 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.synchronizedIncrementBlock();
            }
        });
        Thread t6 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.synchronizedDecrementBlock();
            }
        });
        t5.start();
        t6.start();
        try {
            t5.join();
            t6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Count (synchronised block): " + counter.getCount());

        counter.setCount(0);

        Thread t7 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.synchronizedIncrementLock();
            }
        });
        Thread t8 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.synchronizedDecrementLock();
            }
        });
        t7.start();
        t8.start();
        try {
            t7.join();
            t8.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Count (blocking an object): " + counter.getCount());
    }
}