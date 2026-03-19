import java.util.*;

public class Solution3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String n1 = sc.next();
        String t1 = sc.next();
        double h1 = sc.nextDouble();
        double v1 = sc.nextDouble();

        String n2 = sc.next();
        String t2 = sc.next();
        double h2 = sc.nextDouble();
        double v2 = sc.nextDouble();

        Employee e1 = (t1.equals("F")) ? new FullTimeEmployee(n1, h1, v1) : new PartTimeEmployee(n1, h1, v1);
        Employee e2 = (t2.equals("F")) ? new FullTimeEmployee(n2, h2, v2) : new PartTimeEmployee(n2, h2, v2);

        System.out.println(e1.getName() + " " + e1.calcPay());
        System.out.println(e2.getName() + " " + e2.calcPay());
        System.out.println("Total " + (e1.calcPay() + e2.calcPay()));

        sc.close();
    }
}

abstract class Employee {
    private String name;
    private double hours;

    public Employee(String name, double hours) {
        this.name = name;
        this.hours = hours;
    }

    public String getName() {
        return name;
    }

    public double getHours() {
        return hours;
    }

    public abstract double calcPay();
}

class FullTimeEmployee extends Employee {
    private double monthlySalary;

    public FullTimeEmployee(String name, double hours, double monthlySalary) {
        super(name, hours);
        this.monthlySalary = monthlySalary;
    }

    @Override
    public double calcPay() {
        return monthlySalary;
    }
}

class PartTimeEmployee extends Employee {
    private double hourlyRate;

    public PartTimeEmployee(String name, double hours, double hourlyRate) {
        super(name, hours);
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calcPay() {
        return getHours() * hourlyRate;
    }
}
