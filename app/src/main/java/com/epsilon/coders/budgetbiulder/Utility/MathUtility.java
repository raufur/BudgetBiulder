package com.epsilon.coders.budgetbiulder.Utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Raufur on 10/6/15.
 */
public class MathUtility {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
