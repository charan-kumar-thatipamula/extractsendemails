package com.charan;
import java.util.*;

import com.charan.communication.ParellelProcessCSVsForEmail;
import com.charan.global.GlobalContext;
import com.charan.global.UrlContext;
import com.charan.util.creds.CredentialsUtil;
import com.charan.websites.AbstractWebsiteHandler;
import com.charan.websites.WebsiteHandlerFactory;

public class TriggerApp {

	private void process() throws Exception{
		if (notValid()) {
			return;
		}
		List<String> urls = readUrls();

		ParellelProcessCSVsForEmail parellelProcessCSVsForEmail = new ParellelProcessCSVsForEmail();
		for (String url1 : urls) {
			AbstractWebsiteHandler wh = WebsiteHandlerFactory.getWebsiteHandler(url1);
			wh.handleRequest();
			parellelProcessCSVsForEmail.addCSVFileJobs(wh.getOutputFileName());
		}
//		parellelProcessCSVsForEmail.triggerEmail();
	}
	private List<String> readUrls() throws Exception{
		Scanner scanner = new Scanner(System.in);
		List<String> urls = new ArrayList<String>();
		UrlContext urlContext = new UrlContext();
		while (true) {
			System.out.println("Enter URL to extract journal details or Press 'Enter'");
			String input = scanner.nextLine();
			if (input == null || input.length() == 0) {
				break;
			}
			String[] inputContents = parseInput(input);
			try {
				urlContext.setUrlContext(inputContents);
				urls.add(inputContents[0]);
			} catch (Exception e) {
				System.err.println("Exception setting url context: " + e.getMessage());
				System.out.println("URL: [" + inputContents[0] + "] is not added for processing");
			}
		}
		scanner.close();
		return urls;
	}


	private String[] parseInput(String input) {
		String[] contents = input.split(",");
		return contents;
	}

	public boolean notValid() {
		Calendar cal = Calendar.getInstance();
		// You cannot use Date class to extract individual Date fields
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);      // 0 to 11
		int day = cal.get(Calendar.DAY_OF_MONTH);
//		System.out.println(year + " " + month + " " + day);
		if (year > 2018)
			return true;
		if (month > 6)
			return true;
		return false;
	}
	public static void main(String[] args) {
		try {
			GlobalContext globalContext = GlobalContext.getGlobalContext();
			CredentialsUtil credentialsUtil = globalContext.getCredentialsUtil();
			credentialsUtil.setDefaultCreds();

			TriggerApp triggerApp = new TriggerApp();
			triggerApp.process();
		} catch (Exception e) {

		}
	}
}
