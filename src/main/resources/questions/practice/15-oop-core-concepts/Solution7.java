import java.util.*;

public class Solution7 {
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
        sc.close();
    }
}

class LineItem {
    private String name;
    private int qty;
    private double unitPrice;

    public LineItem(String name, int qty, double unitPrice) {
        this.name = name;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public void addQty(int q) {
        this.qty += q;
    }

    public double subtotal() {
        return qty * unitPrice;
    }
}

class Order {
    private String orderNo;
    private ArrayList<LineItem> items;

    public Order(String orderNo) {
        this.orderNo = orderNo;
        this.items = new ArrayList<>();
    }

    public void addItem(LineItem item) {
        for (LineItem li : items) {
            if (li.getName().equals(item.getName())) {
                li.addQty(item.getQty());
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(name)) {
                items.remove(i);
                return;
            }
        }
    }

    public double grandTotal() {
        double total = 0;
        for (LineItem li : items) {
            total += li.subtotal();
        }
        return total;
    }

    public void printItems() {
        for (LineItem li : items) {
            System.out.println(li.getName() + " " + li.getQty() + " " + li.subtotal());
        }
    }
}
