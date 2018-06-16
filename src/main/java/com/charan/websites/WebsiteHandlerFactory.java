package com.charan.websites;

import com.charan.websites.journalplos.JournalPlosHandler;
import com.charan.websites.sciencedirect.ScienceDirectHandler;

public class WebsiteHandlerFactory {
	public static AbstractWebsiteHandler getWebsiteHandler(String url) {
		if (url.indexOf("journals.plos") != -1) {
			return new JournalPlosHandler(url);
		} else if (url.indexOf("sciencedirect.com") != -1) {
			return new ScienceDirectHandler(url);
		}
		return null;
	}
}
