## Question 6
Implement object sorting with `Comparable`.

- `Player` has `name` and `score`.
- Implement `Comparable<Player>`.
- Sort by score in descending order.
- If scores are equal, sort by name in ascending lexicographic order.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|5 Ken 88 Ann 91 Bob 88 Zoe 91 Max 70|Ann 91<br>Zoe 91<br>Bob 88<br>Ken 88<br>Max 70|

### Starter Code
```java

public class PlayerTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            int score = sc.nextInt();
            players.add(new Player(name, score));
        }

        Collections.sort(players);
        for (Player p : players) {
            System.out.println(p.getName() + " " + p.getScore());
        }
    }
}

```