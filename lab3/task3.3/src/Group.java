import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Group {
    private HashMap<String, List<Integer>> studentList;
    private final int MAX_STUDENTS;

    public Group(int maxStudents) {
        this.MAX_STUDENTS = maxStudents;
        generateStudentList();
    }

    private void generateStudentList() {
        studentList = new HashMap<>();
        for (int i = 0; i < MAX_STUDENTS; i++) {
            List<Integer> marks = new ArrayList<>();
            studentList.put("Студент " + i, marks);
        }
    }

    public synchronized void addMark(String student, int mark) {
        List<Integer> studentMarks = studentList.get(student);
        if (studentMarks == null) {
            return;
        }

        studentMarks.add(mark);
    }

    public List<String> getStudents() {
        return studentList.keySet().stream().toList();
    }

    public List<Integer> getMarks(String student) {
        return studentList.get(student);
    }
}
