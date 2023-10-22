package org.challenge;

import java.util.Calendar;

/**
 * Methods to determine the cost of a service, given the tariff (Alpha1, Alpha2, Alpha3, Beta1, Beta2 or Beta3)
 * and the requested service units.
 */
public class Rating {
    public double determineAlpha1Rating(double rsu, boolean roaming, Calendar requestDate, double counterA, double bucketCValue) {
        double price;
        if (roaming) {
            price = 200;
        } else {
            double dayHour = requestDate.get(Calendar.HOUR_OF_DAY);
            // night request
            if (dayHour > 20 || dayHour < 6) {
                price = 50;
            } else {
                price = 100;
            }
        }

        double totalPrice = price * rsu;
        if (counterA > 10) totalPrice = totalPrice - 25;
        if (bucketCValue > 50) totalPrice = totalPrice - 10;

        return totalPrice;
    }

    public double determineAlpha2Rating(double rsu, boolean roaming, Calendar requestDate, double counterB, double bucketBValue) {
        double dayHour = requestDate.get(Calendar.HOUR_OF_DAY);

        double price = 0;
        if (!roaming) {
            if (dayHour > 20 || dayHour < 6) price = 25;
            else price = 50;
        }

        double totalPrice = price * rsu;
        if (counterB > 10) totalPrice = totalPrice - 20;
        if (bucketBValue > 15) totalPrice = totalPrice - 5;

        return totalPrice;
    }

    public double determineAlpha3Rating(double rsu, Calendar requestDate, double counterB, double bucketBValue) {
        double price = 100;
        if (!isWorkingDay(requestDate)) {
            price = 25;
        }

        double totalPrice = price * rsu;
        if (counterB > 10) totalPrice = totalPrice - 20;
        if (bucketBValue > 15) totalPrice = totalPrice - 5;

        return totalPrice;
    }

    public final double determineBeta1Rating(double rsu, Calendar requestDate, boolean roaming, int counterA, double bucketCValue) {
        double dayHour = requestDate.get(Calendar.HOUR_OF_DAY);

        double price;
        if (roaming) {
            price = 20;
        } else {
            price = 10;
            if (dayHour > 20 || dayHour < 6) {
                price = 5;
            }
        }

        double totalPrice = price * rsu;
        if (counterA > 10) totalPrice = totalPrice - 2.5;
        if (bucketCValue > 15) totalPrice = totalPrice - 1;

        return totalPrice;
    }

    public double determineBeta2Rating(double rsu, Calendar requestDate, boolean roaming, double counterB, double bucketBValue) {
        double dayHour = requestDate.get(Calendar.HOUR_OF_DAY);

        double price = 0;
        if (!roaming) {
            if (dayHour > 20 || dayHour < 6) price = 2.5;
            else price = 5;
        }

        double totalPrice = price * rsu;
        if (counterB > 10) totalPrice = totalPrice - 2;
        if (bucketBValue > 15) totalPrice = totalPrice - 0.5;

        return totalPrice;
    }

    public double determineBeta3Rating(double rsu, Calendar requestDate, int counterB, double bucketBValue) {
        double price = 10;
        if (!isWorkingDay(requestDate)) {
            price = 2.5;
        }

        double totalPrice = price * rsu;
        if (counterB > 10) totalPrice = totalPrice - 2;
        if (bucketBValue > 15) totalPrice = totalPrice - 0.5;

        return totalPrice;
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
