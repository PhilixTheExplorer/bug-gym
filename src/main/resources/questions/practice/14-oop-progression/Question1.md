## Question 1
Create a `Student` class with fields `id` (int), `name` (String), and `score` (double).

Implement constructor, getters, setters, and method `isPass()` that returns `true` when score is at least `50`, otherwise `false`.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|101 Alice 67.5|id: 101 name: Alice pass: true|
|205 Bob 49.9|id: 205 name: Bob pass: false|

### Starter Code
```java

public class StudentTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        String name = sc.next();
        double score = sc.nextDouble();

        Student s = new Student(id, name, score);
        System.out.println("id: " + s.getId() + " name: " + s.getName() + " pass: " + s.isPass());
    }
}

```