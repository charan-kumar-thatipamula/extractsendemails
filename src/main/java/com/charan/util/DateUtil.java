package com.charan.util;

import com.charan.global.GlobalContext;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.floorDiv;

public class DateUtil {
    public static long getTimeInMinutes() {
        long timeMilliSeconds = new Date().getTime();
        long timeInMinutes = floorDiv(timeMilliSeconds, Math.multiplyExact(60, 1000));
        return timeInMinutes;
    }

    public static long getCurrentTime() {
        long timeMilliSeconds = new Date().getTime();
        long currentTime = timeMilliSeconds;
        if (GlobalContext.getGlobalContext().getTimeUnit() == TimeUnit.MINUTES) {
            currentTime = floorDiv(timeMilliSeconds, Math.multiplyExact(60, 1000));
        } else if (GlobalContext.getGlobalContext().getTimeUnit() == TimeUnit.SECONDS) {
            currentTime = floorDiv(timeMilliSeconds, 1000);
        }
        return currentTime;
    }
}
