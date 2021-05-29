package com.charan.websites;

import com.charan.websites.degruyter.DegruyterWebsiteHandler;
import com.charan.websites.journalplos.JournalPlosHandler;
import com.charan.websites.rctportal.RctPortalHandler;
import com.charan.websites.sciencedirect.ScienceDirectHandler;
import com.charan.websites.umin.UminHandler;
import com.charan.websites.wiley.WileyWebsiteHandler;

public class WebsiteHandlerFactory {
	public static AbstractWebsiteHandler getWebsiteHandler(String url) {
		if (url.indexOf("journals.plos") != -1) {
			return new JournalPlosHandler(url);
		} else if (url.indexOf("sciencedirect.com") != -1) {
			return new ScienceDirectHandler(url);
		} else if (url.indexOf("umin") != -1) {
			return new UminHandler(url);
		} else if (url.indexOf("rctportal") != -1) {
			return new RctPortalHandler(url);
		} else if (url.indexOf("wiley") != -1) {
			return new WileyWebsiteHandler(url);
		} else if (url.indexOf("degruyter") != -1) {
			return new DegruyterWebsiteHandler(url);
		}
		return null;
	}
}
