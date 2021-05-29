package com.charan.websites.degruyter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.charan.util.Constants;
import com.charan.util.Helper;
import com.charan.websites.ReadPagesWebsiteHandler;

public class DegruyterWebsiteHandler extends ReadPagesWebsiteHandler{

	String searchTermQueryParam = Constants.DegruyterSearchTermQueryParam;
	
	public DegruyterWebsiteHandler(String url) {
		this.url = url;
		this.journalPageTemplate = Constants.DegruyterWebsiteJournalPageTemplate;
		this.outputFile = "outputcsv" + "_onlinelibrarydegruyter_" + getSearchTerm() + "_" + new Date().getTime() + ".csv";
	}
	
	@Override
	public List<String> extractJournalIdentifiers(String journalsListPageData) {
		String prefix = "text-dark\sfont-weight-bold\"\\shref=\"";
		String midSection = "(.*?)";
		String suffix = "\"";
		
		return Helper.ExtractPatters(journalsListPageData, prefix, midSection, suffix);
	}

	@Override
	public List<String> extractJournalContents(String journalPageData) {
		if (journalPageData == null || journalPageData.length() == 0) {
			return null;
		}
		
		String title = getTitle(journalPageData);
		
 		if (title == null) {
			return null;
		}
		
		List<String> sectionsWithEmailName = getSectionsWithEmailName(journalPageData);
		
		if (sectionsWithEmailName == null || sectionsWithEmailName.size() == 0) {
			return null;
		}

		
		return sectionsWithEmailName.stream().map(sectionWithEmailName -> {
			String email = getEmail(sectionWithEmailName);
			String authorName = getAuthorName(sectionWithEmailName);
			if (email == null || email.length() == 0 || authorName == null || authorName.length() == 0) {
				return null;
			}
			return String.join(",", authorName, email, title);
		})
		.filter(content -> content != null && content.length() >0)
		.collect(Collectors.toList());
	}

	private List<String> getSectionsWithEmailName(String journalPageData) {
		String px1 = "contributor\smetadataInfoFont";
		String ms1 = "(.*?)";
		String sx1 = "</span>";
		
		List<String> sectionsWithEmailName = Helper.ExtractPatters(journalPageData, px1, ms1, sx1);
		return sectionsWithEmailName;
	}

	private String getAuthorName(String sectionWithEmailName) {
		// <span class="contributor metadataInfoFont" data-toggle="tooltip" data-placement="bottom" title="Department of Pharmacognosy, Manipal College of Pharmaceutical Sciences, Manipal Academy of Higher Education, Manipal, Karnataka, India; richardlobo73@gmail.com">Richard Lobo</span>
		String pxName = "\">";
		String msName = "(.*?)";
		String sxName = "</span>";
		List<String> names = Helper.ExtractPatters(sectionWithEmailName + sxName, pxName, msName, sxName);
		if (names == null || names.size() == 0 || names.get(0) == null || names.get(0).length() == 0) {
			return null;
		}
		return names.get(0);
	}

	private String getEmail(String sectionWithEmailName) {	
		// <span class="contributor metadataInfoFont" data-toggle="tooltip" data-placement="bottom" title="Department of Pharmacognosy, Manipal College of Pharmaceutical Sciences, Manipal Academy of Higher Education, Manipal, Karnataka, India; richardlobo73@gmail.com">Richard Lobo</span>
		String pxEmail = "(;\s)(.*?)@";
		String msEmail = "(.*?)";
		String sxEmail = "\\.(.*?)\"";
		List<String> emails = Helper.ExtractPattersWithoutTrim(sectionWithEmailName, pxEmail, msEmail, sxEmail);
		
		if (emails == null || emails.size() == 0 || emails.get(0) == null || emails.get(0).length() == 0) {
			return null;
		}
		// ; richardlobo73@gmail.com"
		if (emails.get(0).indexOf("; ", "; ".length()) != -1) {
			return getEmail(emails.get(0).substring("; ".length()));
		}
		return emails.get(0).substring("; ".length(), emails.get(0).length()-1);
	}

	private String getTitle(String journalPageData) {
		// <h1>Mechanistic targets for BPH and prostate cancer–a review</h1>
		
		List<String> titles = Helper.ExtractPatters(journalPageData, "<h1>", "(.*?)", "</h1>");
		if (titles == null || titles.size() == 0) {
			return null;
		}
		return titles.get(0);
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
