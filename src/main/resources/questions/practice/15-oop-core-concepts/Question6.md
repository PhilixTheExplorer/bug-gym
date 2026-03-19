## Question 6
Combine `ArrayList`, `abstract`, `final`, and overriding.

- `Task` is abstract with fields `title` and `hours`.
- It has `final` method `baseCost()` returning `hours * 100`.
- It has abstract method `extraCost()`.
- Method `totalCost()` returns `baseCost() + extraCost()`.
- `DevTask` extra cost = `hours * 60`.
- `TestTask` extra cost = `hours * 30`.
- `Sprint` keeps `ArrayList<Task>` and computes total sprint cost.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|API 5 UItest 4|API 800.0<br>UItest 520.0<br>Sprint total 1320.0|

### Starter Code
```java

public class SprintTester {
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
    }
}

```
