package com.charan.communication.util;

import com.charan.communication.email.Email;
import com.charan.global.GlobalContext;
import com.charan.util.email.SentMailCountHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EmailUtil {
    public boolean shouldIgnoreEmail(Email email) {
        if (email == null)
            return true;
        String[] sendToArray = email.getTo();
        if (sendToArray == null || sendToArray.length <= 0)
            return true;
        List<String> actualSendTo = new LinkedList<>();
        for (String sendTo : sendToArray) {
            if (!GlobalContext.getGlobalContext().shouldIgnore(sendTo)) {
                actualSendTo.add(sendTo);
            }
        }
        sendToArray = actualSendTo.toArray(new String[actualSendTo.size()]);
        email.setTo(sendToArray);
        if (sendToArray.length > 0) {
            return false;
        }
        return true;
    }

    public void waitIfMailsLimitReached(Email email) {
        SentMailCountHandler sentMailCountHandler = GlobalContext.getGlobalContext().getSentMailCountHandler(email.getFrom());
        if (sentMailCountHandler.sentMailsLimitReached()) {
            long waitTime = GlobalContext.getGlobalContext().getWaitTimeBetweenEmails();
            try {
                if (GlobalContext.getGlobalContext().getTimeUnit() == TimeUnit.MINUTES) {
                    waitTime = waitTime * 60 * 1000;
                } else if (GlobalContext.getGlobalContext().getTimeUnit() == TimeUnit.SECONDS) {
                    waitTime = waitTime * 1000;
                }
                System.out.println("Sent mail count reached. Going to sleep");
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
