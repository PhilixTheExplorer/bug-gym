## Question 5

![q5](q5_oop.png)

The **Owner** class represents a vehicle owner, storing their name and a list of owned vehicles. The `addVehicle` method adds a vehicle to the list if it is not already present. The `move` method moves a specified vehicle using the given speed and distance via the `move` method of the **Vehicle** class.

The **Vehicle** class represents any type of vehicle, storing its brand, type, model, color, and accumulative distance (initialized to 0). The `printInfo` method displays vehicle details. The `move` method is abstract and must be implemented by concrete subclasses.

The **Car** class is a concrete subclass of *Vehicle* with additional attributes: id, consumption rate, and power consumption (initialized to 0). Upon creation, it sets the type as "Car." The `move` method updates the accumulative distance and calculates power consumption as: `power consumption = distance / consumption rate`

If speed is 0, no update occurs. The `printInfo` method extends *Vehicle’s* `printInfo` by also displaying total power consumption.

The **Bicycle** class is another concrete subclass of **Vehicle**, setting its type as "Bicycle" upon creation. Its `move` method updates the accumulative distance unless the speed is 0, in which case no changes occur.

The provided outline includes a structural guide. **Do not modify the VehicleTester class**, as changes may cause errors.

### For example:

|**Input**|**Result**|
|:---|:---|
|Alice<br>101 Toyota Corolla White 20<br>Giant Escape Black<br>60 100<br>15 30|Owner: Alice<br>--- Vehicle Info ---<br>Type: Car<br>Brand: Toyota<br>Model: Corolla<br>Color: White<br>Accumulative Distance: 100<br>Power Consumption: 5<br>--- Vehicle Info ---<br>Type: Bicycle<br>Brand: Giant<br>Model: Escape<br>Color: Black<br>Accumulative Distance: 30|

### Starter Code

```java
import java.util.ArrayList;
import java.util.Scanner;

public class VehicleTester {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		String ownerName = sc.next();

		int carId = sc.nextInt();
		String carBrand = sc.next();
		String carModel = sc.next();
		String carColor = sc.next();
		int consumptionRate = sc.nextInt();

		String bicycleBrand = sc.next();
		String bicycleModel = sc.next();
		String bicycleColor = sc.next();

		int carSpeed = sc.nextInt();
		int carDistance = sc.nextInt();
		int bicycleSpeed = sc.nextInt();
		int bicycleDistance = sc.nextInt();

		Owner owner = new Owner(ownerName);
		Car car = new Car(carBrand, carId, carModel, carColor, consumptionRate);
		Bicycle bicycle = new Bicycle(bicycleBrand, bicycleModel, bicycleColor);

		owner.addVehicle(car);
		owner.addVehicle(bicycle);

		owner.move(car, carSpeed, carDistance);
		owner.move(bicycle, bicycleSpeed, bicycleDistance);

		System.out.println("Owner: " + owner.getName());
		car.printInfo();
		bicycle.printInfo();
	}
}

```