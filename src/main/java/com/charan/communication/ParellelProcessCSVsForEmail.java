package com.charan.communication;

import com.charan.communication.factory.TriggerSendEmailsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParellelProcessCSVsForEmail {
    List<String> csvFileNames;

    public void addCSVFileJobs(String csvFileName) {
        if (csvFileNames == null) {
            csvFileNames = new ArrayList<String>();
        }
        csvFileNames.add(csvFileName);
    }
    public void triggerEmail() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (String csvFileName : csvFileNames) {
            Runnable worker = new TriggerSendEmails(); // TriggerSendEmailsFactory.getTriggerSendEmails();
            ((TriggerSendEmails) worker).setFilePath(csvFileName);
            System.out.println(Thread.currentThread().getName() + "; Starting worker for : " + csvFileName);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }
}
