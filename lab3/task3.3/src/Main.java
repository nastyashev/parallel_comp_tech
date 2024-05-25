import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Journal journal = new Journal(new HashMap<>() {{
            put("K-1", 10);
            put("K-2", 15);
            put("K-3", 5);
        }});

        Teacher teacher1 = new Teacher(journal, List.of("K-1", "K-2", "K-3"), "Lecturer");
        Teacher teacher2 = new Teacher(journal, List.of("K-1"), "Assistant 1");
        Teacher teacher3 = new Teacher(journal, List.of("K-2"), "Assistant 2");
        Teacher teacher4 = new Teacher(journal, List.of("K-3"), "Assistant 3");

        Thread thread1 = new Thread(teacher1);
        Thread thread2 = new Thread(teacher2);
        Thread thread3 = new Thread(teacher3);
        Thread thread4 = new Thread(teacher4);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
        }

        journal.printMarks();
    }
}