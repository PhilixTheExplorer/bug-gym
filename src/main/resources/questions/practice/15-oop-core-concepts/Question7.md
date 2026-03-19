## Question 7
Create an order system emphasizing composition and list updates.

- `LineItem` has `name`, `qty`, and `unitPrice`.
- Method `subtotal()` returns `qty * unitPrice`.
- `Order` has `orderNo` and `ArrayList<LineItem>`.
- `addItem(LineItem item)`:
  - if an item with same name exists, increase its quantity by incoming quantity.
  - otherwise add as a new item.
- `removeItem(String name)` removes item by name if present.
- `grandTotal()` returns sum of subtotals.
- `printItems()` prints each item as `name qty subtotal` using a for loop.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|ORD9 4 Pen 2 10 Notebook 1 35 Pen 3 10 Eraser 2 5 Eraser|Pen 5 50.0<br>Notebook 1 35.0<br>Total 85.0|

### Starter Code
```java

public class OrderTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String orderNo = sc.next();
        int n = sc.nextInt();

        Order order = new Order(orderNo);
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            int qty = sc.nextInt();
            double unitPrice = sc.nextDouble();
            order.addItem(new LineItem(name, qty, unitPrice));
        }

        String removeName = sc.next();
        order.removeItem(removeName);

        order.printItems();
        System.out.println("Total " + order.grandTotal());
    }
}

```
