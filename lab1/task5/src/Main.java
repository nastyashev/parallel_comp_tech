public class Main {
    public static void main(String[] args) {
        try {
            Thread thread1 = new Thread(new SymbolThread("-"));
            Thread thread2 = new Thread(new SymbolThread("|"));

            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    thread1.run();
                    thread2.run();
                    thread1.join();
                    thread2.join();
                }
                System.out.println();
            }
        }
        catch (InterruptedException e) {
            System.out.println("Tread was interrupted");
        }
    }
}