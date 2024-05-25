
import java.util.concurrent.locks.ReentrantLock;

class Bank {
    public static final int NTEST = 10000;
    private final int[] accounts;
    private long ntransacts = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public Bank(int n, int initialBalance){
        accounts = new int[n];

        for (int i = 0; i < accounts.length; i++)
            accounts[i] = initialBalance;
        ntransacts = 0;
    }

    // public synchronized void transfer(int from, int to, int amount) {
    //     accounts[from] -= amount;
    //     accounts[to] += amount;
    //     ntransacts++;

    //     if (ntransacts % NTEST == 0)
    //         test();
    // }

    public void transfer(int from, int to, int amount) {
        synchronized (this) {
            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;
        }

        if (ntransacts % NTEST == 0) {
            test();
        }
    }

    // public void transfer(int from, int to, int amount) {
    //     lock.lock();
    //     try {
    //         accounts[from] -= amount;
    //         accounts[to] += amount;
    //         ntransacts++;
    //     } finally {
    //         lock.unlock();
    //     }

    //     if (ntransacts % NTEST == 0) {
    //         test();
    //     }
    // }

    public synchronized void test(){
        int sum = 0;

        for (int i = 0; i < accounts.length; i++)
            sum += accounts[i] ;

        System.out.println("Transactions:" + ntransacts + " Sum: " + sum);
    }

    public int size(){
        return accounts.length;
    }
}
