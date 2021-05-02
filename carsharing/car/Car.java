package carsharing.car;

public class Car {
    private final String name;

    public Car(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Car)) {
            return false;
        } else {
            Car car = (Car) obj;
            return name.equals(car.name);
        }
    }
}
