package com.charan.websites.sciencedirect;

import com.charan.websites.AbstractWebsiteHandler;
import com.charan.file.FileUility;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.util.*;

public class ScienceDirectHandler extends AbstractWebsiteHandler {

    //https://www.sciencedirect.com/search/advanced?qs=cancer&date=2018&show=25&sortBy=relevance
    String url;
    String outputFile;
    Map<String, String> emails;

    public ScienceDirectHandler(String url) {
        this.url = url;
        String s = "qs=";
        String e = "&";
        String term = url.substring(url.indexOf(s) + s.length(), url.indexOf(e));
        this.outputFile = "outputcsv" + "_sciencedirect_" + term + "_" + new Date().getTime() + ".csv";
    }

    public ScienceDirectHandler() {}

    public String processJournals(List<String> journalUrls) {
        String fData = "";
        ExtractJournalMetadataSD extractJournalMetadataSD = new ExtractJournalMetadataSD();
		for (String journalUrl : journalUrls) {
			String contents = extractJournalMetadataSD.getJournalMetadata(journalUrl);
			addEmailToQueue(contents);
//			String contents = getJournalMetadata(journalUrl);
			if (contents.indexOf("@") == -1)
				continue;
//			System.out.println("contents: " + contents);
			fData = fData + contents;
		}
		addEmailToQueue("Processing done for: " + url);
		return fData;
    }
    public List<String> extractJournalUrls(String url) {
        List<String> journalUrls = new ArrayList<>();
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements refs = doc.select("a[href^=/science/article/pii]");
            for (Element ref : refs) {
//                System.out.println();
                Attributes attributes = ref.attributes();
                String journalUrl = attributes.get("href");
                journalUrl = "https://www.sciencedirect.com" + journalUrl;
//                getJournalMetadata(journalUrl);
                if (map.get(journalUrl) == null) {
                    map.put(journalUrl, true);
                } else {
                    continue;
                }
                System.out.println("Journal link: " + journalUrl);
                journalUrls.add(journalUrl);
            }
        } catch (Exception e) {
            System.out.println("Exception for URL: " + url + "ignoring");
        }
        return journalUrls;
    }

//    public static void main(String[] args)  throws Exception {
//        ScienceDirectHandler jsoupTrial = new ScienceDirectHandler();
//        List<String> journalUrls = jsoupTrial.extractJournalUrls("https://www.sciencedirect.com/search/advanced?qs=cancer&date=2018&show=25&sortBy=relevance");
//        ExtractJournalMetadataSD scienceDirectHandler = new ExtractJournalMetadataSD();
//        String fData = "Title,Author,Email" + "\n";
//        for (String journalUrl : journalUrls) {
//            String contents = scienceDirectHandler.getJournalMetadata(journalUrl);
//            if (contents.indexOf("@") == -1)
//                continue;
//            System.out.println("contents: " + contents);
//            fData = fData + contents;
//        }
//        FileUility fileUility = new FileUility();
//        String fName = "outputcsv" + new Date().getTime() + ".csv";
//        fileUility.setOutputFileName(fName);
//        fileUility.writeOutputFile(fData);
//    }

//    @Override
//    public void handleRequest() {
//        List<String> journalUrls = extractJournalUrls(getUrl());
//        ExtractJournalMetadataSD processJournal = new ExtractJournalMetadataSD();
//        String fData = "Title,Author,Email" + "\n";
//        for (String journalUrl : journalUrls) {
//            String contents = processJournal.getJournalMetadata(journalUrl);
//            if (contents.indexOf("@") == -1)
//                continue;
//            System.out.println("contents: " + contents);
//            fData = fData + contents;
//        }
//        FileUility fileUility = new FileUility();
////        String fName = "outputcsv" + new Date().getTime() + ".csv";
//        fileUility.setOutputFileName(getOutputFileName());
//        fileUility.writeOutputFile(fData);
//    }

    public String getOutputFileName() {
//        if (url == null) {
//            return "NO_URL";
//        }
//        String s = "qs=";
//        String e = "&";
//        String term = url.substring(url.indexOf(s) + s.length(), url.indexOf(e));
//        String fName = "outputcsv" + "_sciencedirect_" + term + "_" + new Date().getTime() + ".csv";
//        return fName;
        return this.outputFile;
    }

    public Map<String, String> getEmails() {
        return emails;
    }

    public void setEmails(Map<String, String> emails) {
        this.emails = emails;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

}
