## Question 8
Implement an aggregation-style classroom system with add/remove and reporting.

- `Student` has `id` and `name`.
- `Classroom` has `roomCode` and `ArrayList<Student>`.
- `enroll(Student s)` adds student if no existing student has the same id.
- `drop(int id)` removes the student by id if present.
- `count()` returns number of students.
- `printRoster()` prints each student in order as `id:name` using a for loop.

`Student` objects can exist independently of `Classroom`.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|CS101 4 1 Ann 2 Ben 2 Bee 3 Cam 2|1:Ann<br>3:Cam<br>Count 2|

### Starter Code
```java

public class ClassroomTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String code = sc.next();
        int n = sc.nextInt();

        Classroom c = new Classroom(code);
        for (int i = 0; i < n; i++) {
            int id = sc.nextInt();
            String name = sc.next();
            c.enroll(new Student(id, name));
        }

        int dropId = sc.nextInt();
        c.drop(dropId);

        c.printRoster();
        System.out.println("Count " + c.count());
    }
}

```
