# Challenge-Altice-Labs

### How to run this program:
- Main Class (src/main/java/org/challenge/Main.java) has the Menu options
- Test class (src/test/java/ApplicationTests.java) performs tests on the main methods

### Menu Options:
1 - Create Billing Account

2 - Create Charging Request and Get Charging Reply

3 - Access Client Data Records

4 - Exit

### Code Logic:

The data classes are: BillingAccount, ChargingReply, ChargingRequest and ClientDataRecord.

Eligibility and Charging are classes built with enums for their categories.
(Eligibility: OK, CREDIT_LIMIT_REACHED, NOT_ELIGIBLE; 
Charging: BUCKET_A, BUCKET_B, BUCKET_C). 
<br /> The Eligibility class has a method to determine if the charging request is eligible and, if eligible, Charging has a method to determine from which bucket the cost will be charged.

Tariff and Service are enums with specific categories. (Tariff: ALPHA1, ALPHA2, ALPHA3, BETA1, BETA2, BETA3; Service: A, B)

The Billing Manager has the main methods of the application:
- determineChargingReply -- given a charging request returns a charging reply
- updateBillingAccount -- updates billing account parameters
- showClientDataRecords -- presents data records of specific phone number
 - showClientExpenses -- presents client expenses
- createClientDataRecord -- creates a ClientDataRecord object
- createChargingReply -- creates a ChargingReply object
- createBillingAccount -- creates a BillingAccount object
- createChargingRequest -- creates a ChargingRequest object
