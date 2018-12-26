package external.wrappers;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import metasearch.Searcher;
import ner.EntityExtractor;
import ranking.RankedItem;
import ranking.Ranking;
import util.PackageManager;
import util.StopWordManager;

@Deprecated
public class DuckWrapper implements Searcher
{
    //We need a real browser user agent or Google will block our request with a 403 - Forbidden
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    public static final String USER_AGENT2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";
    public static void main(String[] args) throws Exception {
    	
    	//search2("user",null);
               
    }
    
    public  Ranking search(String query, Proxy proxy){
    	
    	HashMap<String, Integer> wordCount = new HashMap<String,Integer>();
    	
    	List<RankedItem> ranking = new ArrayList<RankedItem>();
    	
    	query = "javascript library " + query;
    	
    	int rank = 1;
        //Fetch the page
        Document doc;
		try {
			
			System.out.println("Starting connection with dockdock...");
			
			if(proxy!=null){
				doc = Jsoup.connect("https://duckduckgo.com/?q="+query+"&ia=web").proxy(proxy).userAgent(USER_AGENT2).timeout(1000).get();
			}else{
				doc = Jsoup.connect("https://duckduckgo.com/?q="+query+"&ia=web").userAgent(USER_AGENT2).timeout(1000).get();
			}

	        PackageManager pkgm = PackageManager.getInstance();
	        StopWordManager swm = StopWordManager.getInstance();
	        
	        
	        System.out.println("Analizing Results...");
	        System.out.println(doc.toString());
	        Elements elements = doc.select("h2 a");
	        for (int i=1; i<elements.size()-1; i++){
	        	Element result = elements.get(i);
	            //final String title = result.text();
	            final String url = result.attr("href");
	
	            //Now do something with the results (maybe something more useful than just printing to console)
	
	            //System.out.println(title + " -> " + url);
	            
	            String text = "";
	            Document tech = null;
	            try{
					if (proxy != null) {
						tech = Jsoup.connect(url).proxy(proxy).userAgent(USER_AGENT).timeout(0).get();
					} else {
						tech = Jsoup.connect(url).userAgent(USER_AGENT).timeout(0).get();
					}
	            	tech.outputSettings(new Document.OutputSettings()
	                        .prettyPrint(false));// makes html() preserve linebreaks and
	                                                    // spacing
	                tech.select("br").append("\\n");
	                tech.select("p").prepend("\\n\\n");
	                text = tech.html().replaceAll("\\\\n", "\n");
	                text = Jsoup.clean(text, "", Whitelist.none(),
	                        new Document.OutputSettings().prettyPrint(false));
	                
	                String[] words = text.split(" ");
	                
	                for(String word:words){
	                	word=word.trim().toLowerCase();
	                	if(!"".equals(word) && !swm.isStopWord(word) && pkgm.isPkgName(word)){
	                		if(!ranking.contains(word)){
	                			ranking.add(new RankedItem(word, 0.0));
	                			wordCount.put(word, rank);
	                		}
	                	}
	                }
	            }catch(Exception e){
	            	System.out.println("Error en: "+url);
	            }       
	            rank++;
	        }
	        
	       // TFIDFCalculator calculator = new TFIDFCalculator();
	       /* for(String key:wordCount.keySet()){
	            double tfidf = calculator.tfIdf(wdocs.get(2), wdocs, key);
	            results.add(key);
	           // System.out.println(key + " " + tfidf);
	        }*/
        
		} catch (IOException e1) {
			System.out.println("Error estableciendo conexion con dockdock.");;
		}    

       /* for(String key:ranking){
        	System.out.println(key+": "+wordCount.get(key));
        }
		*/
		return new Ranking(ranking);

    }

	@Override
	public String getId() {
		return "duckduckgo.com";
	}

	
	@Override
	public String getContentId() {
		return "duckduckgo.com";
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