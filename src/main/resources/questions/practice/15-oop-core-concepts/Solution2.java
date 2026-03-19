import java.util.*;

public class Solution2 {
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
        sc.close();
    }
}

class Box {
    private double width;
    private double height;
    private double depth;

    public Box() {
        this(1, 1, 1);
    }

    public Box(double side) {
        this(side, side, side);
    }

    public Box(double width, double height, double depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public double volume() {
        return width * height * depth;
    }
}
