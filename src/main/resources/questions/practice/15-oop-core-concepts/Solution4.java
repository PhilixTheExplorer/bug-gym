import java.util.*;

public class Solution4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double w = sc.nextDouble();
        double h = sc.nextDouble();
        double r = sc.nextDouble();

        Shape s1 = new Rectangle(w, h);
        Shape s2 = new Circle(r);

        System.out.printf("Rectangle %.2f%n", s1.area());
        System.out.printf("Circle %.2f%n", s2.area());
        sc.close();
    }
}

abstract class Shape {
    public abstract double area();
}

class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }
}

class Circle extends Shape {
    private double radius;
    private static final double PI = 3.14;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return PI * radius * radius;
    }
}
