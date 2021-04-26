package carsharing.app;

import carsharing.dbInteractoin.Interaction;

import java.util.Scanner;

public class App {
    private Interaction interactionWithDb;
    private static final String mainMenu = "1. Log in as a manager\n0. Exit";
    private static final String manageMenu = "1. Company list\n" +
            "2. Create a company\n" +
            "0. Back";
    private static final String carMenu = "1. Car list\n" +
            "2. Create a car\n" +
            "0. Back";
    private Scanner scan;

    public App(String[] args) {
        interactionWithDb = new Interaction(args);
        scan = new Scanner(System.in);
    }

    public void run() {
        boolean falg = true;
        while (falg) {
            if (mainMenu()) {
                do {
                    printManagerMenu();
                } while (managerMenu());
            } else {
                falg = false;
            }
        }
    }

    private boolean mainMenu() {
        printMainMenu();
        return scan.nextInt() == 1;
    }

    private boolean managerMenu() {
        int action = scan.nextInt();
        scan.nextLine(); // cleaning the line
        boolean flag = false;

        switch (action) {
            // print all companies
            case 1:
                if (!interactionWithDb.getCompanies()) {
                    hiddenCarMenu();
                }
                flag = true;
                break;
            // adding a company
            case 2:
                System.out.println("Enter the company name:");
                interactionWithDb.insertCompany(scan.nextLine());
                System.out.println("The company was created!");
                flag = true;
                break;
            case 0:
            default:
                break;
        }
        return flag;
    }

    // car menu
    private void hiddenCarMenu() {
        int companyNum = scan.nextInt();
        // cleaning the input
        scan.nextLine();
        boolean flag = true;
        while (flag && companyNum != 0) {
            if (interactionWithDb.isCompanyPresent(companyNum)) {
                boolean flag1 = true;
                while (flag1) {
                    printCarMenu();
                    int option = scan.nextInt();
                    scan.nextLine();
                    switch (option) {
                        // printing company's cars
                        case 1:
                            interactionWithDb.getCars(companyNum);
                            break;
                        case 2:
                            // adding new car
                            System.out.println("Enter the car name:");
                            interactionWithDb.insertCar(scan.nextLine(), companyNum);
                            System.out.println("The car was added!");
                            break;
                        default:
                            flag1 = false;
                            flag = false;
                            break;
                    }
                }
            } else {
                flag = false;
            }
        }
    }

    private void printMainMenu() {
        System.out.println(mainMenu);
    }

    private void printManagerMenu() {
        System.out.println(manageMenu);
    }

    private void printCarMenu() {
        System.out.println(carMenu);
    }
}
