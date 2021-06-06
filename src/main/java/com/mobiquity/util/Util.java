package com.mobiquity.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

    public static String defaultIfEmpty(String string, String defaultValue) {
        return string.isEmpty() ? defaultValue : string;
    }

    public static float round(float value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}