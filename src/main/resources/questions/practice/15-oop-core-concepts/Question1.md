## Question 1
Create a class `Wallet` that demonstrates encapsulation.

- Fields: `owner` (String), `balance` (double)
- Keep fields private.
- Constructor sets owner and initial balance.
- Method `deposit(double amount)` adds money only when amount > 0.
- Method `withdraw(double amount)` subtracts money only when amount > 0 and not greater than current balance.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|Mina 500 200 900|owner: Mina balance: 700.0|
|Ken 300 -5 50|owner: Ken balance: 250.0|

### Starter Code
```java

public class WalletTester {
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
    }
}

```
