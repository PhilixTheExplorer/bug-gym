## Question 1
Given a Car class and a driver class called CarCaller, please complete a Car class that supports the driver class.

A Car class should contains the data of car id (int), brand (String), color (String), the name of owner (String), and total driving distance (double). Each data should have getter and setter methods. It should provide at least two constructors: (i) a constructor taking only id as input and (ii) a constructor taking inputs for all attributes (i.e., id, brand, color, and owner name). It also has a method called drive which takes an input of driving distance as double. For each drive, the drive method will accumulate total driving distance for a car. The method getTotalDrivingDist must return total driving distance of a car.

| **Input**     | **Result** |
|:--------------|:-----------|
||----- car1 Info -----<br>id: 15012016 brand: Lamborghini Veneno color: Black owner: James total driving distance: 135.0 <br>----- car2 Info ----- <br>id: 20022011 brand: Jajuar XKR-S color: Blue owner: Paul total driving distance: 350.0 <br>----- car3 Info ----- <br>id: 11112015 brand: Mercedes-AMG GT color: Yello owner: Smith total driving distance: 0.0|

### Starter Code

```java
class Car {
	//put your code here
}

public class CarCaller {
	public static void main(String[] args){
		Car car1 = new Car(15012016);
		Car car2 = new Car(20022011, "Jajuar XKR-S", "Blue", "Paul");
		Car car3 = new Car(11112015, "Mercedes-AMG GT", "Yello", "Smith");
		
		car1.setBrand("Lamborghini Veneno");
		car1.setColor("Black");
		car1.setOwner("James");
		
		car1.drive(120);
		car2.drive(300);
		car2.drive(50);
		car1.drive(15);
		
		System.out.println("----- car1 Info -----");
		System.out.println("id: "+car1.getId()+ 
				" brand: "+ car1.getBrand() +
				" color: "+ car1.getColor() + 
				" owner: "+ car1.getOwner() +
				" total driving distance: " + car1.getTotalDrivingDist());
		
		System.out.println("----- car2 Info -----");
		System.out.println("id: "+car2.getId()+ 
				" brand: "+ car2.getBrand() +
				" color: "+ car2.getColor() + 
				" owner: "+ car2.getOwner() +
				" total driving distance: " + car2.getTotalDrivingDist());

		System.out.println("----- car3 Info -----");
		System.out.println("id: "+car3.getId()+ 
				" brand: "+ car3.getBrand() +
				" color: "+ car3.getColor() + 
				" owner: "+ car3.getOwner() +
				" total driving distance: " + car3.getTotalDrivingDist());
				
	}
}
```
