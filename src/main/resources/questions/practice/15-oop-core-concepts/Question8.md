## Question 8
Model a company with aggregation and interface-based compensation.

- Interface `PayPolicy` has method `double monthlyPay(double base)`.
- `RegularPolicy` returns base.
- `BonusPolicy` returns base + 20% of base.
- `Staff` has `id`, `name`, `baseSalary`, and a `PayPolicy`.
- `Company` has `name` and `ArrayList<Staff>`.
- `hire(Staff s)` adds if id is unique.
- `fire(int id)` removes staff by id.
- `payroll()` returns total monthly pay of all current staff.
- `printStaff()` prints each staff as `id name pay` in order.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|TechCo 3 1 Amy 30000 R 2 Bob 25000 B 2 Ben 26000 R 1|2 Bob 30000.0<br>Total 30000.0|

### Starter Code
```java

public class CompanyTester {
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
    }
}

```
