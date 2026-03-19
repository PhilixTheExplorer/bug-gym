import java.util.*;

public class Solution5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String foodName = sc.next();
        double foodPrice = sc.nextDouble();
        String elecName = sc.next();
        double elecPrice = sc.nextDouble();

        Discountable d1 = new FoodItem(foodName, foodPrice);
        Discountable d2 = new ElectronicItem(elecName, elecPrice);

        System.out.println("Food: " + d1.getDiscountedPrice());
        System.out.println("Electronic: " + d2.getDiscountedPrice());
        System.out.println("Total: " + (d1.getDiscountedPrice() + d2.getDiscountedPrice()));
        sc.close();
    }
}

interface Discountable {
    double getDiscountedPrice();
}

class FoodItem implements Discountable {
    private String name;
    private double price;

    public FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public double getDiscountedPrice() {
        return price * 0.90;
    }
}

class ElectronicItem implements Discountable {
    private String name;
    private double price;

    public ElectronicItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public double getDiscountedPrice() {
        return price * 0.85;
    }
}
