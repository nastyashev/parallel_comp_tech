public class SymbolThread implements Runnable {
    String character;

    public SymbolThread(String character) {
        this.character = character;
    }

    @Override
    public void run() {
        try {
            System.out.print(character);
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
