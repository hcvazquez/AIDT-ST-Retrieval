package external.wrappers;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import metasearch.Searcher;
import ner.EntityExtractor;
import ranking.RankedItem;
import ranking.Ranking;

@Deprecated
public class YARNWrapper implements Searcher
{
    //We need a real browser user agent or Google will block our request with a 403 - Forbidden
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    
    public Ranking search(String query, Proxy proxy){
    	
    	List<RankedItem> results = new ArrayList<RankedItem>();
    	
        Document doc;
		try {
			
			System.out.println("Starting connection with YARN...");
			if(proxy!=null){
				doc = Jsoup.connect("https://yarnpkg.com/packages?q=user").proxy(proxy).userAgent(USER_AGENT).timeout(0).get();
			}else{
				doc = Jsoup.connect("https://yarnpkg.com/packages?q=user").userAgent(USER_AGENT).timeout(0).get();	
			}
			
//			System.out.println("Connection with NPM finished...");

	        
	        System.out.println("Analizing Results...");
	        System.out.println(doc.toString());
	        for (Element result : doc.select("div")){
	        	//System.out.println(result.className());
	        	if(result.className().startsWith("ais-Hit--item")){	        		
	        
		            final String title = result.text();
		            final String url = result.attr("href");
		
		            //Now do something with the results (maybe something more useful than just printing to console)
		
		            System.out.println(title + " -> " + url); 
		            
		            results.add(new RankedItem(title, 0.0));
		            
	        	}
	        }
	        
        
		} catch (IOException e1) {
			System.out.println("Error estableciendo conexion con YARN.");;
		}
        
		return new Ranking(results);

    }

	@Override
	public String getId() {
		return "yarn.com";
	}
	
	@Override
	public String getContentId() {
		return "yarn.com";
	}

	@Override
	public List acquireData(String query, Proxy proxy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ranking processData(List<String> contents) {
		// TODO Auto-generated method stub
		return null;
	}

}