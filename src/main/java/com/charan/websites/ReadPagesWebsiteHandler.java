package com.charan.websites;

import java.util.List;
import java.util.stream.Collectors;
import com.charan.httprequest.SendRequest;
import com.charan.util.Constants;
import com.charan.util.WebsiteUtil;

public abstract class ReadPagesWebsiteHandler extends AbstractWebsiteHandler {

	protected String journalPageTemplate;
	protected String outputFile;
	
	@Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    public String getOutputFileName() {
      return this.outputFile;
    }
    
	@Override
	public List<String> extractJournalUrls(String url) {
		try {
//			SendRequest sReq = new SendRequest();
//            String journalListPageData = sReq.sendGet(url);
			String journalListPageData = WebsiteUtil.readWebpage(url);
            if (journalListPageData == null || journalListPageData.length() == 0) {
            	return null;
            }
            List<String> journalIdentifiers = extractJournalIdentifiers(journalListPageData);
            return journalIdentifiers.stream()
			            .map(journalId -> { return fetchJournalUrlFromTemplate(journalId);})
			            .collect(Collectors.toList());
		} catch (Exception e) {
			System.err.println("Unable to process journal: " + url + ". Error: " + e.getMessage());
		}
		return null;
	}

	protected String fetchJournalUrlFromTemplate(String... journalIdentifier) {
		return String.format(journalPageTemplate, journalIdentifier);
	}

	@Override
	public String processJournals(List<String> journalUrls) {
		List<String> authorEmailTitleList = journalUrls.stream()
			.map(journalUrl -> {
				try {
//					SendRequest sReq = new SendRequest();
//		            String journalPageData = sReq.sendGet(journalUrl);
					System.out.println("\nProcessing journal: " + journalUrl);
					String journalPageData = WebsiteUtil.readWebpage(journalUrl);
		            List<String> authorEmailTitles = extractJournalContents(journalPageData);
		            if (authorEmailTitles == null || authorEmailTitles.size() == 0) {
			            System.out.println("	NO RESULTS FOUND");		            	
		            } else {
			            System.out.println("	" + String.join("; ", authorEmailTitles));
		            }
		            return authorEmailTitles;
				} catch (Exception e) {
					System.out.println("Failed with message: " + e.getMessage());
				}
				return null;
			})
			.filter(authorEmailTitles -> authorEmailTitles != null && authorEmailTitles.size() > 0)
			.flatMap(authorEmailTitles -> authorEmailTitles.stream())
			.collect(Collectors.toList());
		return String.join("\n", authorEmailTitleList);
	}

	public abstract List<String> extractJournalIdentifiers(String journalsListPageData);
	public abstract List<String> extractJournalContents(String journalPageData);
	public abstract String getSearchTerm();
}
