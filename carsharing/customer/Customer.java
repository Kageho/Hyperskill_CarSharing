package carsharing.customer;

import carsharing.car.Car;
import carsharing.company.Company;

public class Customer {
    private final String name;
    private Car car;
    private Company company;
    private boolean hasCar;

    public Customer(String name) {
        this.name = name;
        this.hasCar = false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Customer)) {
            return false;
        } else {
            Customer customer = (Customer) obj;
            return name.equals(customer.name);
        }
    }

    public boolean hasCar() {
        return hasCar;
    }

    public void setHasCar(boolean hasCar) {
        this.hasCar = hasCar;
    }

    public String getName() {
        return name;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
