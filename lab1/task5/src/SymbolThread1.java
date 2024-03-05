public class SymbolThread1 implements Runnable {
    private final String character;

    public SymbolThread1(String character) {
        this.character = character;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                System.out.print(character);
            }
            System.out.println();
        }

    }
}
