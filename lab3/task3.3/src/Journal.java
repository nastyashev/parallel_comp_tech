import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Journal {
    private HashMap<String, Group> groups;
    private final int GROUPS_COUNT;

    public Journal(HashMap<String, Integer> students) {
        this.GROUPS_COUNT = students.size();
        this.groups = new HashMap<>();
        
        for (Map.Entry<String, Integer> entry : students.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            this.groups.put(key, new Group(value));
        }
    }

    public void addMark(String groupCode, String student, int mark) {
        Group group = groups.get(groupCode);
        if (group == null) {
            return;
        }

        group.addMark(student, mark);
    }

    public List<String> getStudents(String groupCode) {
        Group group = groups.get(groupCode);
        if (group == null) {
            return null;
        }

        return group.getStudents();
    }

    public void printMarks() {
        for (Map.Entry<String, Group> entry : groups.entrySet()) {
            String group = entry.getKey();
            Group students = entry.getValue();
            System.out.println("\n\nГрупа " + group + ":");
            for (String student : students.getStudents()) {
                System.out.println(student + ": " + students.getMarks(student));
            }
        }
    }
}
