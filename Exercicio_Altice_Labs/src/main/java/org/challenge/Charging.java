package org.challenge;

/**
 * Class that has all the types of the charging buckets (ChargingStatus)
 * and the methods to determine which bucket will be charged.
 */
public class Charging {
    public enum ChargingStatus {
        BUCKET_A,
        BUCKET_B,
        BUCKET_C
    }

    /**
     * Method that determines in which bucket the cost will be charged
     *
     * @param tariff specif tariff
     * @param roaming if it is local or roaming
     * @param counterB number of requests of the beta1 tariff
     * @return bucket
     */
    public Charging.ChargingStatus determineBucketToCharge(Tariff tariff, boolean roaming, int counterB) {
        if (tariff.equals(Tariff.ALPHA1) || tariff.equals(Tariff.BETA1)) {
            if (!roaming) {
                return Charging.ChargingStatus.BUCKET_A;
            } else if (counterB > 5) {
                return Charging.ChargingStatus.BUCKET_B;
            } else {
                return Charging.ChargingStatus.BUCKET_C;
            }
        } else if (tariff.equals(Tariff.ALPHA2) || tariff.equals(Tariff.BETA2)) {
            return Charging.ChargingStatus.BUCKET_B;
        } else {
            return Charging.ChargingStatus.BUCKET_C;
        }
    }
}
