package com.charan.global;

import com.charan.communication.email.ProcessEmailQueue;
import com.charan.util.creds.CredentialsUtil;

import java.nio.file.Paths;

public class UrlContext {
    GlobalContext globalContext = GlobalContext.getGlobalContext();
    public void setUrlContext(String[] contents) throws Exception{
        String url = null;
        CredentialsUtil credentialsUtil = globalContext.getCredentialsUtil();
        if (contents == null || contents.length <1) {
            throw new Exception("URL is mandatory");
        } else if (contents.length == 1) {
            url = contents[0];
            globalContext.addUrlEmailMap(url, credentialsUtil.getDefaultEmail());
            startQueueProcessorForEmail(credentialsUtil.getDefaultEmail());
        } else if (contents.length ==2) {
            throw new Exception("Please enter both email and password OR just enter URL to use default email password");
        } else if (contents.length ==3) {
            url = contents[0];
            // TODO: validate contents[1] is email or not
            credentialsUtil.addEmailCreds(contents[1], contents[2]);
            startQueueProcessorForEmail(contents[1]);
        } else {
            throw new Exception("More than 3 inputs given; aborting this line");
        }
        String templateFilePath = Paths.get("emailtemplate.txt").toAbsolutePath().toString();
        globalContext.addUrlTemplateFilePath(url, templateFilePath);
    }

    private void startQueueProcessorForEmail(String email) {
        if (globalContext.isQueuePresent(email)) return;

        globalContext.createQueueForEmail(email);

        ProcessEmailQueue processEmailQueue = new ProcessEmailQueue();
        processEmailQueue.setEmailAndQueue(email);
        Thread thread = new Thread(processEmailQueue);
        thread.start();
//        thread.
    }
}
