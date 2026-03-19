import java.util.*;

public class Solution6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String devTitle = sc.next();
        double devHours = sc.nextDouble();
        String testTitle = sc.next();
        double testHours = sc.nextDouble();

        Sprint sprint = new Sprint();
        Task t1 = new DevTask(devTitle, devHours);
        Task t2 = new TestTask(testTitle, testHours);

        sprint.addTask(t1);
        sprint.addTask(t2);

        System.out.println(t1.getTitle() + " " + t1.totalCost());
        System.out.println(t2.getTitle() + " " + t2.totalCost());
        System.out.println("Sprint total " + sprint.totalCost());
        sc.close();
    }
}

abstract class Task {
    private String title;
    private double hours;

    public Task(String title, double hours) {
        this.title = title;
        this.hours = hours;
    }

    public String getTitle() {
        return title;
    }

    public double getHours() {
        return hours;
    }

    public final double baseCost() {
        return hours * 100;
    }

    public abstract double extraCost();

    public double totalCost() {
        return baseCost() + extraCost();
    }
}

class DevTask extends Task {
    public DevTask(String title, double hours) {
        super(title, hours);
    }

    @Override
    public double extraCost() {
        return getHours() * 60;
    }
}

class TestTask extends Task {
    public TestTask(String title, double hours) {
        super(title, hours);
    }

    @Override
    public double extraCost() {
        return getHours() * 30;
    }
}

class Sprint {
    private ArrayList<Task> tasks = new ArrayList<>();

    public void addTask(Task t) {
        tasks.add(t);
    }

    public double totalCost() {
        double sum = 0;
        for (Task t : tasks) {
            sum += t.totalCost();
        }
        return sum;
    }
}
