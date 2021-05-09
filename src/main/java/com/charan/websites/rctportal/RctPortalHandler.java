package com.charan.websites.rctportal;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.charan.httprequest.SendRequest;
import com.charan.websites.AbstractWebsiteHandler;

public class RctPortalHandler extends AbstractWebsiteHandler {

	String url;
    String outputFile;

    public RctPortalHandler(String url) {
        this.url = url;
        String s = "q=";
        String e = "&";
        String term = url.substring(url.indexOf(s) + s.length(), url.length());
        term = term.substring(0, term.indexOf("&"));
        this.outputFile = "outputcsv" + "_rctportal_" + term + "_" + new Date().getTime() + ".csv";
    }
    
    @Override
	public String getUrl() {
		return url;
	}

	@Override
	public List<String> extractJournalUrls(String url) {
		
		String urlPrefix = "https://rctportal.niph.go.jp";
        String keyword = "/en/detail?trial_id=";
        List<String> journalUrls = new ArrayList<>();
        try {
//            Document doc = Jsoup.connect(url).get();
            SendRequest sReq = new SendRequest();
            String response = sReq.sendGet(url);
//            System.out.println(response);
            // response.inde
            int sInd = response.indexOf(keyword);
            while (sInd != -1) {
            	int end = response.indexOf("\">", sInd);
            	if (end == -1) {
            		break;
            	}
            	String jId = response.substring(sInd, end);
            	jId = urlPrefix + jId;
            	journalUrls.add(jId);
            	sInd = response.indexOf(keyword, end);
            }
        } catch (Exception e) {
            System.out.println("Exception for URL: " + url + "ignoring; " + e.getMessage());
        }
        return journalUrls;
    }

	@Override
	public String processJournals(List<String> journalUrls) {
		List<String> finalData = new LinkedList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        rctExtractJournalMetadata rctExtractJournalMetadata = new rctExtractJournalMetadata();
        for (String journalUrl : journalUrls) {
//            Runnable rctExtractJournalMetadata = new rctExtractJournalMetadata();
//            ((rctExtractJournalMetadata)rctExtractJournalMetadata).setJournalUrl(journalUrl);
//            ((rctExtractJournalMetadata)rctExtractJournalMetadata).setFinalData(finalData);
//            executorService.execute(rctExtractJournalMetadata);
        	RctExtractJournalMetadata rctExtractJournalMetadata = new RctExtractJournalMetadata();
            rctExtractJournalMetadata.setJournalUrl(journalUrl);
            rctExtractJournalMetadata.setFinalData(finalData);
            rctExtractJournalMetadata.getJournalMetadata();
//            String contents = rctExtractJournalMetadata.getJournalMetadata();
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

	@Override
	public String getOutputFileName() {
		return this.outputFile;
	}

}
