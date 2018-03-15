import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "data/crawl";
		int numberOfCrawlers = 7;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxPagesToFetch(20000);
		config.setMaxDepthOfCrawling(16);

		/*
		 * Since images are binary content, we need to set this parameter to true to
		 * make sure they are included in the crawl.
		 */
		config.setIncludeBinaryContentInCrawling(true);
		config.setIncludeHttpsPages(true);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first URLs that
		 * are fetched and then the crawler starts following links which are found in
		 * these pages
		 */
		controller.addSeed("https://www.c-span.org/");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code will
		 * reach the line after this only when crawling is finished.
		 */
		controller.start(MyCrawler.class, numberOfCrawlers);

		/*
		 * Start to collect data from mult-thread
		 * 
		 */
		 List<Object> crawlerLocalData = controller.getCrawlersLocalData();
		
		
		 ReportData reportdata = new ReportData();
		 for (Object localData : crawlerLocalData) {
		 reportdata.addData((InfoData) localData);
		 }
		
		 reportdata.calculate();
		 reportdata.outputFile();
		 System.out.println("finish task");
	}
}
