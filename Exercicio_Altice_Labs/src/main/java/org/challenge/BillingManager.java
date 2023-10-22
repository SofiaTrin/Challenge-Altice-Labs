package org.challenge;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Given a charging request, this class has methods to determine the charging reply,
 * update the billing account, show the usage and costs of a specific client.
 * As well as, the creation of billing accounts and charging requests.
 */
public class BillingManager {
    private HashMap<String, BillingAccount> billingAccounts;
    private HashSet<ClientDataRecord> clientDataRecords;
    private final Eligibility e;
    private final Rating r;
    private final Charging c;

    public BillingManager() {
        this.billingAccounts = new HashMap<String, BillingAccount>();
        this.clientDataRecords = new HashSet<ClientDataRecord>();
        this.e = new Eligibility();
        this.r = new Rating();
        this.c = new Charging();
    }

    public BillingManager(HashMap<String, BillingAccount> billingAccounts, HashSet<ClientDataRecord> clientDataRecords) {
        this.billingAccounts = billingAccounts;
        this.clientDataRecords = clientDataRecords;
        this.e = new Eligibility();
        this.r = new Rating();
        this.c = new Charging();
    }

    public BillingAccount getBillingAccount(String phoneNumber) {
        return billingAccounts.get(phoneNumber);
    }

    public HashMap<String, BillingAccount> getBillingAccounts() {
        return billingAccounts;
    }

    public void setBillingAccounts(HashMap<String, BillingAccount> billingAccounts) {
        this.billingAccounts = billingAccounts;
    }

    public HashSet<ClientDataRecord> getClientDataRecords() {
        return clientDataRecords;
    }

    public void setClientDataRecords(HashSet<ClientDataRecord> clientDataRecords) {
        this.clientDataRecords = clientDataRecords;
    }

    /**
     * Method that returns a charging reply given a charging request.
     * It verifies the eligibility, cost and discounts, and the bucket to be charged.
     * And it updates the billing account and adds data record to the client data record list.
     *
     * @param chargingRequest charging request
     * @return charging reply
     */
    public ChargingReply determineChargingReply(ChargingRequest chargingRequest) {
        BillingAccount billingAccount = getBillingAccount(chargingRequest.getMsidn());
        Service service = chargingRequest.getService();
        Tariff tariff;
        double totalPrice = 0;
        Charging.ChargingStatus bucket = Charging.ChargingStatus.BUCKET_A;
        Eligibility.ElegStatus elegStatus = Eligibility.ElegStatus.OK;

        // When phone number doesn't exist -- not eligible
        if (billingAccount == null) {
            elegStatus = Eligibility.ElegStatus.NOT_ELIGIBLE;
            System.out.println("\nBilling account of phone number " + chargingRequest.getMsidn() + " doesn't exist.");
            return new ChargingReply(chargingRequest.getRequestId(), elegStatus, 0);
        }

        // Get client' service tariff
        if (service.equals(Service.A))
            tariff = billingAccount.getServiceATariff();
        else
            tariff = billingAccount.getServiceBTariff();

        // Determine eligibility -- OK (true) or Rejected (false)
        boolean result = e.determineEligibility(tariff, chargingRequest.getTimestamp(),
                chargingRequest.isRoaming(), billingAccount.getCounterA(), billingAccount.getBucketB(),
                billingAccount.getBucketC());


        if (!result) {
            elegStatus = Eligibility.ElegStatus.NOT_ELIGIBLE;
        } else {
            // Calculate rating price
            if (tariff.equals(Tariff.ALPHA1)) {
                totalPrice = r.determineAlpha1Rating(chargingRequest.getRequestedServiceUnit(),
                        chargingRequest.isRoaming(), chargingRequest.getTimestamp(), billingAccount.getCounterA(), billingAccount.getBucketC());
            } else if (tariff.equals(Tariff.ALPHA2)) {
                totalPrice = r.determineAlpha2Rating(chargingRequest.getRequestedServiceUnit(),
                        chargingRequest.isRoaming(), chargingRequest.getTimestamp(), billingAccount.getCounterB(), billingAccount.getBucketB());
            } else if (tariff.equals(Tariff.ALPHA3)) {
                totalPrice = r.determineAlpha3Rating(chargingRequest.getRequestedServiceUnit(), chargingRequest.getTimestamp(), billingAccount.getCounterB(), billingAccount.getBucketB());
            } else if (tariff.equals(Tariff.BETA1)) {
                totalPrice = r.determineBeta1Rating(chargingRequest.getRequestedServiceUnit(), chargingRequest.getTimestamp(), chargingRequest.isRoaming(), billingAccount.getCounterA(), billingAccount.getBucketC());
            } else if (tariff.equals(Tariff.BETA2)) {
                totalPrice = r.determineBeta2Rating(chargingRequest.getRequestedServiceUnit(), chargingRequest.getTimestamp(), chargingRequest.isRoaming(), billingAccount.getCounterB(), billingAccount.getBucketB());
            } else {
                totalPrice = r.determineBeta3Rating(chargingRequest.getRequestedServiceUnit(), chargingRequest.getTimestamp(), billingAccount.getCounterB(), billingAccount.getBucketB());
            }

            bucket = c.determineBucketToCharge(tariff, chargingRequest.isRoaming(), billingAccount.getCounterB());

            // Choose bucket to charge
            double bucketValue;
            if (bucket.equals(Charging.ChargingStatus.BUCKET_A))
                bucketValue = billingAccount.getBucketA();
            else if (bucket.equals(Charging.ChargingStatus.BUCKET_B))
                bucketValue = billingAccount.getBucketB();
            else
                bucketValue = billingAccount.getBucketC();

            // Verify if bucket has enough money
            if (totalPrice > bucketValue) {
                elegStatus = Eligibility.ElegStatus.CREDIT_LIMIT_REACHED;
            }
        }

        // Update billing account parameters
        updateBillingAccount(chargingRequest.getMsidn(),
                chargingRequest.getRequestedServiceUnit(),
                chargingRequest.isRoaming(), chargingRequest.getTimestamp(), bucket, tariff, totalPrice, elegStatus);

        // Create charging reply object
        ChargingReply chargingReply = createChargingReply(chargingRequest.getRequestId(), elegStatus, chargingRequest.getRequestedServiceUnit());
        // Create client data record and add it to the list
        ClientDataRecord clientDataRecord = createClientDataRecord(chargingRequest.getTimestamp(),
                chargingRequest.getMsidn(), service, chargingRequest, chargingReply, billingAccount.getBucketA(), billingAccount.getBucketB(), billingAccount.getBucketC(), billingAccount.getCounterA(), billingAccount.getCounterB(), billingAccount.getCounterC(), billingAccount.getCounterD(), tariff, totalPrice);
        clientDataRecords.add(clientDataRecord);
        return chargingReply;
    }

    /**
     * Creates the charging reply object with the correct given service units (gsu).
     *
     * @param requestId charging request id
     * @param eligibility result of the charging request
     * @param rsu request service units value
     * @return charging reply
     */
    public ChargingReply createChargingReply(String requestId, Eligibility.ElegStatus eligibility, int rsu) {
        int gsu = 0;
        if (eligibility.equals(Eligibility.ElegStatus.OK)) {
            gsu = rsu;
        }
        return new ChargingReply(requestId, eligibility, gsu);
    }

    /**
     * Updates the billing account parameters when a new charging request is created
     *
     * @param phoneNumber client msisdn
     * @param rsu request service units
     * @param roaming if it's local or roaming
     * @param requestDate charging request date
     * @param bucket which bucket the cost is going to be charged
     * @param tariff tariff of a service
     * @param totalPrice cost of the charging
     * @param eligibility result of charging request
     */
    public void updateBillingAccount(String phoneNumber, int rsu, boolean roaming, Calendar requestDate, Charging.ChargingStatus bucket, Tariff tariff, double totalPrice, Eligibility.ElegStatus eligibility) {
        BillingAccount billingAccount = getBillingAccount(phoneNumber);

        // Update the request date even when it's not eligible
        billingAccount.setCounterD(requestDate);

        if (eligibility.equals(Eligibility.ElegStatus.OK)) {
            if (bucket.equals(Charging.ChargingStatus.BUCKET_A)) {
                billingAccount.setBucketA(billingAccount.getBucketA() - totalPrice);
                billingAccount.setCounterA(billingAccount.getCounterA() + rsu);
            } else if (bucket.equals(Charging.ChargingStatus.BUCKET_B)) {
                billingAccount.setBucketB(billingAccount.getBucketB() - totalPrice);
                if (tariff.equals(Tariff.BETA1)) {
                    billingAccount.setCounterB(billingAccount.getCounterB() + 1);
                }
            } else {
                billingAccount.setBucketC(billingAccount.getBucketC() - totalPrice);
            }

            if (roaming) {
                billingAccount.setCounterC(billingAccount.getCounterC() + 1);
            }
        }

        // Update billingAccount object
        billingAccounts.put(phoneNumber, billingAccount);
    }

    public ClientDataRecord createClientDataRecord(Calendar requestDate, String phoneNumber, Service service,
                                                   ChargingRequest chargingRequest, ChargingReply chargingReply,
                                                   double bucketA, double bucketB, double bucketC,
                                                   int counterA, int counterB, int counterC, Calendar counterD,
                                                   Tariff tariff, double totalPrice) {
        return new ClientDataRecord(requestDate, phoneNumber, service, chargingRequest, chargingReply, bucketA, bucketB, bucketC, counterA, counterB, counterC, counterD, tariff, totalPrice);
    }

    /**
     * Method that shows data records of specific client.
     *
     * @param phoneNumber client msisdn
     */
    public void showClientDataRecords(String phoneNumber) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<ClientDataRecord> sortedClientDataRecords = clientDataRecords.stream()
                .filter(clientDataRecord -> clientDataRecord.getMsisdn().equals(phoneNumber))
                .sorted(Comparator.comparing(ClientDataRecord::getTimestamp))
                .toList();

        if (sortedClientDataRecords.isEmpty()) {
            System.out.println("\nThe client of this phone number doesn't have any data record list.");
        } else {
            System.out.println("\n=== Data record list of client phone number " + phoneNumber + " ===\n");
            System.out.format("%25s%15s%15s%15s%15s%25s%15s%15s%15s%15s%15s%15s%25s%n", "Timestamp", "Phone Number", "Service Id", "Roaming", "RSU", "Result", "Bucket1", "Bucket2", "Bucket3", "CounterA", "CounterB", "CounterC", "CounterD");

            sortedClientDataRecords.forEach( clientDataRecord -> System.out.format("%25s%15s%15s%15s%15s%25s%15s%15s%15s%15s%15s%15s%25s%n", format.format(clientDataRecord.getTimestamp().getTime()), clientDataRecord.getMsisdn(), clientDataRecord.getService(), clientDataRecord.getChargingRequest().isRoaming(), clientDataRecord.getChargingRequest().getRequestedServiceUnit(), clientDataRecord.getChargingReply().getResult(), clientDataRecord.getBucketA(), clientDataRecord.getBucketB(), clientDataRecord.getBucketC(), clientDataRecord.getCounterA(), clientDataRecord.getCounterB(), clientDataRecord.getCounterC(), format.format(clientDataRecord.getCounterD().getTime())));

            showClientExpenses(phoneNumber);
        }
    }

    /**
     * Shows the value of all expenses of a specific client.
     *
     * @param phoneNumber client msisdn
     */
    public void showClientExpenses(String phoneNumber) {
        double sumPrice = clientDataRecords.stream()
                .filter(clientDataRecord -> clientDataRecord.getMsisdn().equals(phoneNumber))
                .mapToDouble(ClientDataRecord::getTotalPrice).sum();

        System.out.println("\n=== Client costs: " + sumPrice + " ====\n");
    }

    /**
     * Creates a billing account object, if all validations is met
     *
     * @param phoneNumber msisdn
     * @param bucket1 bucket1 value
     * @param bucket2 bucket2 value
     * @param bucket3 bucket3 value
     * @param serviceATariff service A tariff
     * @param serviceBTariff service B tariff
     * @return billing account
     */
    public BillingAccount createBillingAccount(String phoneNumber, String bucket1, String bucket2, String bucket3, String serviceATariff, String serviceBTariff) {
        // Validate Buckets
        double bucket1Value, bucket2Value, bucket3Value;
        try {
            bucket1Value = Double.parseDouble(bucket1);
            bucket2Value = Double.parseDouble(bucket2);
            bucket3Value = Double.parseDouble(bucket3);
        } catch (NumberFormatException e) {
            System.out.println("\nOne of the buckets has an invalid value. It was not possible to create a billing account.");
            return null;
        }
        // Validate Phone Number
        if (!Pattern.compile("^(\\+?351)?9\\d\\d{7}$").matcher(phoneNumber).matches()) {
            System.out.println("\nInvalid phone number. It was not possible to create a billing account.");
            return null;
        }
        // Validate Service A Tariff
        Tariff tariffA, tariffB;
        if (serviceATariff.equalsIgnoreCase("ALPHA1")) {
            tariffA = Tariff.ALPHA1;
        } else if (serviceATariff.equalsIgnoreCase("ALPHA2")) {
            tariffA = Tariff.ALPHA2;
        } else if (serviceATariff.equalsIgnoreCase("ALPHA3")) {
            tariffA = Tariff.ALPHA3;
        } else {
            System.out.println("\nInvalid Service A Tariff. It was not possible to create a billing account.");
            return null;
        }
        // Validate Service B Tariff
        if (serviceBTariff.equalsIgnoreCase("BETA1")) {
            tariffB = Tariff.BETA1;
        } else if (serviceBTariff.equalsIgnoreCase("BETA2")) {
            tariffB = Tariff.BETA2;
        } else if (serviceBTariff.equalsIgnoreCase("BETA3")) {
            tariffB = Tariff.BETA3;
        } else {
            System.out.println("\nInvalid Service B Tariff. It was not possible to create a billing account.");
            return null;
        }

        BillingAccount billingAccount = new BillingAccount(phoneNumber, bucket1Value, bucket2Value, bucket3Value, tariffA, tariffB);
        this.billingAccounts.put(phoneNumber, billingAccount);
        return billingAccount;
    }

    /**
     * Creates charging request object if all validations are OK
     *
     * @param service service
     * @param roaming if it is local or roaming
     * @param phoneNumber msisdn
     * @param rsu request service units
     * @return charging request
     */
    public ChargingRequest createChargingRequest(String service, String roaming, String phoneNumber, String rsu) {
        // Validate Service
        Service serviceValue;
        if (Pattern.compile("^[AaBb]$").matcher(service).matches()) {
            if (service.equalsIgnoreCase("A")) {
                serviceValue = Service.A;
            } else {
                serviceValue = Service.B;
            }
        } else {
            System.out.println("\nInvalid roaming value (0 or 1). It was not possible to create a charging request.");
            return null;
        }
        // Validate Roaming
        boolean roamingValue;
        if (Pattern.compile("^[01]$").matcher(roaming).matches()) {
            roamingValue = !roaming.equals("0");
        } else {
            System.out.println("\nInvalid roaming value (0 or 1). It was not possible to create a charging request.");
            return null;
        }
        // Validate Phone Number
        if (!Pattern.compile("^(\\+?351)?9\\d\\d{7}$").matcher(phoneNumber).matches()) {
            System.out.println("\nInvalid phone number. It was not possible to create a charging request.");
            return null;
        }
        // Validate Request Service Units (RSU)
        int rsuValue;
        try {
            rsuValue = Integer.parseInt(rsu);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid request service units. It was not possible to create a charging request.");
            return null;
        }
        return new ChargingRequest(serviceValue, roamingValue, phoneNumber, rsuValue);
    }
}
