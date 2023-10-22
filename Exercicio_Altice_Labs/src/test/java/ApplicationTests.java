import org.challenge.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class with tests to check the main methods of the application.
 */
public class ApplicationTests {

    /**
     * Happy Path (Service A)
     * Test Case: call determineChargingReply method for the Alpha1 tariff.
     * Expected Result: Eligibility Status OK
     */
    @Test
    public void determineChargingReplyTestAlpha1EligibilityOk() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "911222111";
        // Create Billing Account
        billingManager.createBillingAccount(phoneNumber, "100", "100", "10", "ALPHA1", "BETA1");
        // Create Charging Request
        ChargingRequest chargingRequest = billingManager.createChargingRequest("A", "0", phoneNumber, "2");
        // Set timestamp as working day
        chargingRequest.setTimestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 23, 22, 30));

        // Get Charging Reply
        ChargingReply chargingReply = billingManager.determineChargingReply(chargingRequest);

        // Asserts
        Assert.assertEquals(chargingReply.getResult(), Eligibility.ElegStatus.OK);
        Assert.assertEquals(chargingReply.getGrantedServiceUnits(), 2);
        Assert.assertEquals(billingManager.getClientDataRecords().size(), 1);
    }

    /**
     * Test Case: call determineChargingReply method for the Alpha1 tariff. Bucket without enough money.
     * Expected Result: Eligibility Status Credit Limit Reached
     */
    @Test
    public void determineChargingReplyTestAlpha1EligibilityCreditLimitReached() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "911222111";
        // Create Billing Account
        billingManager.createBillingAccount(phoneNumber, "10", "100", "10", "ALPHA1", "BETA1");
        // Create Charging Request
        ChargingRequest chargingRequest = billingManager.createChargingRequest("A", "0", phoneNumber, "2");
        // Set timestamp as working day
        chargingRequest.setTimestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 23, 22, 30));

        // Get Charging Reply
        ChargingReply chargingReply = billingManager.determineChargingReply(chargingRequest);

        // Asserts
        Assert.assertEquals(chargingReply.getResult(), Eligibility.ElegStatus.CREDIT_LIMIT_REACHED);
        Assert.assertEquals(chargingReply.getGrantedServiceUnits(), 0);
        Assert.assertEquals(billingManager.getClientDataRecords().size(), 1);
    }

    /**
     * Test Case: call determineChargingReply method for the Alpha1 tariff. Weekend Day not eligible.
     * Expected Result: Eligibility Status Not Eligible
     */
    @Test
    public void determineChargingReplyTestAlpha1EligibilityNotEligible() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "911222111";
        // Create Billing Account
        billingManager.createBillingAccount(phoneNumber, "100", "100", "10", "ALPHA1", "BETA1");
        // Create Charging Request
        ChargingRequest chargingRequest = billingManager.createChargingRequest("A", "0", phoneNumber, "2");
        // Set timestamp as weekend day
        chargingRequest.setTimestamp(new GregorianCalendar(2023, Calendar.OCTOBER, 22, 22, 30));

        // Get Charging Reply
        ChargingReply chargingReply = billingManager.determineChargingReply(chargingRequest);

        // Asserts
        Assert.assertEquals(chargingReply.getResult(), Eligibility.ElegStatus.NOT_ELIGIBLE);
        Assert.assertEquals(chargingReply.getGrantedServiceUnits(), 0);
        Assert.assertEquals(billingManager.getClientDataRecords().size(), 1);
    }

    /**
     * Happy Path (Service B)
     * Test Case: call determineChargingReply method for the Beta2 tariff.
     * Expected Result: Eligibility Status OK
     */
    @Test
    public void determineChargingReplyTestBeta2EligibilityOk() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "911222111";

        // Create Billing Account
        billingManager.createBillingAccount(phoneNumber, "10", "100", "10", "ALPHA2", "BETA2");
        // Create Charging Request
        ChargingRequest chargingRequest = billingManager.createChargingRequest("B", "0", phoneNumber, "2");

        // Get Charging Reply
        ChargingReply chargingReply = billingManager.determineChargingReply(chargingRequest);

        // Asserts
        Assert.assertEquals(chargingReply.getResult(), Eligibility.ElegStatus.OK);
        Assert.assertEquals(chargingReply.getGrantedServiceUnits(), 2);
        Assert.assertEquals(billingManager.getClientDataRecords().size(), 1);
    }

    /**
     * Test Case: call determineChargingReply method for the Beta2 tariff. Bucket without enough money.
     * Expected Result: Eligibility Status Credit Limit Reached
     */
    @Test
    public void determineChargingReplyTestBeta2EligibilityCreditLimitReached() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "911222111";

        // Create Billing Account
        billingManager.createBillingAccount(phoneNumber, "10", "1", "10", "ALPHA2", "BETA2");
        // Create Charging Request
        ChargingRequest chargingRequest = billingManager.createChargingRequest("B", "0", phoneNumber, "2");

        // Get Charging Reply
        ChargingReply chargingReply = billingManager.determineChargingReply(chargingRequest);

        // Asserts
        Assert.assertEquals(chargingReply.getResult(), Eligibility.ElegStatus.CREDIT_LIMIT_REACHED);
        Assert.assertEquals(chargingReply.getGrantedServiceUnits(), 0);
        Assert.assertEquals(billingManager.getClientDataRecords().size(), 1);
    }

    /**
     * Test Case: call determineChargingReply method for the Beta2 tariff. Roaming and bucketB > 10 not Eligible.
     * Expected Result: Eligibility Status Not Eligible
     */
    @Test
    public void determineChargingReplyTestBeta2EligibilityNotEligible() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "911222112";

        // Create Billing Account
        billingManager.createBillingAccount(phoneNumber, "10", "15", "10", "ALPHA2", "BETA2");
        // Create Charging Request
        ChargingRequest chargingRequest = billingManager.createChargingRequest("B", "1", phoneNumber, "2");

        // Get Charging Reply
        ChargingReply chargingReply = billingManager.determineChargingReply(chargingRequest);

        // Asserts
        Assert.assertEquals(chargingReply.getResult(), Eligibility.ElegStatus.NOT_ELIGIBLE);
        Assert.assertEquals(chargingReply.getGrantedServiceUnits(), 0);
        Assert.assertEquals(billingManager.getClientDataRecords().size(), 1);
    }

    /**
     * Test Case: call determineChargingReply method. Client's phone number doesn't exist.
     * Expected Result: Eligibility Status Not Eligible
     */
    @Test
    public void determineChargingReplyTestBeta2WhenPhoneNumberDoesNotExist() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "911222112";

        // Create Billing Account
        ChargingRequest chargingRequest = billingManager.createChargingRequest("B", "1", phoneNumber, "2");
        // Get Charging Reply
        ChargingReply chargingReply = billingManager.determineChargingReply(chargingRequest);

        // Asserts
        Assert.assertEquals(chargingReply.getResult(), Eligibility.ElegStatus.NOT_ELIGIBLE);
        Assert.assertEquals(chargingReply.getGrantedServiceUnits(), 0);
        Assert.assertEquals(billingManager.getClientDataRecords().size(), 0);
    }

    /**
     * Test Case: call createBillingAccount with valid attributes.
     */
    @Test
    public void createBillingAccountValidAttributes() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "911222111";
        BillingAccount billingAccount = billingManager.createBillingAccount(phoneNumber, "10", "100", "10", "ALPHA2", "BETA2");

        Assert.assertNotNull(billingAccount);
        Assert.assertEquals(billingManager.getBillingAccounts().size(), 1);
        Assert.assertEquals(billingManager.getBillingAccounts().get(phoneNumber).getMsisdn(), phoneNumber);
        Assert.assertEquals(billingManager.getBillingAccounts().get(phoneNumber).getBucketA(), 10, 0.001);
        Assert.assertEquals(billingManager.getBillingAccounts().get(phoneNumber).getBucketB(), 100, 0.001);
        Assert.assertEquals(billingManager.getBillingAccounts().get(phoneNumber).getBucketC(), 10, 0.001);
        Assert.assertEquals(billingManager.getBillingAccounts().get(phoneNumber).getCounterA(), 0);
        Assert.assertEquals(billingManager.getBillingAccounts().get(phoneNumber).getCounterB(), 0);
        Assert.assertEquals(billingManager.getBillingAccounts().get(phoneNumber).getCounterC(), 0);
        Assert.assertEquals(billingManager.getBillingAccounts().get(phoneNumber).getServiceATariff(), Tariff.ALPHA2);
        Assert.assertEquals(billingManager.getBillingAccounts().get(phoneNumber).getServiceBTariff(), Tariff.BETA2);
    }

    /**
     * Test Case: call createBillingAccount with invalid attributes (Service B Tariff).
     */
    @Test
    public void createBillingAccountInValidAttributes() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "911222111";
        BillingAccount billingAccount = billingManager.createBillingAccount(phoneNumber, "10", "100", "10", "BETA1", "BETA2");

        // Expected: No billing account object was created
        Assert.assertEquals(billingManager.getBillingAccounts().size(), 0);
        Assert.assertNull(billingAccount);
    }

    /**
     * Test Case: call createChargingRequest with valid attributes.
     */
    @Test
    public void createChargingRequestValidAttributes() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "911222111";
        ChargingRequest chargingRequest = billingManager.createChargingRequest("B", "1", phoneNumber, "2");

        Assert.assertNotNull(chargingRequest);
        Assert.assertEquals(chargingRequest.getService(), Service.B);
        Assert.assertEquals(chargingRequest.isRoaming(), true);
        Assert.assertEquals(chargingRequest.getMsidn(), phoneNumber);
        Assert.assertEquals(chargingRequest.getRequestedServiceUnit(), 2);
    }

    /**
     * Test Case: call createChargingRequest with invalid attributes (phone number).
     */
    @Test
    public void createChargingRequestInvalidAttributes() {
        BillingManager billingManager = new BillingManager();
        String phoneNumber = "91122211100";
        ChargingRequest chargingRequest = billingManager.createChargingRequest("B", "1", phoneNumber, "2");

        // Expected: No charging request object was created
        Assert.assertNull(chargingRequest);
    }
}
