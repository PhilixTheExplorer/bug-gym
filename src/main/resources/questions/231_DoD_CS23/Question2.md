## Question 2: Case-Based Sentence Encryption

Your task is to encrypt the message with a certain number of words according to the following conditions:

1. If the word is in the **even position** (starting counting from 0), convert to **UPPERCASE**; otherwise, convert to **lowercase**.
2. **Reverse** characters in each word.

The input is a number of words in a sentence and the sentence itself. The output is an encrypted sentence.

### Example:

| **Input** | **Result** |
| --- | --- |
| 15 <br> Java applications are typically compiled to bytecode that can run on any Java virtual machine | AVAJ snoitacilppa ERA yllacipyt DELIPMOC ot EDOCETYB taht NAC nur NO yna AVAJ lautriv ENIHCAM |
| 1 <br> hElLo | OLLEH |
| 2 <br> HeLLO wOrLd | OLLEH dlrow |
| 3 <br> 123 !@# aBc | 321 #@! CBA |