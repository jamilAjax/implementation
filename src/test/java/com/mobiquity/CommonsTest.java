package com.mobiquity;

import com.mobiquity.commons.Commons;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonsTest {
    @Test
    void returnDefaultValueWhenStringIsEmpty() {
        String expectedResult = "default";
        String result = Commons.defaultIfEmpty("", expectedResult);

        assertEquals(expectedResult, result);
    }

    @Test
    void returnStringValueWhenStringIsNotEmpty() {
        String expectedResult = "Value";
        String result = Commons.defaultIfEmpty(expectedResult, "default");

        assertEquals(expectedResult, result);
    }

    @Test
    void returnFloatScaledTo2PrecisionWhenMoreThan2DecimalPoints() {
        float expectedResult = 14.57f;
        float result = Commons.round(14.5699999999f);

        assertEquals(expectedResult, result);
    }
}
