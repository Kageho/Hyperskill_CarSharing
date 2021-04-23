package carsharing.app;

import carsharing.dbInteractoin.Interaction;

import java.util.Scanner;

public class App {
    private Interaction interactionWithDb;
    private static final String mainMenu = "1. Log in as a manager\n0. Exit";
    private static final String manageMenu = "1. Company list\n" +
            "2. Create a company\n" +
            "0. Back";

    public App(String[] args) {
        interactionWithDb = new Interaction(args);
    }

    public void run() {
        interactionWithDb.createDB();
        Scanner scan = new Scanner(System.in);
        boolean mainFlag = true;
        while (mainFlag) {
            printMainMenu();
            int mainAct = scan.nextInt();
            System.out.println();
            switch (mainAct) {
                // log in as a manger
                case 1:
                    boolean manageFlag = true;
                    // manger loop
                    while (manageFlag) {
                        printManageMenu();
                        int manageAct = scan.nextInt();
                        System.out.println();
                        switch (manageAct) {
                            case 1:
                                // prints all companies
                                interactionWithDb.getCompanies();
                                System.out.println();
                                break;
                            case 2:
                                // adding a new company
                                scan.nextLine();
                                System.out.println("Enter the company name:");
                                interactionWithDb.insertCompany(scan.nextLine());
                                System.out.println("The company was created!\n");
                                break;
                            case 0:
                                manageFlag = false;
                                break;
                            default:
                                System.out.println("there is no such option");
                        }
                    }
                    break;
                case 0:
                    mainFlag = false;
            }
        }
    }

    private void printMainMenu() {
        System.out.println(mainMenu);
    }

    private void printManageMenu() {
        System.out.println(manageMenu);
    }
}
