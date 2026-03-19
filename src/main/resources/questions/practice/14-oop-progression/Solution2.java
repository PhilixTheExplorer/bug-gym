import java.util.*;

public class Solution2 {
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

        sc.close();
    }
}

class Pet {
    private String name;
    private String species;

    public Pet(String name, String species) {
        this.name = name;
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }
}

class Owner {
    private String ownerName;
    private ArrayList<Pet> pets;

    public Owner(String ownerName) {
        this.ownerName = ownerName;
        this.pets = new ArrayList<>();
    }

    public void addPet(Pet p) {
        for (Pet pet : pets) {
            if (pet.getName().equals(p.getName())) {
                return;
            }
        }
        pets.add(p);
    }

    public void printPets() {
        for (int i = 0; i < pets.size(); i++) {
            Pet p = pets.get(i);
            System.out.print(p.getName() + "(" + p.getSpecies() + ")");
            if (i < pets.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}
