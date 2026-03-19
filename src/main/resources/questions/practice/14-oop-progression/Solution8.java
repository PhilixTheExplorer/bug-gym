import java.util.*;

public class Solution8 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String code = sc.next();
        int n = sc.nextInt();

        Classroom c = new Classroom(code);
        for (int i = 0; i < n; i++) {
            int id = sc.nextInt();
            String name = sc.next();
            c.enroll(new Student(id, name));
        }

        int dropId = sc.nextInt();
        c.drop(dropId);

        c.printRoster();
        System.out.println("Count " + c.count());
        sc.close();
    }
}

class Student {
    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class Classroom {
    private String roomCode;
    private ArrayList<Student> students;

    public Classroom(String roomCode) {
        this.roomCode = roomCode;
        this.students = new ArrayList<>();
    }

    public void enroll(Student s) {
        for (Student student : students) {
            if (student.getId() == s.getId()) {
                return;
            }
        }
        students.add(s);
    }

    public void drop(int id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                students.remove(i);
                return;
            }
        }
    }

    public int count() {
        return students.size();
    }

    public void printRoster() {
        for (Student student : students) {
            System.out.println(student.getId() + ":" + student.getName());
        }
    }
}
