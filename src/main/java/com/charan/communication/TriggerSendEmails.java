package com.charan.communication;

import com.charan.communication.csv.ReadCSV;
import com.charan.communication.email.Email;
import com.charan.communication.email.GetEmailFromTemplate;
import com.charan.communication.email.EmailSendService;
import com.charan.communication.factory.TriggerSendEmailsFactory;
import com.charan.communication.util.EmailUtil;
import com.charan.global.CSVContext;
import com.charan.global.GlobalContext;
import com.charan.log.AkitaLogger;
import com.charan.util.creds.CredentialsUtil;
import com.charan.util.email.SentMailCountHandler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TriggerSendEmails implements Runnable{

    String filePath;
    GlobalContext globalContext = GlobalContext.getGlobalContext();
    AkitaLogger akitaLogger = AkitaLogger.getAkitaLogger();
    SentMailCountHandler sentMailCountHandler;
    String templatePath;
    String fromEmail;

    public TriggerSendEmails () {
        this.templatePath = "emailtemplate.txt";
        this.fromEmail = "charant.lgp@gmail.com";
    }

    public TriggerSendEmails(String filePath, String templatePath, String fromEmail) {
        this.filePath = filePath;
        this.templatePath = templatePath;
        this.fromEmail = fromEmail;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public SentMailCountHandler getSentMailCountHandler() {
        return sentMailCountHandler;
    }

    public void setSentMailCountHandler(SentMailCountHandler sentMailCountHandler) {
        this.sentMailCountHandler = sentMailCountHandler;
    }

    public void process() {
        Path path = Paths.get(filePath);
//        System.out.println(path.toAbsolutePath());
        ReadCSV gm = new ReadCSV(filePath);
        List<String[]> list = gm.readFile();

        ExecutorService executor = Executors.newFixedThreadPool(3);

        int i = 0;
        for(String[] contents : list) {
            List<String> row = Arrays.asList(contents);
            if(row.indexOf("Title") != -1) {
                continue;
            }
//            templatePath = Paths.get("emailtemplate.txt").toAbsolutePath().toString();

            Email email = getEmailtoSend(contents);
            EmailUtil emailUtil = new EmailUtil();
            if (emailUtil.shouldIgnoreEmail(email)) {
                System.out.println("***** Ignoring email: " + email.getTo()[0] + "********");
                continue;
            }
            emailUtil.waitIfMailsLimitReached(email);
            sendMail(executor, email);
            globalContext.getSentMailCountHandler(email.getFrom()).updateSentMailCount();
            akitaLogger.info(email.toString());
//            i++;
//            if (i == 5 ) {
//                break;
//            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Done sending emails");
//        gm.setEt(et);
    }

    private void sendMail(Executor executor, Email email) {
        Runnable worker = new EmailSendService();
        ((EmailSendService) worker).setEm(email);
        System.out.println("Sending email : " + email.toString());
        executor.execute(worker);
    }
    private Email getEmailtoSend(String[] contents) {
//        templatePath = globalContext.getCSVContext(getFilePath()).getTemplateFilePath();
        GetEmailFromTemplate et = new GetEmailFromTemplate();
//        et.setTemplateFilePath();
        et.setCsvContext(globalContext.getCSVContext(getFilePath()));
        et.setHeaderIndices(contents);
        Email em = et.process(); //et.extractEmail(contents);
        return em;
    }
    public static void main(String[] args) {
//        String filePath = "/Users/communication/Projects/sendemails/src/com/communication/csv/outputcsv1526668119123.csv";
        String filePath = "C:\\Users\\Buddamma\\Documents\\Projects\\outputcsv1527930209807.csv";
        String templatePath = "C:\\Users\\Buddamma\\Documents\\Projects\\extractsendemails\\emailtemplate.txt";

        GlobalContext globalContext = GlobalContext.getGlobalContext();
        CredentialsUtil credentialsUtil = globalContext.getCredentialsUtil();
        credentialsUtil.setDefaultCreds();

        CSVContext csvContext = new CSVContext();
        csvContext.setCsvpath(filePath);
        csvContext.setTemplateFilePath(templatePath);
        csvContext.setEmail(credentialsUtil.getDefaultEmail());
        csvContext.setSubject("Share with JCRM");

        globalContext.addCSVContext(filePath, csvContext);
        globalContext.initSentMailCountHandler(csvContext.getEmail());
        globalContext.setWaitTimeBetweenEmails(10);
        globalContext.setTimeUnit(TimeUnit.SECONDS);
        globalContext.setEmailCount(8);
//        String templatePath = "/Users/communication/Projects/extractemails/src/com/communication/templates/emailtemplate.txt";
//        String fromEmail = "charant.lgp+journalstest@gmail.com";
        TriggerSendEmails triggerSendEmails = new TriggerSendEmails(); //TriggerSendEmailsFactory.getTriggerSendEmails();//filePath, templatePath, fromEmail);
        triggerSendEmails.setFilePath(filePath);
        triggerSendEmails.process();
    }

    @Override
    public void run() {
        process();
    }
}
