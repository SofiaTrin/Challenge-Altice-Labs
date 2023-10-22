package org.challenge;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class: billing account of a client.
 */
public class BillingAccount {
    private String msisdn;
    private double bucketA;
    private double bucketB;
    private double bucketC;
    private int counterA;
    private int counterB;
    private int counterC;
    private Calendar counterD;
    private Tariff serviceATariff;
    private Tariff serviceBTariff;

    public BillingAccount() {
    }

    public BillingAccount(String msisdn, Tariff serviceATariff, Tariff serviceBTariff) {
        this.msisdn = msisdn;
        this.bucketA = 0;
        this.bucketB = 0;
        this.bucketC = 0;
        this.counterA = 0;
        this.counterB = 0;
        this.counterC = 0;
        this.counterD = new GregorianCalendar(1990, Calendar.JANUARY, 1);
        this.serviceATariff = serviceATariff;
        this.serviceBTariff = serviceBTariff;
    }

    public BillingAccount(String msisdn, double bucketA, double bucketB, double bucketC, Tariff serviceATariff, Tariff serviceBTariff) {
        this.msisdn = msisdn;
        this.bucketA = bucketA;
        this.bucketB = bucketB;
        this.bucketC = bucketC;
        this.counterA = 0;
        this.counterB = 0;
        this.counterC = 0;
        this.counterD = new GregorianCalendar(1990, Calendar.JANUARY, 1);
        this.serviceATariff = serviceATariff;
        this.serviceBTariff = serviceBTariff;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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

    public Tariff getServiceATariff() {
        return serviceATariff;
    }

    public void setServiceATariff(Tariff serviceATariff) {
        this.serviceATariff = serviceATariff;
    }

    public Tariff getServiceBTariff() {
        return serviceBTariff;
    }

    public void setServiceBTariff(Tariff serviceBTariff) {
        this.serviceBTariff = serviceBTariff;
    }
}
