## Question 5
Write an adventure game program according to the given the UML diagram below. 

![PlayerUML](PlayerUML.png)

The Player class represents a player in the game. The Player class has an attribute named playerCount which will increase every time, if a new player is created (the playerCount value start from 0). Each player can own many characters. From the start, each player will earn 10 bahts and start at level 1 and player id will be generate using the number of playerCount. 

A Player can choose character that he/she owns to adventure using the adventure method. The logic of method checks if whether the selected character is in the characters list as the order of following conditions:

1. If the player does not own any character it should print (println) "You do not own any character".

2. If the player does not own the character it should print (println) "You do not own this character".

3. If the player owns the character, the player's level and character's level increases +5 points and money increases +10. 

A Player can buy new character using the method buyCharacter. The price for each character is based on rank (i.e., rank : '3' cost 15 baht, '2' cost 10 baht, and '1' cost 5 baht). The buying logic is based on the order of following conditions:

1. If the character rank are not 1, 2, and 3 it should print (println) "The rank of this character are not supported". 

2. If a player do not have enough money it should print (println) "You do not have enough money". 

3. Otherwise, a character can be bought. The money must be reduced properly and the character must be added to the list of owned characters.

The method getAllCharacter lists all characters' name the player has own. Print name of characters line by line. Hint: Use the loop to iterate through the ArrayList.

The Character class is an abstract class which provides template for each character when a character is created, its level will start at 1. The method increaseLevel will increase the level by adding up the current level points to the input level points.

The Geo, Anemo, and Electro class inherit properties from Character class. Each of them can perform different kind of attack to an enemy (i.e., stoneStatue, windFlow, electricCurrent). Attack skill will decrease an enemy healthPoint consider by character rank, character level, and class. The formula for calculating attack are:

Geo = (Level x 0.2) + (rank x 2)

Anemo = (Level x 0.5) + (rank x 1.5)

Electro = (Level x 0.1) + (rank x 2) + 5

When a character defeats the enemy (i.e., healthPoint <= 0), it should print (println) "You defeated an enemy" and its level will be increase by +1 by using the method increaseLevel.

The driver class of *Tester* is provided in the answer box for testing the game system. Please do not modify the driver class since your output may be incorrect.

| **Input**     | **Result** |
|:--------------|:-----------|
||--------------------------------<br>Player: Float<br>Player Id: 0<br>Level: 1 Money: 10<br>--------------------------------<br>Player: BABA<br>Player Id: 1<br>Level: 1 Money: 10<br>--------------------------------<br>You do not own any character<br>You do not have enough money<br>--------------------------------<br>Player: Float<br>Player Id: 0<br>Level: 1 Money: 5<br>--------------------------------<br>Character: Beidou Level: 1<br>--------------------------------<br>Player: Float<br>Player Id: 0<br>Level: 6 Money: 15<br>--------------------------------<br>10.0<br>2.4000000000000004<br>You defeated an enemy<br>--------------------------------<br>You do not own this character<br>Beidou<br>Zhong Li<br>--------------------------------<br>Enemy Health: 5.0<br>Character: Zhong Li Level: 11<br>You defeated an enemy<br>Character: Zhong Li Level: 12<br>--------------------------------<br>The rank of this character are not supported|

### Starter Code

```java
import java.util.ArrayList;

public class AdventureGame {
    public static void main(String[] args) {
        Player p1 = new Player("Float");
        Player p2 = new Player("BABA");
        System.out.println("--------------------------------");
        System.out.println("Player: "+p1.getName());
        System.out.println("Player Id: "+p1.getId());
        System.out.println("Level: "+p1.getLevel() + " Money: " + p1.getMoney());
        System.out.println("--------------------------------");
        System.out.println("Player: "+p2.getName());
        System.out.println("Player Id: "+p2.getId());
        System.out.println("Level: "+p2.getLevel() + " Money: " + p2.getMoney());
        System.out.println("--------------------------------");
        Geo ZhongLi = new Geo("Zhong Li", 3);
        Anemo Venti = new Anemo("Venti", 2);
        Electro RaidenShogun = new Electro("Raiden Shogun", 3);
        Electro Beidou = new Electro("Beidou", 1);
        p1.adventure(Beidou);
        p1.buyCharacter(RaidenShogun);
        p1.buyCharacter(Beidou);
        System.out.println("--------------------------------");
        System.out.println("Player: "+p1.getName());
        System.out.println("Player Id: "+p1.getId());
        System.out.println("Level: "+p1.getLevel() + " Money: " + p1.getMoney());
        System.out.println("--------------------------------");
        System.out.println("Character: "+ Beidou.getName() + " Level: " +Beidou.getLevel());
        p1.adventure(Beidou);
        System.out.println("--------------------------------");
        System.out.println("Player: "+p1.getName());
        System.out.println("Player Id: "+p1.getId());
        System.out.println("Level: "+p1.getLevel() + " Money: " + p1.getMoney());
        System.out.println("--------------------------------");

        Enemy e1 = new Enemy(10);
        System.out.println(e1.getHealthPoint());
        Beidou.electricCurrent(e1);
        System.out.println(e1.getHealthPoint());
        Beidou.electricCurrent(e1);
        System.out.println("--------------------------------");
        p1.adventure(RaidenShogun);
        p1.buyCharacter(ZhongLi);
        p1.getAllCharacter();
        p1.adventure(ZhongLi);
        p1.adventure(ZhongLi);
        p1.buyCharacter(RaidenShogun);
        System.out.println("--------------------------------");
        Enemy e2 = new Enemy(5);
        System.out.println("Enemy Health: "+e2.getHealthPoint());
        System.out.println("Character: "+ ZhongLi.getName() + " Level: " +ZhongLi.getLevel());
        ZhongLi.stoneStatue(e2);
        System.out.println("Character: "+ ZhongLi.getName() + " Level: " +ZhongLi.getLevel());
        System.out.println("--------------------------------");
        Anemo RiceShower = new Anemo("Rice Shower",500);
        p1.buyCharacter(RiceShower);
    }
}
```
