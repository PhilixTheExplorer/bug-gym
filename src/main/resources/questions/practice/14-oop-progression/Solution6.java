import java.util.*;

public class Solution6 {
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

        sc.close();
    }
}

class Player implements Comparable<Player> {
    private String name;
    private int score;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Player other) {
        if (this.score != other.score) {
            return Integer.compare(other.score, this.score);
        }
        return this.name.compareTo(other.name);
    }
}
