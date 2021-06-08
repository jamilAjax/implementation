package com.mobiquity.commons;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Commons {

    private Commons() {
    }

    public static String defaultIfEmpty(String string, String defaultValue) {
        return string.isEmpty() ? defaultValue : string;
    }

    public static float round(float value) {
        var bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}