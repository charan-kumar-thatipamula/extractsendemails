package com.charan.websites;

import com.charan.communication.email.Email;
import com.charan.communication.email.GetEmailFromTemplate;
import com.charan.file.FileUility;
import com.charan.global.GlobalContext;
import com.charan.websites.sciencedirect.ExtractJournalMetadataSD;
//import jdk.nashorn.internal.objects.Global;

import java.util.List;

public abstract class AbstractWebsiteHandler {

	String url;

	public void handleRequest() {
		List<String> journalUrls = extractJournalUrls(getUrl());
		String header = "Author,Email,Title" + "\n";
		String fData = processJournals(journalUrls);
		fData = header + fData;
//		ExtractJournalMetadataSD extractJournalMetadataSD = new ExtractJournalMetadataSD();
//		for (String journalUrl : journalUrls) {
////			String contents = extractJournalMetadataSD.getJournalMetadata(journalUrl);
//			String contents = getJournalMetadata(journalUrl);
//			if (contents.indexOf("@") == -1)
//				continue;
//			System.out.println("contents: " + contents);
//			fData = fData + contents;
//		}
		FileUility fileUility = new FileUility();
//        String fName = "outputcsv" + new Date().getTime() + ".csv";
		fileUility.setOutputFileName(getOutputFileName());
		System.out.println("Done processing: " + getUrl());
		fileUility.writeOutputFile(fData);
		System.out.println("Extracted data to: " + getOutputFileName());
		System.out.println("******************************************");
	}

	public abstract String getUrl();

	public void setUrl(String url) {
		this.url = url;
	}

//	public abstract void setOutputCSVName(String csvName);

	public abstract List<String> extractJournalUrls(String url);

	public abstract String processJournals(List<String> journalUrls);

	public abstract String getOutputFileName();

	public void addEmailToQueue(String contents) {
//		GlobalContext gc = GlobalContext.getGlobalContext();
//		String[] row = contents.split(",");
//		GetEmailFromTemplate et = new GetEmailFromTemplate();
//		et.setTemplateFilePath(gc.getTemplatePathForUrl(getUrl()));
//		Email email =  et.extractEmail(row);
//		gc.addToQueue(getUrl(), email);
//		System.out.println("Added email: " + email.toString());
	}
}
