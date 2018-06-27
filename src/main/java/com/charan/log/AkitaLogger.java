package com.charan.log;

import com.charan.file.FileUility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AkitaLogger {
    private volatile static AkitaLogger akitaLogger;
    private FileUility fileUtility;
    private AkitaLogger() {
        fileUtility = new FileUility();
        String timeStamp = Long.toString(new Date().getTime());
        String fileName = "emailsSent_" + timeStamp + ".log";
        fileUtility.setOutputFileName(fileName);
    }
    public static AkitaLogger getAkitaLogger() {
        if (akitaLogger == null) {
            synchronized (AkitaLogger.class) {
                if (akitaLogger == null) {
                    akitaLogger = new AkitaLogger();
                }
            }
        }
        return akitaLogger;
    }

    public void info(String log) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        log = ft.format(new Date()) + ": " + log + "\n";
        fileUtility.writeOutputFile(log, true);
    }
}
