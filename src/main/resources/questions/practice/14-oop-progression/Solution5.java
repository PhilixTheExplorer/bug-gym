import java.util.*;

public class Solution5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String fName = sc.next();
        double fanWatt = sc.nextDouble();
        double fanHours = sc.nextDouble();

        String acName = sc.next();
        double acBtu = sc.nextDouble();
        double acHours = sc.nextDouble();

        Device d1 = new Fan(fName, fanWatt);
        Device d2 = new AirConditioner(acName, acBtu);

        d1.use(fanHours);
        d2.use(acHours);

        System.out.println(d1.getName() + " " + d1.energy());
        System.out.println(d2.getName() + " " + d2.energy());
        System.out.println("Total " + (d1.energy() + d2.energy()));

        sc.close();
    }
}

abstract class Device {
    private String name;
    private double hoursUsed;

    public Device(String name) {
        this.name = name;
        this.hoursUsed = 0;
    }

    public String getName() {
        return name;
    }

    public double getHoursUsed() {
        return hoursUsed;
    }

    public void use(double h) {
        if (h > 0) {
            hoursUsed += h;
        }
    }

    public abstract double energy();
}

class Fan extends Device {
    private double watt;

    public Fan(String name, double watt) {
        super(name);
        this.watt = watt;
    }

    @Override
    public double energy() {
        return (watt * getHoursUsed()) / 1000.0;
    }
}

class AirConditioner extends Device {
    private double btu;

    public AirConditioner(String name, double btu) {
        super(name);
        this.btu = btu;
    }

    @Override
    public double energy() {
        return (btu * getHoursUsed()) / 12000.0;
    }
}
