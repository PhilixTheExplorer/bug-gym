import java.util.*;

public class Solution1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String owner = sc.next();
        double initial = sc.nextDouble();
        double dep = sc.nextDouble();
        double wd = sc.nextDouble();

        Wallet w = new Wallet(owner, initial);
        w.deposit(dep);
        w.withdraw(wd);

        System.out.println("owner: " + w.getOwner() + " balance: " + w.getBalance());
        sc.close();
    }
}

class Wallet {
    private String owner;
    private double balance;

    public Wallet(String owner, double balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }
}
