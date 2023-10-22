package org.challenge;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Class: charging request
 */
public class ChargingRequest {
    private String requestId;
    private Calendar timestamp;
    private Service service;
    private boolean roaming;
    private String msidn;
    private int requestedServiceUnit;

    public ChargingRequest() {
    }

    public ChargingRequest(Service service, boolean roaming, String msidn, int requestedServiceUnit) {
        this.requestId = UUID.randomUUID().toString();
        this.timestamp = new GregorianCalendar();
        this.service = service;
        this.roaming = roaming;
        this.msidn = msidn;
        this.requestedServiceUnit = requestedServiceUnit;
    }

    public String getRequestId() {
        return requestId;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }

    public String getMsidn() {
        return msidn;
    }

    public void setMsidn(String msidn) {
        this.msidn = msidn;
    }

    public int getRequestedServiceUnit() {
        return requestedServiceUnit;
    }

    public void setRequestedServiceUnit(int requestedServiceUnit) {
        this.requestedServiceUnit = requestedServiceUnit;
    }

    @Override
    public String toString() {
        return "ChargingRequest {\n" +
                "requestId='" + requestId + ",\n" +
                "timestamp=" + timestamp + ",\n" +
                "service=" + service + ",\n" +
                "roaming=" + roaming + ",\n" +
                "msidn='" + msidn + ",\n" +
                "requestedServiceUnit=" + requestedServiceUnit + "\n" +
                "}\n";
    }
}
