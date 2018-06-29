package com.charan.websites.umin;

import com.charan.httprequest.SendRequest;

import java.util.List;

public class UminExtractJournalMetadata implements Runnable{
    String journalUrl;
    List<String> finalData;
    public String getJournalMetadata() {
        String journalMetadata = "";
        try {
            SendRequest sReq = new SendRequest();
            String response = sReq.sendGet(journalUrl);
            String title = getTitle(response);
            String firstAuthorName = getFirstAuthorName(response);
            String firstAuthorEmail = getFirstAuthorEmail(response);

            String secondAuthorName = getSecondAuthorName(response);
            String secondAuthorEmail = getSecondAuthorEmail(response);

            if (firstAuthorEmail != null &&
                    firstAuthorEmail.length() > 0) {
                journalMetadata = journalMetadata + firstAuthorName + "," + firstAuthorEmail + "," + title;
                if (secondAuthorEmail != null &&
                        secondAuthorEmail.length() > 0 &&
                        !firstAuthorEmail.equals(secondAuthorEmail)) {
                    journalMetadata = journalMetadata + "\n" + secondAuthorName + "," + secondAuthorEmail + "," + title;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception while generating data for journal: " + e.getMessage());
        }
        System.out.println(journalMetadata);
        synchronized (finalData) {
            finalData.add(journalMetadata);
        }
        return journalMetadata;
    }

    private String getTitle(String response) {
        String keyword = "Official scientific title of the study";
        String title = getValueForKeyWord(response, keyword);
        return title;
    }

    private String getFirstAuthorName(String response) {
        String keyword = "Name of lead principal investigator";
        String firstAuthorName = getValueForKeyWord(response, keyword);
        return firstAuthorName;
    }

    private String getFirstAuthorEmail(String response) {
        String keyword = "Name of lead principal investigator";
        response = response.substring(response.indexOf(keyword), response.length());
        keyword = "Email";
        String firstAuthorName = getValueForKeyWord(response, keyword);
        return firstAuthorName;
    }

    private String getSecondAuthorName(String response) {
        String keyword = "Name of contact person";
        String firstAuthorName = getValueForKeyWord(response, keyword);
        return firstAuthorName;
    }

    private String getSecondAuthorEmail(String response) {
        String keyword = "Name of contact person";
        response = response.substring(response.indexOf(keyword), response.length());
        keyword = "Email";
        String firstAuthorName = getValueForKeyWord(response, keyword);
        return firstAuthorName;
    }

    private String getValueForKeyWord(String response, String keyword) {
        String tResponse = response.substring(response.indexOf(keyword) + keyword.length(), response.length());
        int sInd = tResponse.indexOf("\">") + "\">".length();
        tResponse = tResponse.substring(sInd, tResponse.length());
        int eInd = tResponse.indexOf("</");
        String value = tResponse.substring(0, eInd);
        System.out.println(value);
        return value;
    }

    @Override
    public void run() {
        getJournalMetadata();
    }

    public void setJournalUrl(String journalUrl) {
        this.journalUrl = journalUrl;
    }

    public void setFinalData(List<String> finalData) {
        this.finalData = finalData;
    }
    //    public static void main(String[] args) {
//        String journalUrl = "https://upload.umin.ac.jp/cgi-open-bin/ctr_e/ctr_view.cgi?recptno=R000037862";
//        UminExtractJournalMetadata uminExtractJournalMetadata = new UminExtractJournalMetadata();
//        uminExtractJournalMetadata.getJournalMetadata(journalUrl);
//    }
}
