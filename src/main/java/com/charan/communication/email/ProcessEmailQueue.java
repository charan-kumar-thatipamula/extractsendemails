package com.charan.communication.email;

import com.charan.global.GlobalContext;
import com.charan.util.DateUtil;
import com.charan.util.email.SentMailCountHandler;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProcessEmailQueue implements Runnable{

    ExecutorService executor;
    BlockingQueue<Email> emailQueue;
    String email;
    int emailCount;
    int timeInMinutes;
    SentMailCountHandler sentMailCountHandler;
    public ProcessEmailQueue() {}
    ProcessEmailQueue(BlockingQueue<Email> emailQueue) {
        this.emailQueue = emailQueue;
        this.emailCount = GlobalContext.getGlobalContext().getEmailCount();
        this.timeInMinutes = GlobalContext.getGlobalContext().getWaitTimeBetweenEmails();
        this.executor = Executors.newFixedThreadPool(emailCount);
        this.sentMailCountHandler = new SentMailCountHandler(timeInMinutes, emailCount);
    }

    private void processEmails() {
        boolean condition = true;
        while (condition) {
            Email email = null;
            long timeInMinutes = DateUtil.getTimeInMinutes();
            try {
                if (sentMailCountHandler.countReached(timeInMinutes)) {
                    Thread.sleep(timeInMinutes * 60 * 1000);
                }
                email = emailQueue.poll(5, TimeUnit.MINUTES);
                Runnable worker = new EmailSendService();
                ((EmailSendService) worker).setEm(email);
                executor.execute(worker);
                sentMailCountHandler.updateCount(timeInMinutes);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        processEmails();
    }

    public void setEmailAndQueue(String email) {
        this.email = email;
        this.emailQueue = GlobalContext.getGlobalContext().getEmailQueue(email);
    }
}
