package com.charan.communication.factory;

import com.charan.communication.TriggerSendEmails;
import com.charan.global.GlobalContext;
import com.charan.util.email.SentMailCountHandler;

public class TriggerSendEmailsFactory {
    public static TriggerSendEmails getTriggerSendEmails() {
        SentMailCountHandler sentMailCountHandler = new SentMailCountHandler();
        sentMailCountHandler.setWaitTimeBetweenEmails(GlobalContext.getGlobalContext().getWaitTimeBetweenEmails());
        sentMailCountHandler.setEmailLimit(GlobalContext.getGlobalContext().getEmailCount());

        TriggerSendEmails triggerSendEmails = new TriggerSendEmails();
        triggerSendEmails.setSentMailCountHandler(sentMailCountHandler);

        return triggerSendEmails;
    }
}
