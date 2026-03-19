import java.util.*;

public class Solution3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String n1 = sc.next();
        double f1 = sc.nextDouble();
        String n2 = sc.next();
        double f2 = sc.nextDouble();

        Member m1 = new Member(n1, f1);
        Member m2 = new Member(n2, f2);

        System.out.println(m1.getId() + " " + m1.getName() + " " + m1.totalFee());
        System.out.println(m2.getId() + " " + m2.getName() + " " + m2.totalFee());
        sc.close();
    }
}

class Member {
    private static int nextId = 1001;
    public static final double VAT = 0.07;

    private int id;
    private String name;
    private double fee;

    public Member(String name, double fee) {
        this.id = nextId++;
        this.name = name;
        this.fee = fee;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double totalFee() {
        return fee + fee * VAT;
    }
}
