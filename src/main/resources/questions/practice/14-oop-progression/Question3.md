## Question 3
Use inheritance and polymorphism to compute employee payment.

- `Employee` is an abstract class with fields `name` and `hours`.
- It has abstract method `double calcPay()`.
- `FullTimeEmployee` has extra field `monthlySalary` and `calcPay()` returns monthlySalary.
- `PartTimeEmployee` has extra field `hourlyRate` and `calcPay()` returns `hours * hourlyRate`.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|Ana F 160 35000 Ben P 42 180|Ana 35000.0<br>Ben 7560.0<br>Total 42560.0|

### Starter Code
```java

public class EmployeeTester {
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
    }
}

```