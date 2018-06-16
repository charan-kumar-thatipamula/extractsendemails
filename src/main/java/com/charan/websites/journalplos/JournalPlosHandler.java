package com.charan.websites.journalplos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.charan.factory.ResponseHandlerFactory;
import com.charan.httprequest.SendRequest;
import com.charan.util.ResponseHandler;
import com.charan.websites.AbstractWebsiteHandler;

public class JournalPlosHandler extends AbstractWebsiteHandler {
	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	//http://journals.plos.org/plosone/dynamicSearch?resultsPerPage=60&q=author_affiliate%3A%22genetics+%22&sortOrder=DATE_NEWEST_FIRST&page=1
	String url;
	String outputFile;
	Map<String, String> emails;
	public JournalPlosHandler(String url) {
		this.url = url;
		this.outputFile = "outputcsv" + new Date().getTime() + ".csv";
	}
//	public void handleRequest() {
//		System.out.println("****** Processing URL: " + this.url + "*****");
//		SendRequest sReq = new SendRequest();
//		ResponseHandlerFactory rhfactory = new ResponseHandlerFactory();
//		try {
//			String response = sReq.sendGet(this.url);
//			ResponseHandler resHandler = rhfactory.getResponseHandler(url);
//			String[] journals = resHandler.handleResponse(response);
//
//			handleJournals(journals);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	public List<String> extractJournalUrls(String url) {
		System.out.println("****** Processing URL: " + this.url + "*****");
		SendRequest sReq = new SendRequest();
		ResponseHandlerFactory rhfactory = new ResponseHandlerFactory();
		List<String> list = null;
		try {
			String response = sReq.sendGet(this.url);
			ResponseHandler resHandler = rhfactory.getResponseHandler(this.url);
			String[] journals = resHandler.handleResponse(response);

			list = Arrays.asList(journals);
//			handleJournals(journals);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String processJournals(List<String> journals) {
		String domain = "http://journals.plos.org";
		String contents = ""; // getHeader();
		int i = 0;
		emails = new HashMap<>();
		for (String journal : journals) {
			journal = journal.replace("/article?", "/article/authors?");
			journal = domain + journal;
//			System.out.println(journal);
			System.out.println("Journal: " + journal);
			String details  = getDetails(journal);
			addEmailToQueue(details);
			if (details != null) {
				contents = contents.concat(details);
			}
			i++;
//			if (i == 5) {
//				break;
//			}
//			System.out.println("\n");
//			break;
		}
		System.out.println();
//		writeOutputFile(contents);
		addEmailToQueue("Processing done for: " + url);
		return contents;
	}

	private void writeOutputFile(String contents) {
		BufferedWriter writer = null;
		try {
			String outputFileName = getOutputFileName();
			System.out.println(outputFileName);
			writer = new BufferedWriter(new FileWriter(outputFileName));
			writer.write(contents);
		} catch (IOException e) {
			System.out.println("Error in writing output file");
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
//	private String getHeader() {
//		return "Title,Author,Email" + "\n";
//	}
	private String getDetails(String journal) {
		String details = "";
		SendRequest sReq = new SendRequest();
		try {
			String response = sReq.sendGet(journal);
//			System.out.println("Response while accessing journal");
//			System.out.println(response);
//			extractName(response);
//			extractEmail(response);
			String title = extractTitle(response);
//			System.out.println("title: " + title);
			title = title.replaceAll(",", "");
//			details = details.concat(title + ",");
			int start = response.indexOf("mailto:");
			int last = response.indexOf("\">", start);
			String email = response.substring(start + "mailto:".length(), last);
			if (email !=null ) {
				email = email.trim();
			}
			email = email.replaceAll(",", "");
//			System.out.println("email: " +email);

			String subRes = response.substring(0, start);
//			System.out.println(subRes);
			start = subRes.lastIndexOf("\"author-name\" >");
			last = subRes.indexOf("<span class=\"email\">");
			String name = response.substring(start + "\"author-name\" >".length(), last);
			if (name != null) {
				name = name.substring(0, (name.indexOf("<") != -1 ? name.indexOf("<") : name.length()));
				name = name.trim();
			}
			name = name.replaceAll(",", "");
			details = details.concat(name + ",");
			details = details.concat(email + ",");
			details = details.concat(title);
			if (emails.get(email) != null) {
				return null;
			} else {
				emails.put(email, "yes");
			}
//			System.out.println("name: " + name);
		} catch (Exception e) {
			System.out.println("Exception in handleJournals JournalPlosHandler: " + e.getMessage());
		}
		
		return details + "\n";
	}
	private String extractTitle(String response) {
		String title = null;
		if (response == null || response.length() == 0) {
			return title;
		}
		int start = response.indexOf("<title>");
		int end = response.indexOf("</title>");
		String s = response.substring(start + "<title>".length(), end);
//		System.out.println(s);
//		System.out.println(s.indexOf(":") + 1);
		title = s.substring(s.indexOf(":") + 1, s.length());
		if (title !=null) {
			title = title.trim();
		}
//		System.out.println(title);
		return title;
	}
//	private String extractName(String response) {
//		
//	}

	public String getOutputFileName() {
		return this.outputFile;
	}

	public Map<String, String> getEmails() {
		return emails;
	}

	public void setEmails(Map<String, String> emails) {
		this.emails = emails;
	}
}
