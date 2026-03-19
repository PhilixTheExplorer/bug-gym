## Question 5

In October 2021, Apple released redesigned MacBook Pro 14 inches and 16 inches. They also announce the new Apple Silicon, M1 Pro, and M1 Max processor for two new MacBook Pro. The only major difference between those processors is the maximum memory support. On M1 Pro, it only supports memory up to 32GB and 64GB on M1 Max. By default, each MacBook Pro has 16GB of memory, but the user can change the amount of memory using the modifyMemory function. There is no limitation of CPUCore, GPUCore, and neuralEngineCore on both of the processors. Implement the Java classes according to the UML below.

![SiliconUML](SiliconUML.png)

Note that the modifyMemory function in MacBookPro class will receive an integer parameter which represents the amount of memory that the user wants to change in GB. If it is larger than the maximum memory that the processor can support, print the message `ERROR` and change nothing.

Noted that, the driver class and some layouts of each class are already provided in the preload answer. Please do not modify the driver class since the output may be incorrect.

### Starter Code

```java
public class TestDriver {

    public static void main(String[] args) {
        // DO NOT MODIFY Main class and main function
        M1Pro m1Pro = new M1Pro(8, 14, 16);
        M1Max m1Max = new M1Max(10, 24, 16);

        MacBookPro mbp1 = new MacBookPro(m1Pro);
        MacBookPro mbp2 = new MacBookPro(m1Max);
        System.out.println("-----------MacBook Pro 1-----------");
        System.out.println("MBP1, Memory: " + mbp1.getMemory().getMemoryInGB() + " GB");
        System.out.println("MBP1, CPU: " + mbp1.getSilicon().getCPUCore() + " Cores");
        System.out.println("MBP1, GPU: " + mbp1.getSilicon().getGPUCore() + " Cores");
        System.out.println("MBP1, NeuralEngine: " + mbp1.getSilicon().getNeuralEngineCore() + " Cores");
        System.out.println("MBP1, MaxMemory: " + mbp1.getSilicon().getMaximumMemorySupportedInGB() + " GB");
        mbp1.modifyMemory(32);
        System.out.println("MBP1, Memory: " + mbp1.getMemory().getMemoryInGB() + " GB");
        mbp1.modifyMemory(64);
        System.out.println("MBP1, Memory: " + mbp1.getMemory().getMemoryInGB() + " GB");

        System.out.println("-----------MacBook Pro 2-----------");
        System.out.println("MBP2, Memory: " + mbp2.getMemory().getMemoryInGB() + " GB");
        System.out.println("MBP2, CPU: " + mbp2.getSilicon().getCPUCore() + " Cores");
        System.out.println("MBP2, GPU: " + mbp2.getSilicon().getGPUCore() + " Cores");
        System.out.println("MBP2, NeuralEngine: " + mbp2.getSilicon().getNeuralEngineCore() + " Cores");
        System.out.println("MBP2, MaxMemory: " + mbp2.getSilicon().getMaximumMemorySupportedInGB() + " GB");
        mbp2.modifyMemory(64);
        System.out.println("MBP2, Memory: " + mbp2.getMemory().getMemoryInGB() + " GB");
    }
}

abstract class AppleSilicon {

}

class M1Pro extends AppleSilicon {

}

class M1Max extends AppleSilicon {

}

class Memory {

}

class MacBookPro {

}
```

### For example:

| **Input**     | **Result** |
|:--------------|:-----------|
||-----------MacBook Pro 1----------- <br> MBP1, Memory: 16 GB<br>MBP1, CPU: 8 Cores<br>MBP1, GPU: 14 Cores<br>MBP1, NeuralEngine: 16 Cores<br>MBP1, MaxMemory: 32 GB<br>MBP1, Memory: 32 GB<br>ERROR<br>MBP1, Memory: 32 GB<br>-----------MacBook Pro 2-----------<br>MBP2, Memory: 16 GB<br>MBP2, CPU: 10 Cores<br>MBP2, GPU: 24 Cores<br>MBP2, NeuralEngine: 16 Cores<br>MBP2, MaxMemory: 64 GB<br>MBP2, Memory: 64 GB|
