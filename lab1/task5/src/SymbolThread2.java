public class SymbolThread2 implements Runnable {
    private final String character;
    private final boolean isRunning;
    private final Synchronize synchronize;

    public SymbolThread2(String character, boolean isRunning, Synchronize synchronize) {
        this.character = character;
        this.isRunning = isRunning;
        this.synchronize = synchronize;
    }

    @Override
    public void run() {
        while (!synchronize.getStop()) {
            synchronize.waitThread(isRunning, character);
        }
    }
}
