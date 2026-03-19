import java.util.*;

public class Solution4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String productName = sc.next();
        double productPrice = sc.nextDouble();
        int qty = sc.nextInt();

        String serviceName = sc.next();
        int serviceHours = sc.nextInt();

        Payable p1 = new Product(productName, productPrice, qty);
        Payable p2 = new Service(serviceName, serviceHours);

        System.out.println("Product payment: " + p1.getPayment());
        System.out.println("Service payment: " + p2.getPayment());
        System.out.println("Total payment: " + (p1.getPayment() + p2.getPayment()));

        sc.close();
    }
}

interface Payable {
    double getPayment();
}

class Product implements Payable {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public double getPayment() {
        return price * quantity;
    }
}

class Service implements Payable {
    private String serviceName;
    private int hours;

    public Service(String serviceName, int hours) {
        this.serviceName = serviceName;
        this.hours = hours;
    }

    @Override
    public double getPayment() {
        return hours * 250;
    }
}
