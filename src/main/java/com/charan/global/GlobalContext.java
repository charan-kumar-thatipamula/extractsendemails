package com.charan.global;

import com.charan.communication.email.Email;
import com.charan.config.Config;
import com.charan.util.creds.CredentialsUtil;
import com.charan.util.email.SentMailCountHandler;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class GlobalContext {
    private int emailCount = 100;
    private int waitTimeBetweenEmails = 60;
    private TimeUnit timeUnit = TimeUnit.MINUTES;
    private volatile static GlobalContext config;
    private Map<String, BlockingQueue<Email>> mapEmailQueue;
    private Map<String, String> mapUrlEmail;
    private Map<String, String> mapUrlTemplateFilePath;
    private Map<String, SentMailCountHandler> mapEmailSentMailCountHandler;
    private Map<String, CSVContext> mapCsvToContext;
    private Set<String> emailsToIgnore;

    public CredentialsUtil getCredentialsUtil() {
        return credentialsUtil;
    }

    private CredentialsUtil credentialsUtil;
    private GlobalContext() {
        mapEmailQueue = new HashMap<>();
        mapUrlEmail = new HashMap<>();
        mapUrlTemplateFilePath = new HashMap<>();
        credentialsUtil = new CredentialsUtil();
        mapEmailSentMailCountHandler = new HashMap<>();
        mapCsvToContext = new HashMap<>();
        emailsToIgnore = new HashSet<>();
    }

    public static GlobalContext getGlobalContext() {
        if (config == null) {
            synchronized (GlobalContext.class) {
                if (config == null) {
                    config = new GlobalContext();
                }
            }
        }
        return config;
    }

    public void addUrlEmailMap(String url, String defaultEmail) {
        mapUrlEmail.put(url, defaultEmail);
    }

    public boolean isQueuePresent(String email) {
        return mapEmailQueue.get(email) != null;
    }

    public void createQueueForEmail(String email) {
        BlockingQueue<Email> blockingQueue = new LinkedBlockingDeque<>();
        mapEmailQueue.put(email, blockingQueue);
    }

    public BlockingQueue<Email> getEmailQueue(String email) {
        return mapEmailQueue.get(email);
    }

    public void setEmailCount(int emailCount) {
        this.emailCount = emailCount;
    }

    public int getEmailCount() {
        return emailCount;
    }

    public int getWaitTimeBetweenEmails() {
        return waitTimeBetweenEmails;
    }

    public void setWaitTimeBetweenEmails(int waitTimeBetweenEmails) {
        this.waitTimeBetweenEmails = waitTimeBetweenEmails;
    }

    public void addUrlTemplateFilePath(String url, String templateFilePath) {
        mapUrlTemplateFilePath.put(url, templateFilePath);
    }
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getTemplatePathForUrl(String url) {
        return mapUrlTemplateFilePath.get(url);
    }

    public String getEmailForUrl(String url) {
        return mapUrlEmail.get(url);
    }

    public BlockingQueue<Email> getQueueForEmail(String email) {
        return mapEmailQueue.get(email);
    }

    public boolean addToQueue(String url, Email email) {
        String emailS = getEmailForUrl(url);
        BlockingQueue<Email> bc = getQueueForEmail(emailS);
        bc.add(email);
        return true;
    }

    public void initSentMailCountHandler(String email) {
        synchronized (this) {
            if (this.mapEmailSentMailCountHandler == null) {
                this.mapEmailSentMailCountHandler = new HashMap<>();
            }
        }
        synchronized (this) {
            SentMailCountHandler sentMailCountHandler = new SentMailCountHandler();
            sentMailCountHandler.setWaitTimeBetweenEmails(GlobalContext.getGlobalContext().getWaitTimeBetweenEmails());
            sentMailCountHandler.setEmailLimit(GlobalContext.getGlobalContext().getEmailCount());
            if (this.mapEmailSentMailCountHandler.get(email) == null) {
                this.mapEmailSentMailCountHandler.put(email, sentMailCountHandler);
            }
        }
    }

    public SentMailCountHandler getSentMailCountHandler(String email) {
        return this.mapEmailSentMailCountHandler.get(email);
    }

    public void addCSVContext(String csvFilePath, CSVContext csvContext) {
        synchronized (mapCsvToContext) {
            if (mapCsvToContext == null) {
                mapCsvToContext = new HashMap<>();
            }
            if (mapCsvToContext.get(csvFilePath) == null) {
                mapCsvToContext.put(csvFilePath, csvContext);
            }
        }
    }

    public Set<String> getCSVFilePaths() {
        Set<String> csvFilePaths = mapCsvToContext.keySet();
        return csvFilePaths;
    }

    public CSVContext getCSVContext(String csvFilePath) {
        return mapCsvToContext.get(csvFilePath);
    }

    public void addToEmailsToIgnore(String email) {
        if (email == null)
             return;
        emailsToIgnore.add(email);
    }

    public boolean shouldIgnore(String email) {
        if (email == null)
            return true;
        return emailsToIgnore.contains(email);
    }
}
