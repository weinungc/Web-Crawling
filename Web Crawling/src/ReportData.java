import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ReportData {
	InfoData mydata;
	int status_200;
	int status_301;
	int status_401;
	int status_403;
	int status_404;
	int fetches_attempted;
	int fetches_succeeded;
	int fetches_aborted;
	int Total_URLs_extracted;
	int unique_URLs_extracted;
	int unique_URLs_within_News_Site;
	int unique_URLs_outside_News_Site;
	int less_1KB;
	int less_10KB;
	int less_100KB;
	int less_1MB;
	int more_1MB;
	Map<String, Integer> content_type;
	Map<Integer,Integer> statsus_temp;

	ReportData() {
		mydata = new InfoData();
		status_200 = 0;
		status_301 = 0;
		status_401 = 0;
		status_403 = 0;
		status_404 = 0;
		fetches_attempted = 0;
		fetches_succeeded = 0;
		fetches_aborted = 0;
		Total_URLs_extracted = 0;
		unique_URLs_extracted = 0;
		unique_URLs_within_News_Site = 0;
		unique_URLs_outside_News_Site = 0;
		less_1KB = 0;
		less_10KB = 0;
		less_100KB = 0;
		less_1MB = 0;
		more_1MB = 0;
		content_type = new HashMap<String, Integer>();
		statsus_temp = new HashMap<Integer,Integer>();

	}

	void addData(InfoData data) {
		mydata.getFetch().addAll(data.getFetch());
		mydata.getVisit().addAll(data.getVisit());
		mydata.getAll().addAll(data.getAll());

	}

	void calculate() {
		for (Fetch fetch : mydata.getFetch()) {
			// count fetch
			int number = fetch.get_status() / 100;
			fetches_attempted++;
			if (number == 2)
				fetches_succeeded++;
			else if (number == 3 || number == 4 || number == 5)
				fetches_aborted++;
			// count status
			if (fetch.get_status() == 200)
				status_200++;
			else if (fetch.get_status() == 301)
				status_301++;
			else if (fetch.get_status() == 401)
				status_401++;
			else if (fetch.get_status() == 403)
				status_403++;
			else if (fetch.get_status() == 404)
				status_404++;
			
			if(statsus_temp.containsKey(fetch.get_status())) {
				statsus_temp.put(fetch.get_status(), statsus_temp.get(fetch.get_status())+1);
			}else {
				statsus_temp.put(fetch.get_status(), 1);
			}

		}

		for (Visit visit : mydata.getVisit()) {
			// count size
			if (visit.size < 1024) {
				less_1KB++;
			} else if (visit.get_size() < 1024 * 10) {
				less_10KB++;
			} else if (visit.get_size() < 1024 * 100) {
				less_100KB++;
			} else if (visit.get_size() < 1024 * 1024) {
				less_1MB++;
			} else {
				more_1MB++;
			}
			// count content type
			if (content_type.containsKey(visit.get_content_type())) {
				content_type.put(visit.get_content_type(), content_type.get(visit.get_content_type()) + 1);
			} else {
				content_type.put(visit.get_content_type(), 1);
			}

		}

		Set<String> unique_url = new HashSet<String>(mydata.getAll());
		Total_URLs_extracted = mydata.getAll().size();
		unique_URLs_extracted = unique_url.size();

		for (String all : unique_url) {
			if (all.startsWith("https://www.c-span.org/") || all.startsWith("http://www.c-span.org/")) {
				unique_URLs_within_News_Site++;
			} else {
				unique_URLs_outside_News_Site++;
			}
		}
	}

	boolean outputFile() {

		try {
			
			File fe = new File("fetch_NewsSite.csv");
			FileWriter fe_writer = new FileWriter(fe);
			fe.createNewFile();
			for (Fetch fetch : mydata.getFetch()) {
				fe_writer.write(fetch.get_URL() + "," + fetch.get_status() + "\n");
			}
			fe_writer.close();
			
			File vi = new File("visit_NewsSite.csv");
			vi.createNewFile();
			FileWriter vi_writer = new FileWriter(vi);
			for (Visit visit : mydata.getVisit()) {
				vi_writer.write(visit.get_URL() + "," + visit.get_size() + "bytes," + visit.get_numsofOutlink() + ","
						+ visit.get_content_type() + "\n");
			}
			vi_writer.close();
			
			File al = new File("urls_C-Span.csv");
			al.createNewFile();
			FileWriter al_writer = new FileWriter(al);
			for (String all : mydata.getAll()) {
				if(all.startsWith("https://www.c-span.org/")||all.startsWith("http://www.c-span.org/"))
					al_writer.write(all + ",OK\n");
				else
					al_writer.write(all + ",N_OK\n");
			}
			al_writer.close();
			
			// write report
			File report = new File("CrawlReport_C-Span.txt");
			report.createNewFile();
			FileWriter report_writer = new FileWriter(report);
			
			
			report_writer.write("Name: Wei-Nung Chao\n");
			report_writer.write("USC ID: 1720632986\n");
			report_writer.write("News site crawled: https://www.c-span.org/\n");
			report_writer.write("Fetch Statistics \n");
			report_writer.write("================\n");
			report_writer.write("# fetches attempted: " + fetches_attempted + "\n");
			report_writer.write("# fetches succeeded: " + fetches_succeeded + "\n");
			report_writer.write("# fetches aborded or failed: " + fetches_aborted + "\n\n\n");

			report_writer.write("Outgoing URLs: \n");
			report_writer.write("============== \n");
			report_writer.write("Total URLs extracted: " + Total_URLs_extracted + "\n");
			report_writer.write("# unique URLs extracted: "+unique_URLs_extracted+"\n");
			report_writer.write("# unique URLs within News Site: "+unique_URLs_within_News_Site+"\n");
			report_writer.write("# unique URLs outside News Site: "+unique_URLs_outside_News_Site+"\n\n\n");

			report_writer.write("Status Codes: \n");
			report_writer.write("============= \n");
//			report_writer.write("200 OK: " + status_200 + "\n");
//			report_writer.write("301 Moved Permanently: " + status_301 + "\n");
//			report_writer.write("401 Unauthorized: " + status_401 + "\n");
//			report_writer.write("403 Forbidden: " + status_403 + "\n");
//			report_writer.write("404 Not Found: " + status_404 + "\n\n\n");
			
			SortedSet<Integer> keys = new TreeSet<Integer>(statsus_temp.keySet());
			for(int key : keys) {
				report_writer.write(key + ": " + statsus_temp.get(key) + "\n");
			}
			
			report_writer.write("\n\n");

			report_writer.write("File Sizes: \n");
			report_writer.write("=========== \n");
			report_writer.write("< 1KB: " + less_1KB + "\n");
			report_writer.write("1KB ~ <10KB: " + less_10KB + "\n");
			report_writer.write("10KB ~ <100KB: " + less_100KB + "\n");
			report_writer.write("100KB ~ <1MB: " + less_1MB + "\n");
			report_writer.write(">= 1MB: " + more_1MB + "\n\n\n");

			report_writer.write("Content Types:");
			report_writer.write("============== \n");
			for (Map.Entry<String, Integer> entry : content_type.entrySet()) {
				report_writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
			}
			report_writer.close();

			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}
