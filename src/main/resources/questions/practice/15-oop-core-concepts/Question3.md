## Question 3
Use `static` and `final` in class design.

- Class `Member` has fields: `id` (int), `name` (String), `fee` (double).
- `id` is auto-generated using a static counter starting from 1001.
- `VAT` is a `final static` constant = 0.07.
- Method `totalFee()` returns `fee + fee * VAT`.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|Alice 1000 Bob 500|1001 Alice 1070.0<br>1002 Bob 535.0|

### Starter Code
```java

public class MemberTester {
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
    }
}

```
