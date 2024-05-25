import java.util.List;

public class Teacher implements Runnable {
    private final Journal journal;
    private final List<String> groupCode;
    private String position;
    private final int weeks = 4;

    public Teacher(Journal journal, List<String> groupCode, String position) {
        this.journal = journal;
        this.groupCode = groupCode;
        this.position = position;
    }

    @Override
    public void run() {
        for (int i = 0; i < weeks; i++) {
            for (String code : groupCode) {
                for (String student : journal.getStudents(code)) {
                    journal.addMark(code, student, (int) (Math.random() * 100 + 1));
                }
            }
        }
    }
}
