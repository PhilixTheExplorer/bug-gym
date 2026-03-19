## Question 5  
Giving the UML Class Diagram as below. Write a program according to the given class diagram.

![LineItemUML](invoice_uml.png)

An invoice consists of many line items which indicate product, quantity and price per unit. The Invoice class holds line item in the ArrayList. It can add a line item by using addLineItem(LineItem line) method. This method will add a line item into the ArrayList. The logic of adding a line item is to check if the line item is already existed in the ArrayList. If a line item already exists, it will print "This line item already exists." Otherwise, the line item is added to ArrayList. Similarly, to remove a line item, the removeLineItem(LineItem line) method will check if the ArrayList contains the line item. If it contains the line item, it will remove the line item directly. Otherwise, it will print "This line item does not exist.".

The LineItem class represents line item. Each line item contains product name, quantity and price per unit. The attribute line_sum is the total price of the line item which is calculated from quantity * unit_price. Furthermore, the method setQuantity(int quantity) and setUnit_price(double unit_price) will also update line_sum whenever they are called.

Noted that the driver class is provided in the preloaded answer box. Please do not modify the driver class.

| **Input**     | **Result** |
|:--------------|:-----------|
||This line item does not exist.<br>This line item already exists.<br>----------- Invoice 1 Info -----------<br>Lays 5 20.0<br>KitKat 2 15.0<br>CocaCola 2 17.5<br>Total 165.0<br>----------- Invoice 2 Info -----------<br>UHT 3 8.0<br>Milo-Icecream 2 10.0<br>Nestle 2 7.5<br>Total 59.0|

### Starter Code

```java
public class Tester {

  public static void main(String args[]) {
    Tester t = new Tester();
    Invoice in1 = new Invoice(001);
    LineItem l1 = new LineItem("Lays", 5, 20);
    LineItem l2 = new LineItem("KitKat", 2, 15);
    LineItem l3 = new LineItem("CocaCola", 2, 17.5);
    in1.addLineItem(l1);
    in1.removeLineItem(l3);
    in1.addLineItem(l2);
    in1.addLineItem(l3);
    in1.addLineItem(l1);
    System.out.println(
      "----------- Invoice " + in1.getId() + " Info -----------"
    );
    t.printLineItem(in1);
    System.out.println("Total " + t.getTotal(in1));

    Invoice in2 = new Invoice(003);
    in2.setId(002);
    LineItem l4 = new LineItem("UHT", 6, 8);
    LineItem l5 = new LineItem("Milo-Icecream", 2, 15);
    LineItem l6 = new LineItem("7-select", 2, 7.5);
    l4.setQuantity(3);
    l5.setUnit_price(10);
    l6.setProduct("Nestle");
    in2.addLineItem(l4);
    in2.addLineItem(l5);
    in2.addLineItem(l6);
    System.out.println(
      "----------- Invoice " + in2.getId() + " Info -----------"
    );
    t.printLineItem(in2);
    System.out.println("Total " + t.getTotal(in2));
  }

  public double getTotal(Invoice in) {
    double total = 0;
    for (int i = 0; i < in.getLineItems().size(); i++) {
      total += in.getLineItems().get(i).getLine_sum();
    }
    return total;
  }

  public void printLineItem(Invoice in) {
    for (int i = 0; i < in.getLineItems().size(); i++) {
      LineItem l = in.getLineItems().get(i);
      System.out.println(
        l.getProduct() + " " + l.getQuantity() + " " + l.getUnit_price()
      );
    }
  }
}
```
