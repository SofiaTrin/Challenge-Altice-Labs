package org.challenge;

import java.util.Scanner;

/**
 * Provides an interface to use the application. The user has four option to use:
 * create billing account, create charging request and get charging reply,
 * access client data records and exit.
 */
public class Menu {

    public Menu() {
    }

    public void menu(BillingManager billingManager) throws NumberFormatException {
        Scanner sc = new Scanner(System.in);
        int option;

        System.out.println("**** Welcome! ****");

        do {
            System.out.println("\n=== MENU ===");
            System.out.print("\n(1) Create Billing Account\n(2) Create Charging Request and Get Charging Reply\n(3) Access Client Data Records\n(4) Exit");
            System.out.print("\nChoose an option: ");

            option = validateOption(sc, 4);

            switch (option) {
                case 1 -> createBillingAccountOption(billingManager);
                case 2 -> createChargingRequestOption(billingManager);
                case 3 -> showClientDataRecords(billingManager);
            }

        } while (option != 4);
        System.out.println("\n== Thank you! ==");
    }

    public int validateOption(Scanner sc, int max) {
        boolean validatedOption = false;
        int option = 4;

        do {
            String input = sc.next();
            try {
                option = Integer.parseInt(input);
                if (option >= 1 && option <= max)
                    validatedOption = true;
                else
                    System.out.print("Invalid Option. Try again: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid Option. Try again: ");
            }
        } while (!validatedOption);

        return option;
    }

    public void createBillingAccountOption(BillingManager billingManager) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Insert Phone Number: ");
        String phoneNumber = sc.next();
        System.out.print("Insert Bucket 1 Value (in cents): ");
        String bucket1Value = sc.next();
        System.out.print("Insert Bucket 2 Value (in cents): ");
        String bucket2Value = sc.next();
        System.out.print("Insert Bucket 3 Value (in cents): ");
        String bucket3Value = sc.next();
        System.out.print("Insert Service A Tariff (ALPHA1, ALPHA2, ALPHA3): ");
        String serviceATariff = sc.next();
        System.out.print("Insert Service B Tariff (BETA1, BETA2, BETA3): ");
        String serviceBTariff = sc.next();

        BillingAccount billingAccount = billingManager.createBillingAccount(phoneNumber, bucket1Value, bucket2Value, bucket3Value, serviceATariff, serviceBTariff);
        if (billingAccount != null) {
            System.out.println("\nBilling Account created with success.");
        }
    }

    public void createChargingRequestOption(BillingManager billingManager) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Insert Phone Number: ");
        String phoneNumber = sc.next();
        System.out.print("Insert Service -- A or B: ");
        String serviceValue = sc.next();
        System.out.print("Insert Roaming -- 0 (false) or 1 (true): ");
        String roamingValue = sc.next();
        System.out.print("Insert Bucket Request Service Units: ");
        String rsuValue = sc.next();

        ChargingRequest chargingRequest = billingManager.createChargingRequest(serviceValue, roamingValue, phoneNumber, rsuValue);
        if (chargingRequest != null) {
            ChargingReply chargingReply = billingManager.determineChargingReply(chargingRequest);
        }
    }

    public void showClientDataRecords(BillingManager billingManager) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Insert Phone Number: ");
        String phoneNumber = sc.next();

        billingManager.showClientDataRecords(phoneNumber);
    }
}
