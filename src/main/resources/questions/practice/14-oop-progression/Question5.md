## Question 5
Write classes for an energy-aware device system.

- `Device` is an abstract class with fields `name` and `hoursUsed`.
- It has method `use(double h)` that adds to `hoursUsed` only when `h > 0`.
- It has abstract method `double energy()`.
- `Fan` extends `Device` with `watt` and energy formula: `(watt * hoursUsed) / 1000`.
- `AirConditioner` extends `Device` with `btu` and energy formula: `(btu * hoursUsed) / 12000`.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|StandFan 75 8 AC9000 9000 6|StandFan 0.6<br>AC9000 4.5<br>Total 5.1|

### Starter Code
```java

public class DeviceTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String fName = sc.next();
        double fanWatt = sc.nextDouble();
        double fanHours = sc.nextDouble();

        String acName = sc.next();
        double acBtu = sc.nextDouble();
        double acHours = sc.nextDouble();

        Device d1 = new Fan(fName, fanWatt);
        Device d2 = new AirConditioner(acName, acBtu);

        d1.use(fanHours);
        d2.use(acHours);

        System.out.println(d1.getName() + " " + d1.energy());
        System.out.println(d2.getName() + " " + d2.energy());
        System.out.println("Total " + (d1.energy() + d2.energy()));
    }
}

```