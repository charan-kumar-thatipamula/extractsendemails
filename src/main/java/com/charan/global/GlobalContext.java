package com.charan.global;

import com.charan.communication.email.Email;
import com.charan.config.Config;
import com.charan.util.creds.CredentialsUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class GlobalContext {
    private int emailCount = 50;
    private int waitTimeBetweenEmails = 60;
    private volatile static GlobalContext config;
    private Map<String, BlockingQueue<Email>> mapEmailQueue;
    private Map<String, String> mapUrlEmail;
    private Map<String, String> mapUrlTemplateFilePath;
    public CredentialsUtil getCredentialsUtil() {
        return credentialsUtil;
    }

    private CredentialsUtil credentialsUtil;
    private GlobalContext() {
        mapEmailQueue = new HashMap<>();
        mapUrlEmail = new HashMap<>();
        mapUrlTemplateFilePath = new HashMap<>();
        credentialsUtil = new CredentialsUtil();
    }

    public static GlobalContext getGlobalContext() {
        if (config == null) {
            synchronized (Config.class) {
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
        return mapEmailQueue.get(email) == null;
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
        getQueueForEmail(getEmailForUrl(url)).add(email);
        return true;
    }
}
