## Question 2
Practice constructor overloading with class `Box`.

- Fields: `width`, `height`, `depth` (all double)
- Constructor 1: no arguments, set all dimensions to 1.
- Constructor 2: one argument `side`, set all dimensions to side.
- Constructor 3: three arguments `width height depth`.
- Method `volume()` returns width * height * depth.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|2 3 4 5|default: 1.0<br>cube: 8.0<br>custom: 60.0|

### Starter Code
```java

public class BoxTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double side = sc.nextDouble();
        double w = sc.nextDouble();
        double h = sc.nextDouble();
        double d = sc.nextDouble();

        Box b1 = new Box();
        Box b2 = new Box(side);
        Box b3 = new Box(w, h, d);

        System.out.println("default: " + b1.volume());
        System.out.println("cube: " + b2.volume());
        System.out.println("custom: " + b3.volume());
    }
}

```
