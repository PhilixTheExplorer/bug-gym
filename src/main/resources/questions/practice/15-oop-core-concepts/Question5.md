## Question 5
Use interfaces with multiple implementations.

- Interface `Discountable` has method `double getDiscountedPrice()`.
- `FoodItem` implements `Discountable` with 10% discount.
- `ElectronicItem` implements `Discountable` with 15% discount.
- Both classes store `name` and `price`.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|Bread 80 Mouse 600|Food: 72.0<br>Electronic: 510.0<br>Total: 582.0|

### Starter Code
```java

public class DiscountTester {
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
    }
}

```
