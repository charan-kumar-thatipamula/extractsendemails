package com.charan.websites;

import com.charan.websites.journalplos.JournalPlosHandler;
import com.charan.websites.sciencedirect.ScienceDirectHandler;
import com.charan.websites.umin.UminHandler;

public class WebsiteHandlerFactory {
	public static AbstractWebsiteHandler getWebsiteHandler(String url) {
		if (url.indexOf("journals.plos") != -1) {
			return new JournalPlosHandler(url);
		} else if (url.indexOf("sciencedirect.com") != -1) {
			return new ScienceDirectHandler(url);
		} else if (url.indexOf("umin") != -1) {
			return new UminHandler(url);
		}
		return null;
	}
}
