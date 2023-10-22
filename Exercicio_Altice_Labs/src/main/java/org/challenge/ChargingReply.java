package org.challenge;

/**
 * Class: charging reply
 */
public class ChargingReply {
    private String requestId;
    private Eligibility.ElegStatus result;
    private int grantedServiceUnits;

    public ChargingReply() {
    }

    public ChargingReply(String requestId, Eligibility.ElegStatus result, int grantedServiceUnits) {
        this.requestId = requestId;
        this.result = result;
        this.grantedServiceUnits = grantedServiceUnits;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Eligibility.ElegStatus getResult() {
        return result;
    }

    public void setResult(Eligibility.ElegStatus result) {
        this.result = result;
    }

    public int getGrantedServiceUnits() {
        return grantedServiceUnits;
    }

    public void setGrantedServiceUnits(int grantedServiceUnits) {
        this.grantedServiceUnits = grantedServiceUnits;
    }

    @Override
    public String toString() {
        return "ChargingReply {\n" +
                "requestId = '" + requestId + ",\n" +
                "result = " + result + ",\n" +
                "grantedServiceUnits = " + grantedServiceUnits + ",\n" +
                "}";
    }
}
