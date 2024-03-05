public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Asynchronous threads:");
        Thread t1 = new Thread(new SymbolThread1("|"));
        Thread t2 = new Thread(new SymbolThread1("-"));
        t1.start();
        t2.start();

        Thread.sleep(100);

        System.out.println("\nSynchronous threads:");
        Synchronize synchronize = new Synchronize();
        SymbolThread2 st1 = new SymbolThread2("|", true, synchronize);
        SymbolThread2 st2 = new SymbolThread2("-", false, synchronize);
        Thread t3 = new Thread(st1);
        Thread t4 = new Thread(st2);
        t3.start();
        t4.start();
    }
}