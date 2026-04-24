## Question 4

Giving a grid (two-dimensional array) containing numbers, write a program to change the number by 1 per click to the clicked cell and its adjacent cells (i.e., right cell, left cell, top cell, bottom cell). The number will be changed up to 3. If the cell already contains value of 3, the number will be changed back to 1 again.

**For example, giving the array of 4 rows and 5 columns, if the clicked position is at (1,2) (2,2), (1,3) and (1,3) respectively, the result is shown as below.**

![q4](q4.png)

The input has three parts. The first part is two numbers indicate row and column and the arrays. The second part is a number indicate number of clicks. The last part is the list of clicked position in a pair of row and column.

### For example:

| **Input** | **Result** |
| --- | --- |
| 4 5 <br> 4 <br> 1 2 <br> 2 2 <br> 1 3 <br> 1 3 | 0 0 1 2 0 <br> 0 1 1 3 2 <br> 0 1 2 3 0 <br> 0 0 1 0 0 |
| 3 3 <br> 1 <br> 0 0 | 1 1 0 <br> 1 0 0 <br> 0 0 0 |
| 2 2 <br> 3 <br> 0 0 <br> 0 0 <br> 0 0 | 3 3 <br> 3 0 |
| 1 1 <br> 4 <br> 0 0 <br> 0 0 <br> 0 0 <br> 0 0 | 1 |