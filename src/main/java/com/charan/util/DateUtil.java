package com.charan.util;

import java.util.Date;

import static java.lang.Math.floorDiv;

public class DateUtil {
    public static long getTimeInMinutes() {
        long timeMilliSeconds = new Date().getTime();
        long timeInMinutes = floorDiv(timeMilliSeconds, Math.multiplyExact(60, 1000));
        return timeInMinutes;
    }
}
