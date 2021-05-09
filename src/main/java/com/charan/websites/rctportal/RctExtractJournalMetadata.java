package com.charan.websites.rctportal;

import java.util.List;

import com.charan.httprequest.SendRequest;

public class RctExtractJournalMetadata implements Runnable {
	String journalUrl;
    List<String> finalData;
    public String getJournalMetadata() {
        String journalMetadata = "";
        String secondMetadata = "";
        try {
            SendRequest sReq = new SendRequest();
            String response = sReq.sendGet(journalUrl);
            String title = getTitle(response);
            String name = getScientificContactName(response);
            String email = getScientificContactEmail(response);
            if (title != null) {
            	title = title.trim();
            }
            if (name != null) {
            	name = name.trim();
            }
            if (email != null) {
            	email = email.trim();
            }
            if (email != null && email.length() > 0) {
                journalMetadata = journalMetadata + name + "," + email + "," + title;
            }
        } catch (Exception e) {
            System.out.println("Exception while generating data for journal: " + e.getMessage());
        }
        System.out.println(journalMetadata);
        synchronized (finalData) {
            if (journalMetadata != null && journalMetadata.length() > 0) {
                finalData.add(journalMetadata);
            }
            if (secondMetadata != null && secondMetadata.length() > 0) {
                finalData.add(secondMetadata);
            }
        }
        return journalMetadata;
    }

    private String getTitle(String response) {
    	String headerStartTag = "<h2>";
    	String headerEndTag = "</h2>";
    	int start = response.indexOf(headerStartTag) + headerStartTag.length();
    	int end = response.indexOf(headerEndTag);
    	return response.substring(start, end);
    }

    private String getScientificContactName(String response) {
    	int scientificContanctStartIndex = response.indexOf("Scientific contact");
    	int nameIndex = response.indexOf("Name", scientificContanctStartIndex);
    	String tdStartTag = "<td>";
    	int nameValueStartindex = response.indexOf(tdStartTag, nameIndex) + tdStartTag.length();
    	String tdEndTag = "</td>";
    	int nameValueEndIndex = response.indexOf(tdEndTag, nameValueStartindex);
        return response.substring(nameValueStartindex, nameValueEndIndex);
    }

    private String getScientificContactEmail(String response) {
    	int scientificContanctStartIndex = response.indexOf("Scientific contact");
    	String mailTo = "mailto:";
    	int mailToStartIndex = response.indexOf(mailTo, scientificContanctStartIndex) + mailTo.length();
    	int mailToEndIndex = response.indexOf("\">", mailToStartIndex);
        return response.substring(mailToStartIndex, mailToEndIndex);
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
}
