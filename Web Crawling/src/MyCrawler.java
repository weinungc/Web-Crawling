import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private static final Pattern myPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?|pdf|docx?))$");
	InfoData mydata;
	
	public MyCrawler() {
		mydata = new InfoData();
	}

	/**
	 * This method receives two parameters. The first parameter is the page in which
	 * we have discovered this new url and the second parameter is the new url. You
	 * should implement this function to specify whether the given url should be
	 * crawled or not (based on your crawling logic). In this example, we are
	 * instructing the crawler to ignore urls that have css, js, git, ... extensions
	 * and to only accept urls that start with "http://www.ics.uci.edu/". In this
	 * case, we didn't need the referringPage parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		mydata.addAll(href);
		if (FILTERS.matcher(href).matches()) {
			return false;
		}
		
		if (myPatterns.matcher(href).matches()) {
			return true;
		}
		
		
		return href.startsWith("https://www.c-span.org/")||href.startsWith("http://www.c-span.org/");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 */
	@Override
	public void visit(Page page) {
		
		String url = page.getWebURL().getURL();
		System.out.println("content type: "+page.getContentType());
		//System.out.println("URL: " + url);
		//System.out.println("Status: " + page.getStatusCode());
		
		
		//HTML
		if (page.getParseData() instanceof HtmlParseData) {
			
			
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			mydata.addVisit(url, page.getContentData().length, links.size(), "text/html");
			//System.out.println("Text length: " + text.length());
			//System.out.println("Html length: " + html.length());
			//System.out.println("Number of outgoing links: " + links.size());
			for(WebURL link :links) {
				mydata.addAll(link.getURL());
			}
		}
		//doc, pdf, image 
		else if(page.getParseData() instanceof BinaryParseData) {
			System.out.println("get type in binary: " + page.getContentType());
			mydata.addVisit(url, page.getContentData().length, 0, page.getContentType());
			//BinaryParseData binaryParseData =(BinaryParseData) page.getParseData();
		}
	}
	
	/**
     * This function is called by controller to get the local data of this crawler when job is
     * finished
     */
    @Override
    public Object getMyLocalData() {
        return mydata;
    }
	
	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		//System.out.println("handlePageStatusCode: " + webUrl.getURL());
		System.out.println("statusCode: " + statusCode);
		mydata.addFetch(webUrl.getURL(), statusCode);
		
		if (statusCode != HttpStatus.SC_OK) {

			if (statusCode == HttpStatus.SC_NOT_FOUND) {
				logger.warn("Broken link: {}, this link was found in page: {}", webUrl.getURL(), webUrl.getParentUrl());
			} else {
				logger.warn("Non success status for link: {} status code: {}, description: ", webUrl.getURL(),
						statusCode, statusDescription);
			}
		}
	}
}