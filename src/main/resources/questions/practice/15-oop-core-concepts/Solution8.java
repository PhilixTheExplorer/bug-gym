import java.util.*;

public class Solution8 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String companyName = sc.next();
        int n = sc.nextInt();

        Company company = new Company(companyName);
        for (int i = 0; i < n; i++) {
            int id = sc.nextInt();
            String name = sc.next();
            double base = sc.nextDouble();
            String type = sc.next();

            PayPolicy policy = type.equals("B") ? new BonusPolicy() : new RegularPolicy();
            company.hire(new Staff(id, name, base, policy));
        }

        int fireId = sc.nextInt();
        company.fire(fireId);

        company.printStaff();
        System.out.println("Total " + company.payroll());
        sc.close();
    }
}

interface PayPolicy {
    double monthlyPay(double base);
}

class RegularPolicy implements PayPolicy {
    @Override
    public double monthlyPay(double base) {
        return base;
    }
}

class BonusPolicy implements PayPolicy {
    @Override
    public double monthlyPay(double base) {
        return base + (0.2 * base);
    }
}

class Staff {
    private int id;
    private String name;
    private double baseSalary;
    private PayPolicy policy;

    public Staff(int id, String name, double baseSalary, PayPolicy policy) {
        this.id = id;
        this.name = name;
        this.baseSalary = baseSalary;
        this.policy = policy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double pay() {
        return policy.monthlyPay(baseSalary);
    }
}

class Company {
    private String name;
    private ArrayList<Staff> staffs;

    public Company(String name) {
        this.name = name;
        this.staffs = new ArrayList<>();
    }

    public void hire(Staff s) {
        for (Staff st : staffs) {
            if (st.getId() == s.getId()) {
                return;
            }
        }
        staffs.add(s);
    }

    public void fire(int id) {
        for (int i = 0; i < staffs.size(); i++) {
            if (staffs.get(i).getId() == id) {
                staffs.remove(i);
                return;
            }
        }
    }

    public double payroll() {
        double total = 0;
        for (Staff s : staffs) {
            total += s.pay();
        }
        return total;
    }

    public void printStaff() {
        for (Staff s : staffs) {
            System.out.println(s.getId() + " " + s.getName() + " " + s.pay());
        }
    }
}
