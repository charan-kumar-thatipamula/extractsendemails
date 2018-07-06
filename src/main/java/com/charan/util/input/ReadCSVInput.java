package com.charan.util.input;

import com.charan.communication.ParellelProcessCSVsForEmail;
import com.charan.communication.csv.ReadCSV;
import com.charan.global.CSVContext;
import com.charan.global.GlobalContext;

import java.util.List;
import java.util.Scanner;

public class ReadCSVInput {
    private GlobalContext globalContext = GlobalContext.getGlobalContext();
    public void readInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter time gap between emails (in minutes):");
        int time = scanner.nextInt();
        if (time > 0) {
            globalContext.setWaitTimeBetweenEmails(time);
        }
        System.out.println("Number of emails to send within " + time + " minutes?");
        int emailCount = scanner.nextInt();
        if (emailCount > 0) {
            globalContext.setEmailCount(emailCount);
        }
        System.out.println("Enter CSV file path that has email ids that should be ignored; If no such file press 'Enter'");
        String fileOfEmailsToIgnore = scanner.nextLine();
        while (true) {
            System.out.println("\n\n********* GIVE ME CSV DETAILS*********\n");
            System.out.println("Enter CSV file path");
            String csvFilePath = scanner.nextLine();
            if (!validateCSVPath(csvFilePath)) {
                System.out.println("Invalid csv path");
            }

            System.out.println("Enter email id");
            String email = scanner.nextLine();
            if (!validateEmail(email)) {
                System.out.println("Invalid email");
            }
            System.out.println("Enter password");
            String password = scanner.nextLine();

            System.out.println("Enter subject");
            String subject = scanner.nextLine();
//            System.out.println("\n\n");
            System.out.println("You want to use TITLE in email subject y/n");
            String yesOrNo = scanner.nextLine();
            if (yesOrNo.equalsIgnoreCase("Y")) {
                subject = subject + " <TITLE>";
            }
            System.out.println("Enter email template path");
            String emailTemplatePath = scanner.nextLine();
            CSVContext csvContext = new CSVContext();
            csvContext.setCsvpath(csvFilePath);
            csvContext.setEmail(email);
            csvContext.setSubject(subject);
            csvContext.setTemplateFilePath(emailTemplatePath);

            globalContext.getCredentialsUtil().addEmailCreds(email, password);
            globalContext.initSentMailCountHandler(email);
            globalContext.addCSVContext(csvFilePath, csvContext);
            System.out.println("\n\n Do you have more CSVs y/n?");
            if (scanner.nextLine().equalsIgnoreCase("n")) {
                break;
            }
        }
        if (fileOfEmailsToIgnore !=null && fileOfEmailsToIgnore.length() > 0) {
            updateEmailsToIgnore(fileOfEmailsToIgnore);
        }
    }

    private void updateEmailsToIgnore(String fileOfEmailsToIgnore) {
        ReadCSV gm = new ReadCSV(fileOfEmailsToIgnore);
        List<String[]> list = gm.readFile();
        for (String[] line : list) {
            if (line != null && line[0] != null && line[0].length() > 0 && line[0].indexOf("@") != -1) {
                globalContext.addToEmailsToIgnore(line[0]);
            }
        }
    }

    private boolean validateEmail(String csvFilePath) {
        return true;
    }

    private boolean validateCSVPath(String csvFilePath) {
        return true;
    }
}
