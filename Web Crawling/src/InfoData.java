import java.util.ArrayList;
import java.util.List;

public class InfoData {
	//fetch_NewsSite.csv
	//first csv -> URL,code_Status
	
	//visit_NewsSite.csv
	//second csv -> URL_you download,size,# of outlinks found,resulting content-type
	
	List<Fetch> fetch;
	List<Visit> visit;
	List<String> all;
	
	public InfoData() {
		fetch = new ArrayList<Fetch>();
		visit = new ArrayList<Visit>();
		all = new ArrayList<String>();
		
	}
	
	void addFetch(String URL, int status) {
		fetch.add(new Fetch(URL,status));
	}
	
	void addVisit(String URL,int size,int numsofOutlink,String content_type) {
		visit.add(new Visit(URL,size,numsofOutlink,content_type));
	}
	
	void addAll(String URL) {
		all.add(URL);
	}
	
	List<Fetch> getFetch(){
		return fetch;
	}
	
	List<Visit> getVisit(){
		return visit;
	}
	
	List<String> getAll(){
		return all;
	}
	
	
	
	
}

class Fetch{
	String URL;
	int status;
	
	Fetch(String URL,int status){
		this.URL = URL;
		this.status = status;
	}
	
	public void set_URL(String URL) {
		this.URL = URL;
	}
	
	public void set_status(int status) {
		this.status = status;
	}
	
	public String get_URL() {
		return URL;
	}
	public int get_status() {
		return status;
	}
	
	
}

class Visit{
	String URL;
	int size;
	int numsofOutlink;
	String content_type;
	
	Visit(String URL, int size, int numsofOutlink, String content_type){
		this.URL = URL;
		this.size = size;
		this.numsofOutlink = numsofOutlink;
		this.content_type = content_type;
	}
	
	String get_URL() {
		return URL;
	}
	int get_size() {
		return size;
	}
	
	int get_numsofOutlink() {
		return numsofOutlink;
	}
	String get_content_type() {
		return content_type;
	}
}

class All{
	String URL;
	boolean isInDomain;
	
	All(String URL, boolean isInDomain){
		this.URL = URL;
		this.isInDomain = isInDomain;
	}
	
	String get_URL() {
		return URL;
	}
	
	boolean get_isInDomain(){
		return isInDomain;
	}
	
}
