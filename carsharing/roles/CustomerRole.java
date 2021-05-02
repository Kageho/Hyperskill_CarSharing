package carsharing.roles;

import carsharing.app.App;
import carsharing.car.Car;
import carsharing.company.Company;
import carsharing.company.CompanyDao;
import carsharing.company.CompanyDaoImpl;
import carsharing.customer.Customer;
import carsharing.customer.CustomerDao;
import carsharing.customer.CustomerDaoImpl;

import java.util.List;
import java.util.Scanner;

public class CustomerRole {
    private final CustomerDao customerDao;
    private final Scanner scan;
    private final CompanyDao companyDao;

    public CustomerRole(String pathToDb) {
        this.customerDao = new CustomerDaoImpl(pathToDb);
        this.scan = new Scanner(System.in);
        this.companyDao = new CompanyDaoImpl(pathToDb);
    }

    public void logInAsCustomer() {
        List<Customer> customers = customerDao.getCustomers();
        if (customers.size() == 0) {
            System.out.println("The customer list is empty!\n");
        } else {
            boolean flag = true;
            while (flag) {
                printCustomers(customers);
                int customerId = scan.nextInt();
                System.out.println();
                if (customerId == 0) {
                    flag = false;
                    // log in as customer with customerId
                } else if (customerId > 0 && customerId <= customers.size()) {
                    clientActs(customerId);
                    flag = false;
                } else {
                    System.out.println("No such customer");
                }
            }
        }
    }

    // thing that client can do
    private void clientActs(int customerId) {
        boolean flag = true;
        while (flag) {
            printServices();
            int action = scan.nextInt();
            System.out.println();
            switch (action) {
                // exit
                case 0:
                    flag = false;
                    // rents car
                    break;
                case 1:
                    rentCar(customerId);
                    break;
                // returns car
                case 2:
                    returnRentedCar(customerId);
                    break;
                // prints info about rented car
                case 3:
                    getInfoAboutCar(customerId);
                    break;
                default:
                    System.out.println("No such service");
            }
        }
    }
// customer may rent a car if he doesn't rent any
    private void rentCar(int customerId) {
        Customer customer = customerDao.getInfoAboutRentedCar(customerId);
        if (customer == null) {
            System.out.println("There is no such customer");
        } else {
            if (customer.hasCar()) {
                System.out.println("You've already rented a car!");
            } else {
                List<Company> companies = companyDao.getCompanies();
                boolean flag = true;
                while (flag) {
                    int companyId = pickCompany(companies);
                    if (companyId > 0 && companyId <= companies.size()) {
                        pickCar(companies.get(companyId - 1), customerId);
                        flag = false;
                    } else if (companyId == 0) {
                        flag = false;
                    } else {
                        System.out.println("No such company!");
                    }
                }
            }
        }
        System.out.println();
    }

    private void pickCar(Company company, int customerId) {
        List<Car> cars = customerDao.getAvailableCars(company);
        if (cars.size() == 0) {
            System.out.printf("No available cars in the '%s' company\n", company.getName());
        } else {
            boolean flag = true;
            while (flag) {
                printCars(cars);
                int carId = scan.nextInt();
                System.out.println();
                if (carId == 0) {
                    flag = false;
                } else if (carId < 0 || carId > cars.size()) {
                    System.out.println("There is no such car\n");
                } else {
                    customerDao.rentCar(cars.get(carId - 1).getName(), customerId);
                    System.out.println("You rented '" + cars.get(carId - 1).getName() + "'");
                    flag = false;
                }
            }
        }
    }


    private int pickCompany(List<Company> companies) {
        int index = 0;
        if (companies.size() == 0) {
            System.out.println("Company list is empty!");
        } else {
            System.out.println("Choose a company:");
            int i = 0;
            for (var e : companies) {
                System.out.println(++i + ". " + e.getName());
            }
            System.out.println(App.BACK);
            index = scan.nextInt();
            System.out.println();
        }
        return index;
    }

    private void getInfoAboutCar(int customerId) {
        Customer customer = customerDao.getInfoAboutRentedCar(customerId);
        if (customer == null) {
            System.out.println("There is no such customer!");
        } else {
            if (customer.hasCar()) {
                System.out.println("Your rented car:");
                System.out.println(customer.getCar().getName());
                System.out.println("Company:");
                System.out.println(customer.getCompany().getName());
            } else {
                System.out.println("You didn't rent a car!");
            }
        }
        System.out.println();
    }

    private void returnRentedCar(int customerId) {
        Customer customer = customerDao.getInfoAboutRentedCar(customerId);
        if (customer == null) {
            System.out.println("There is no such customer");
        } else {
            if (customer.hasCar()) {
                customerDao.returnCar(customerId);
                System.out.println("You've returned a rented car!");
            } else {
                System.out.println("You didn't rent a car!");
            }

        }
        System.out.println();
    }

    private void printCars(List<Car> cars) {
        int index = 0;
        for (var i : cars) {
            System.out.println(++index + ". " + i.getName());
        }
        System.out.println(App.BACK);
    }

    private static void printServices() {
        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
    }

    private void printCustomers(List<Customer> customers) {
        System.out.println("Customer list:");
        int i = 0;
        for (var e : customers) {
            System.out.println(++i + ". " + e.getName());
        }
        System.out.println(App.BACK);
    }
}
