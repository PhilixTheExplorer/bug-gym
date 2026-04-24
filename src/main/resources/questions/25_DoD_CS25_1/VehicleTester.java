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

        sc.close();
    }
}

class Owner {
    private String name;
    private ArrayList<Vehicle> vehicles;

    public Owner(String name) {
        this.name = name;
        this.vehicles = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        if (!vehicles.contains(vehicle)) {
            vehicles.add(vehicle);
        }
    }

    public void move(Vehicle vehicle, int speed, int distance) {
        if (vehicles.contains(vehicle)) {
            vehicle.move(speed, distance);
        }
    }
}

abstract class Vehicle {
    private String brand;
    private String type;
    private String model;
    private String color;
    private int accumulative_distance;

    public Vehicle(String brand, String type, String model, String color) {
        this.brand = brand;
        this.type = type;
        this.model = model;
        this.color = color;
        this.accumulative_distance = 0;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAccumulative_distance() {
        return accumulative_distance;
    }

    public void setAccumulative_distance(int accumulative_distance) {
        this.accumulative_distance = accumulative_distance;
    }

    public void printInfo() {
        System.out.println("--- Vehicle Info ---");
        System.out.println("Type: " + type
                + "\nBrand: " + brand
                + "\nModel: " + model
                + "\nColor: " + color
                + "\nAccumulative Distance: " + accumulative_distance);
    }

    public abstract void move(int speed, int distance);
}

class Car extends Vehicle {
    private int id;
    private int consumption_rate;
    private int power_consumption;

    public Car(String brand, int id, String model, String color, int consumption_rate) {
        super(brand, "Car", model, color);
        this.id = id;
        this.consumption_rate = consumption_rate;
        this.power_consumption = 0;
    }

    public int getId() {
        return id;
    }

    public int getConsumption_rate() {
        return consumption_rate;
    }

    public void setConsumption_rate(int consumption_rate) {
        this.consumption_rate = consumption_rate;
    }

    public int getPower_consumption() {
        return power_consumption;
    }

    @Override
    public void move(int speed, int distance) {
        if (speed == 0) {
            return;
        }
        setAccumulative_distance(getAccumulative_distance() + distance);
        power_consumption += distance / consumption_rate;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Power Consumption: " + power_consumption);
    }
}

class Bicycle extends Vehicle {
    public Bicycle(String brand, String model, String color) {
        super(brand, "Bicycle", model, color);
    }

    @Override
    public void move(int speed, int distance) {
        if (speed == 0) {
            return;
        }
        setAccumulative_distance(getAccumulative_distance() + distance);
    }
}
