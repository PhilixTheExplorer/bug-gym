## Question 4
Apply `abstract` classes and method overriding.

- `Shape` is abstract with method `double area()`.
- `Rectangle` extends `Shape` with width and height.
- `Circle` extends `Shape` with radius.
- Use `PI = 3.14` for circle area.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|4 5 3|Rectangle 20.00<br>Circle 28.26|

### Starter Code
```java

public class ShapeTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double w = sc.nextDouble();
        double h = sc.nextDouble();
        double r = sc.nextDouble();

        Shape s1 = new Rectangle(w, h);
        Shape s2 = new Circle(r);

        System.out.printf("Rectangle %.2f%n", s1.area());
        System.out.printf("Circle %.2f%n", s2.area());
    }
}

```
