## Question 4
Implement a payment system using interfaces.

- `Payable` interface has method `double getPayment()`.
- `Product` has `name`, `price`, and `quantity`.
- `Product` implements `Payable`, where payment is `price * quantity`.
- `Service` has `serviceName` and `hours`.
- `Service` implements `Payable`, where payment is `hours * 250`.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|Mouse 450 2 Cleaning 3|Product payment: 900.0<br>Service payment: 750.0<br>Total payment: 1650.0|

### Starter Code
```java

public class PayableTester {
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
    }
}

```