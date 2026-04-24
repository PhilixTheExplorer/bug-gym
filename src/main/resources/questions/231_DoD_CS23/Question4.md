## Question 4: Bomber Man Lite 2D

You are creating a Bomber Man Lite 2D with a field size of **10X10**.
The rule is a player can put a bomb in any cell of the field. When a bomb explores, it will destroy a cell itself and all **adjacent** cells in **8 directions** (i.e., up, down, left, right, and on diagonals).

![bomber](bomber.png)

Your task is to write a program to count the number of destroyed cells in the field when the number of bombs and position of bombs are provided.

**Input:**

* The first line of the input is a number of bombs in the field.
* The following lines are the position of each bomb (row and column) located in the field.

**Output:**

* The total number of destroyed cells in the field.

### Example:

| **Input** | **Result** |
| --- | --- |
| 2 <br> 4 3 <br> 8 8 | 18 |
| 0 | 0 |
| 1 <br> 0 0 | 4 |
| 2 <br> 0 0 <br> 0 1 | 6 |