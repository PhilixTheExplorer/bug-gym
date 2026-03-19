## Question 2
Write classes for a simple owner-pet system.

- `Pet` has `name` (String) and `species` (String).
- `Owner` has `ownerName` (String) and `ArrayList<Pet>`.
- `addPet(Pet p)` adds pet only if no existing pet has the same name.
- `printPets()` prints all pets in insertion order as `name(species)` separated by one space.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|Mina 4 Coco cat Koko dog Coco bird Nemo fish|Coco(cat) Koko(dog) Nemo(fish)|

### Starter Code
```java

public class OwnerTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String ownerName = sc.next();
        int n = sc.nextInt();

        Owner o = new Owner(ownerName);
        for (int i = 0; i < n; i++) {
            String petName = sc.next();
            String species = sc.next();
            o.addPet(new Pet(petName, species));
        }
        o.printPets();
    }
}

```