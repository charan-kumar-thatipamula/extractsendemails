package com.charan.websites.umin;

import com.charan.httprequest.SendRequest;
import com.charan.websites.AbstractWebsiteHandler;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UminHandler extends AbstractWebsiteHandler {
    String url;
    String outputFile;
    Map<String, String> emails;

    public UminHandler(String url) {
        this.url = url;
        String s = "freeword=";
        String e = "&";
        String term = url.substring(url.indexOf(s) + s.length(), url.length());
        term = term.substring(0, term.indexOf("&"));
        this.outputFile = "outputcsv" + "_umin_" + term + "_" + new Date().getTime() + ".csv";
    }
    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public List<String> extractJournalUrls(String url) {
        List<String> journalUrls = new ArrayList<>();
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        int pageCount = 2;//getPageCount(url);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int i=1;
//        boolean morePagesPresent = true;
        while (i <= pageCount) {
            url = url.substring(0, url.lastIndexOf("page="));
            url = url + "page=" + i;
            System.out.println("url: " + url);
            Runnable uminWorker = new UminWorker(journalUrls, url);
            executorService.execute(uminWorker);
//            UminWorker uminWorker = new UminWorker(journalUrls, url);
//            uminWorker.processSinglePage();
//            morePagesPresent = processSinglePage(journalUrls, url);
            i = i + 1;
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        String journals = String.join("\n", journalUrls);
        System.out.println(journals);
        return journalUrls;
    }

    @Override
    public String processJournals(List<String> journalUrls) {
//        UminExtractJournalMetadata uminExtractJournalMetadata = new UminExtractJournalMetadata();
        List<String> finalData = new LinkedList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        UminExtractJournalMetadata uminExtractJournalMetadata = new UminExtractJournalMetadata();
        for (String journalUrl : journalUrls) {
//            Runnable uminExtractJournalMetadata = new UminExtractJournalMetadata();
//            ((UminExtractJournalMetadata)uminExtractJournalMetadata).setJournalUrl(journalUrl);
//            ((UminExtractJournalMetadata)uminExtractJournalMetadata).setFinalData(finalData);
//            executorService.execute(uminExtractJournalMetadata);
            UminExtractJournalMetadata uminExtractJournalMetadata = new UminExtractJournalMetadata();
            uminExtractJournalMetadata.setJournalUrl(journalUrl);
            uminExtractJournalMetadata.setFinalData(finalData);
            uminExtractJournalMetadata.getJournalMetadata();
//            String contents = uminExtractJournalMetadata.getJournalMetadata();
//            addEmailToQueue(contents);
//			String contents = getJournalMetadata(journalUrl);
//            if (contents.indexOf("@") == -1)
//                continue;
//			System.out.println("contents: " + contents);
//            fData = fData + contents;
        }
        executorService.shutdown();
        while (!executorService.isTerminated()){}
//        addEmailToQueue("Processing done for: " + url);
        String fData = String.join("\n", finalData);
        return fData;
    }
    private int getPageCount(String url) {
        System.out.println("Getting page count");
        int pageCount = 100;
        try {
            String keyword = "Studies searched：";
            SendRequest sendRequest = new SendRequest();
            String response = sendRequest.sendGet(url);
            int sInd = response.indexOf(keyword) + keyword.length();
            response = response.substring(sInd, response.length());
            int totalResults = Integer.parseInt(response.substring(0, response.indexOf("]")));
            pageCount = totalResults/100;
            pageCount = (totalResults % 100) == 0 ? pageCount : pageCount + 1;
        } catch (Exception e) {
            System.out.println("Exception while calculating page count: " + e.getMessage());
        }
        System.out.println("Total number of pages: " + pageCount);
        return pageCount;
    }

    @Override
    public String getOutputFileName() {
        return this.outputFile;
    }

    public static void main(String[] args) {
        String url = "https://upload.umin.ac.jp/cgi-open-bin/ctr_e/index.cgi?sort=03&freeword=cancer&isicdr=1&page=1";
        UminHandler uminHandler = new UminHandler(url);
        uminHandler.extractJournalUrls(url);
    }
}
