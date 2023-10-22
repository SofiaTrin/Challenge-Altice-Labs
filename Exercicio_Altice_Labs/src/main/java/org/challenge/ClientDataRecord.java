package org.challenge;

import java.util.Calendar;

/**
 * Class: client data record
 */
public class ClientDataRecord {
    private Calendar timestamp;
    private String msisdn;
    private Service service;
    private ChargingRequest chargingRequest;
    private ChargingReply chargingReply;
    private double bucketA;
    private double bucketB;
    private double bucketC;
    private int counterA;
    private int counterB;
    private int counterC;
    private Calendar counterD;
    private Tariff tariff;
    private double totalPrice;

    public ClientDataRecord() {
    }

    public ClientDataRecord(Calendar timestamp, String msisdn, Service service, ChargingRequest chargingRequest, ChargingReply chargingReply, double bucketA, double bucketB, double bucketC, int counterA, int counterB, int counterC, Calendar counterD, Tariff tariff, double totalPrice) {
        this.timestamp = timestamp;
        this.msisdn = msisdn;
        this.service = service;
        this.chargingRequest = chargingRequest;
        this.chargingReply = chargingReply;
        this.bucketA = bucketA;
        this.bucketB = bucketB;
        this.bucketC = bucketC;
        this.counterA = counterA;
        this.counterB = counterB;
        this.counterC = counterC;
        this.counterD = counterD;
        this.tariff = tariff;
        this.totalPrice = totalPrice;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ChargingRequest getChargingRequest() {
        return chargingRequest;
    }

    public void setChargingRequest(ChargingRequest chargingRequest) {
        this.chargingRequest = chargingRequest;
    }

    public ChargingReply getChargingReply() {
        return chargingReply;
    }

    public void setChargingReply(ChargingReply chargingReply) {
        this.chargingReply = chargingReply;
    }

    public double getBucketA() {
        return bucketA;
    }

    public void setBucketA(double bucketA) {
        this.bucketA = bucketA;
    }

    public double getBucketB() {
        return bucketB;
    }

    public void setBucketB(double bucketB) {
        this.bucketB = bucketB;
    }

    public double getBucketC() {
        return bucketC;
    }

    public void setBucketC(double bucketC) {
        this.bucketC = bucketC;
    }

    public int getCounterA() {
        return counterA;
    }

    public void setCounterA(int counterA) {
        this.counterA = counterA;
    }

    public int getCounterB() {
        return counterB;
    }

    public void setCounterB(int counterB) {
        this.counterB = counterB;
    }

    public int getCounterC() {
        return counterC;
    }

    public void setCounterC(int counterC) {
        this.counterC = counterC;
    }

    public Calendar getCounterD() {
        return counterD;
    }

    public void setCounterD(Calendar counterD) {
        this.counterD = counterD;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "ClientDataRecord {\n" +
                "date = " + timestamp.getTime() + ",\n" +
                "service = " + service + ", " +
                "tariff = " + tariff + ",\n" +
                "tariff price = " + totalPrice + ",\n" +
                "chargingRequest = " + chargingRequest + ",\n" +
                "chargingReply = " + chargingReply + ",\n" +
                "bucketA = " + bucketA + ", " +
                "bucketB = " + bucketB + ", " +
                "bucketC = " + bucketC + ",\n" +
                "counterA = " + counterA + ", " +
                "counterB = " + counterB + ", " +
                "counterC = " + counterC + ",\n" +
                "counterD = " + counterD.getTime() + ",\n" +
                "}";
    }
}
