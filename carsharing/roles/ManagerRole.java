package carsharing.roles;

import carsharing.app.App;
import carsharing.car.Car;
import carsharing.car.CarDao;
import carsharing.car.CarDaoImpl;
import carsharing.company.Company;
import carsharing.company.CompanyDao;
import carsharing.company.CompanyDaoImpl;

import java.util.List;
import java.util.Scanner;

public class ManagerRole {
    private final CarDao carDao;
    private final CompanyDao companyDao;
    private final Scanner scan;

    public ManagerRole(String pathToDb) {
        carDao = new CarDaoImpl(pathToDb);
        companyDao = new CompanyDaoImpl(pathToDb);
        scan = new Scanner(System.in);
    }

    // allows create company and show list of companies
    public void loginAsManager() {
        int action;
        do {
            printManagerMenu();
            action = scan.nextInt();
            scan.nextLine();
            System.out.println();
            switch (action) {
                // exit
                case 0:
                    break;
                // company list
                case 1:
                    companyMenu();
                    break;
                // create a company
                case 2:
                    createCompany();
            }
        } while (action != 0);
    }

    // allows pick a company and manage it
    private void companyMenu() {
        List<Company> companies = companyDao.getCompanies();
        boolean flag = true;
        if (companies.size() == 0) {
            System.out.println("The company list is empty!\n");
        } else {
            while (flag) {
                printCompanies(companies);
                int companyId = scan.nextInt();
                System.out.println();
                if (companyId == 0) {
                    flag = false;
                } else if (companyId < 1 || companyId > companies.size()) {
                    System.out.println("No such company!");
                } else {
                    manageCompany(companies.get(companyId - 1));
                    flag = false;
                }
            }
        }
    }

    // manages certain company. For example shows car list and adds cars
    private void manageCompany(Company company) {
        int companyId = companyDao.getCompanyId(company.getName());
        if (companyId == -1) {
            System.out.println("There is a big problem id equals -1");
            return;
        }
        System.out.printf("'%s' company \n", company.getName());
        boolean flag = true;
        while (flag) {
            printCarMenu();
            int action = scan.nextInt();
            scan.nextLine();
            System.out.println();
            switch (action) {
                // exit
                case 0:
                    flag = false;
                    break;
                case 1:
                    printCars(companyId);
                    break;
                case 2:
                    createCar(companyId);
                    break;
                default:
                    System.out.println("No such option");
            }
        }
    }

    private void createCar(int companyId) {
        System.out.println("Enter the car name:");
        String carName = scan.nextLine();
        carDao.createCar(companyId, carName);
        System.out.println("The car was added!\n");
    }

    private void printCars(int companyId) {
        List<Car> cars = carDao.getCars(companyId);
        if (cars.size() == 0) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            int index = 0;
            for (var i : cars) {
                System.out.println(++index + ". " + i.getName());
            }
        }
        System.out.println();
    }

    private void printCompanies(List<Company> companies) {
        System.out.println("Choose the company:");
        int index = 0;
        for (var i : companies) {
            System.out.println(++index + ". " + i.getName());
        }
        System.out.println(App.BACK);
    }

    private void createCompany() {
        System.out.println("Enter the company name:");
        String name = scan.nextLine();
        companyDao.createCompany(name);
        System.out.println("The company was created!\n");
    }


    private static void printManagerMenu() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
    }

    private static void printCarMenu() {
        System.out.println("1. Car list\n" +
                "2. Create a car\n" +
                "0. Back");
    }
}
