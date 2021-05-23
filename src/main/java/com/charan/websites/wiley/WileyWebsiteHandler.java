package com.charan.websites.wiley;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.charan.util.Constants;
import com.charan.websites.ReadPagesWebsiteHandler;

public class WileyWebsiteHandler extends ReadPagesWebsiteHandler{
	
	String searchTermQueryParam = Constants.WileyWebsiteSearchTermQueryParam;
	
	public WileyWebsiteHandler(String url) {
		this.url = url;
		this.journalPageTemplate = Constants.WileyWebsiteJournalPageTemplate;
		this.outputFile = "outputcsv" + "_onlinelibrarywiley_" + getSearchTerm() + "_" + new Date().getTime() + ".csv";
	}
	
	@Override
	public List<String> extractJournalIdentifiers(String journalsListPageData) {
		List<String> journalIds = new ArrayList<String>();
		int sInd = 0;
		int pageLength = journalsListPageData.length();
		while (sInd != -1 && sInd < pageLength) {
			String keyword1 = Constants.WileyWebsiteJournalListPageKeyword1;
			String keyword2 = Constants.WileyWebsiteJournalListPageKeyword2;
			sInd = journalsListPageData.indexOf(keyword1, sInd);
	        if (sInd != -1) {
	            sInd = journalsListPageData.indexOf(keyword2, sInd + keyword1.length());
	            if (sInd == -1) {
	            	continue;
	            }
	            sInd += keyword2.length();
	        } else {
	        	continue;
	        }
	        String tResponse = journalsListPageData.substring(sInd);
			int eInd = tResponse.indexOf("\"");
			String journalId = tResponse.substring(0, eInd);
			if (journalId == null || journalId.length() == 0) {
				continue;
			}
			journalId = journalId.trim();
			journalIds.add(journalId);
		}
		return journalIds;
	}

	@Override
	public List<String> extractJournalContents(String journalPageData) {
		List<String> journalContents = new ArrayList<String>();
		if (journalPageData == null || journalPageData.length() == 0) {
			return null;
		}
		
		String authorName = getAuthorName(journalPageData);
		String email = getEmail(journalPageData);
		String title = getTitle(journalPageData);
		
		if (authorName == null || email == null || title == null) {
			return journalContents;
		}
		journalContents.add(String.join(",", authorName, email, title));
		return journalContents;
	}

	private String getAuthorName(String journalPageData) {
		
		// <p><b>Corresponding author:</b> Jiemin Ma, PhD, MHS, Surveillance and Health Services Research, American Cancer Society, 250 Williams St, Atlanta, GA 30303; <a class="corr-email" title="Link to email address" 
				// href="mailto:jiemin.ma@cancer.org"><span>jiemin.ma@cancer.org</span>
		// <b>Correspondence to:</b> Dr Nathalie Kliemann, E-mail: <a class="corr-email" title="Link to email address" 
				// href="mailto:kliemannn@fellows.iarc.fr"><span>kliemannn@fellows.iarc.fr</span>
		
		int sInd = getNameEmailRealtedSectionSInd(journalPageData);
		if (sInd == -1) {
			return null;
		}
		String remainingPage = journalPageData.substring(sInd);
		int eInd = remainingPage.indexOf(",");
		if (eInd == -1) {
			return null;
		}
		remainingPage = remainingPage.substring(0, eInd);
		sInd = remainingPage.indexOf(Constants.WileyWebsiteJournalPageKW2_1);
		if (sInd == -1) {
			sInd = remainingPage.indexOf(Constants.WileyWebsiteJournalPageKW2_4);
			if (sInd == -1) {
				return null;
			}
			sInd += Constants.WileyWebsiteJournalPageKW2_4.length();
		} else {
			sInd = remainingPage.indexOf(Constants.WileyWebsiteJournalPageKW2_3, sInd);
		}
		if (sInd == -1) {
			return null;
		}
		return remainingPage.substring(sInd, eInd).replaceAll(":", "").trim();
	}

	private String getEmail(String journalPageData) {		
		int sInd = getNameEmailRealtedSectionSInd(journalPageData);
		if (sInd == -1) {
			return null;
		}
		String remainingPage = journalPageData.substring(sInd);
		sInd = remainingPage.indexOf(Constants.WileyWebsiteJournalPageKW3);
		if (sInd == -1) {
			return null;
		}
		sInd +=  Constants.WileyWebsiteJournalPageKW3.length();
		int eInd = remainingPage.indexOf("\"", sInd);
		
		if (eInd == -1) {
			return null;
		}
		
		return remainingPage.substring(sInd, eInd).trim();
	}

	private String getTitle(String journalPageData) {
		// <meta name="citation_title" content="Predicted basal metabolic rate and cancer risk in the European Prospective Investigation into Cancer and Nutrition">
		int sInd = journalPageData.indexOf(Constants.WileyWebsiteJournalPageTitleKeyword);
		if (sInd == -1) {
			return null;
		}
		
		sInd += Constants.WileyWebsiteJournalPageTitleKeyword.length();
		
		int eInd = journalPageData.indexOf("\"", sInd);
		
		if (eInd == -1) {
			return null;
		}
		
		return journalPageData.substring(sInd, eInd).replaceAll(",", ";");
	}


	private int getNameEmailRealtedSectionSInd(String journalPageData) {
		// <p><b>Corresponding author:</b> Jiemin Ma, PhD, MHS, Surveillance and Health Services Research, American Cancer Society, 250 Williams St, Atlanta, GA 30303; <a class="corr-email" title="Link to email address" 
				// href="mailto:jiemin.ma@cancer.org"><span>jiemin.ma@cancer.org</span>
		// <b>Correspondence to:</b> Dr Nathalie Kliemann, E-mail: <a class="corr-email" title="Link to email address" 
				// href="mailto:kliemannn@fellows.iarc.fr"><span>kliemannn@fellows.iarc.fr</span>
		int sInd = journalPageData.toLowerCase().indexOf(Constants.WileyWebsiteJournalPageKW1_0.toLowerCase());
		if (sInd == -1) {
			sInd = journalPageData.toLowerCase().indexOf(Constants.WileyWebsiteJournalPageKW1_1.toLowerCase());
		}
		if (sInd == -1) {
			sInd = journalPageData.toLowerCase().indexOf(Constants.WileyWebsiteJournalPageKW1_2.toLowerCase());
		}
		return sInd;
	}
	
	@Override
	public String getSearchTerm() {
		if (this.url == null || this.url.length() == 0 || this.url.indexOf(searchTermQueryParam) == -1) {
			return null;
		}
		int stQueryParamIndex = this.url.indexOf(searchTermQueryParam);
		if (stQueryParamIndex == -1) {
			return null;
		}
		return this.url.substring(stQueryParamIndex + searchTermQueryParam.length(), this.url.indexOf("&", stQueryParamIndex));
	}

}
