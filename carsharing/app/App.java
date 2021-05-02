package carsharing.app;


import carsharing.customer.CustomerDao;
import carsharing.customer.CustomerDaoImpl;
import carsharing.roles.CustomerRole;
import carsharing.roles.ManagerRole;
import carsharing.storage.Storage;

import java.util.Scanner;

public class App {

    public static final String BACK = "0. Back";
    private final Scanner scan;
    private final CustomerDao customerDao;
    private final String pathToDb;

    public App(String[] args) {
        if (args.length > 1) {
            pathToDb = args[1];
        } else {
            pathToDb = "default name1";
        }
        // initializing db
        Storage storage = new Storage(pathToDb);
        storage.createTables();

        // object is used to create customers
        customerDao = new CustomerDaoImpl(pathToDb);

        scan = new Scanner(System.in);
    }

    public void run() {
        ManagerRole manager = new ManagerRole(pathToDb);
        CustomerRole customer = new CustomerRole(pathToDb);

        int action;
        do {
            printMainMenu();
            action = scan.nextInt();
            scan.nextLine();
            System.out.println();
            switch (action) {
                // exit
                case 0:
                    break;
                // login as a manager
                case 1:
                    manager.loginAsManager();
                    break;
                // login as a customer
                case 2:
                    customer.logInAsCustomer();
                    break;
                case 3:
                    // create customer
                    createCustomer();
                    break;
                default:
                    System.out.println("No such option");
            }
        } while (action != 0);
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scan.nextLine();
        customerDao.createCustomer(name);
        System.out.println("The customer was added!\n");
    }

    private static void printMainMenu() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
    }
}
