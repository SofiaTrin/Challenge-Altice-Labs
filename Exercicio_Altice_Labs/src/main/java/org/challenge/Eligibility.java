package org.challenge;

import java.util.Calendar;

/**
 * Class to assess tariff eligibility
 */
public class Eligibility {
    public enum ElegStatus {
        OK,
        CREDIT_LIMIT_REACHED,
        NOT_ELIGIBLE
    }

    /**
     * Given specif params, determines the eligibility of a tariff.
     *
     * @param tariff given tariff
     * @param requestDate date of the request
     * @param roaming if it's local or roaming
     * @param counterA number of service units of service A
     * @param bucketB value of bucket B
     * @param bucketC value of bucket C
     * @return if tariff is ok or it's rejected
     */
    public boolean determineEligibility(Tariff tariff, Calendar requestDate, boolean roaming, int counterA, double bucketB, double bucketC) {
        if (tariff.equals(Tariff.ALPHA1)) {
            return (isWorkingDay(requestDate) && counterA < 100);
        }
        if (tariff.equals(Tariff.BETA1)) {
            int dayHour = requestDate.get(Calendar.HOUR_OF_DAY);
            return isWorkingDay(requestDate) || (dayHour > 20 || dayHour < 6);
        } else if (tariff.equals(Tariff.ALPHA2) || tariff.equals(Tariff.BETA2)) {
            return !(roaming && bucketB > 10);
        } else {
            return !(!roaming && bucketC > 10);
        }
    }

    /**
     * Method that checks if a specific day is a working day or not.
     *
     * @param requestDate Calendar date
     * @return boolean that indicates if requestDate is a working day or not
     */
    public boolean isWorkingDay(Calendar requestDate) {
        double nDay = requestDate.get(Calendar.DAY_OF_WEEK);
        return nDay > 1 && nDay < 6;
    }
}
