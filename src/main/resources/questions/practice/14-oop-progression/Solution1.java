import java.util.*;

public class Solution1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        String name = sc.next();
        double score = sc.nextDouble();

        Student s = new Student(id, name, score);
        System.out.println("id: " + s.getId() + " name: " + s.getName() + " pass: " + s.isPass());

        sc.close();
    }
}

class Student {
    private int id;
    private String name;
    private double score;

    public Student(int id, String name, double score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isPass() {
        return score >= 50;
    }
}
