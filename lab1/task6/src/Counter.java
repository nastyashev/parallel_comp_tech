public class Counter {
    private int count = 0;
    private Object lock = new Object();

    public void increment() {
        count++;
    }

    public void decrement() {
        count--;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void synchronizedIncrementLock() {
        synchronized (lock) {
            count++;
        }
    }

    public void synchronizedDecrementLock() {
        synchronized (lock) {
            count--;
        }
    }

    public void synchronizedIncrementBlock() {
        synchronized (this) {
            count++;
        }
    }

    public void synchronizedDecrementBlock() {
        synchronized (this) {
            count--;
        }
    }

    public synchronized void synchronizedIncrementMethod() {
        count++;
    }

    public synchronized void synchronizedDecrementMethod() {
        count--;
    }
}
