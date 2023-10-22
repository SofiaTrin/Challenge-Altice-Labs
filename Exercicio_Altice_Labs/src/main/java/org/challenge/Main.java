package org.challenge;

/**
 * Menu Options:
 * 1 - Create Billing Account
 * 2 - Create Charging Request and Get Charging Reply
 * 3 - Access Client Data Records
 * 4 - Exit
 * <p>
 * The data classes are: BillingAccount, ChargingReply, ChargingRequest and ClientDataRecord
 * <p>
 * Eligibility and Charging are classes built with enums for theirs categories:
 * (Eligibility: OK, CREDIT_LIMIT_REACHED, NOT_ELIGIBLE;
 *  Charging: BUCKET_A, BUCKET_B, BUCKET_C)
 * The Eligibility class has a method to determine if the charging request is eligible and, if eligible,
 * Charging has a method to determine from which bucket the cost will be charged.
 * <p>
 * Tariff and Service are enums with the specific categories
 * (Tariff: ALPHA1, ALPHA2, ALPHA3, BETA1, BETA2, BETA3; Service: A, B)
 * <p>
 * The Billing Manager has the main methods of the application, such as:
 * - determineChargingReply -- given a charging request returns a charging reply
 * - updateBillingAccount -- updates billing account parameters
 * - showClientDataRecords -- presents data records of specific phone number
 * - showClientExpenses -- presents client expenses
 * - createClientDataRecord -- creates a ClientDataRecord object
 * - createChargingReply -- creates a ChargingReply object
 * - createBillingAccount -- creates a BillingAccount object
 * - createChargingRequest -- creates a ChargingRequest object
 */

public class Main {
    public static void main(String[] args) {
        BillingManager billingManager = new BillingManager();
        Menu menu = new Menu();

        menu.menu(billingManager);
    }
}
