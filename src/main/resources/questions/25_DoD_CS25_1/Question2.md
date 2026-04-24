## Question 2

Write a program to encrypt the given sentence with following steps:

1. Convert a sentence to uppercase.
2. Swap a character with its swap pair.
3. Reverse a sentence and print.

**The input consists of 3 parts:**

1. A sentence
2. Number of swap pair
3. Swap pair

**For example, the input is:**

```text
Hello Java
2
E T
A R
```

**The output is:**
`RVRJ OLLTH`

**Here are steps of encryption.**

1. Convert a sentence to uppercase -> `HELLO JAVA`
2. Swap a character with its swap pair. -> `HTLLO JRVR`
3. Reverse a sentence and print. -> `RVRJ OLLTH`

### For example:

| **Input** | **Result** |
| --- | --- |
| Hello Java <br> 2 <br> E T <br> A R | RVRJ OLLTH |
| test string <br> 0 | GNIRTS TSET |
| 123 abc! <br> 1 <br> B X | !CXA 321 |
| x <br> 1 <br> X Y | Y |