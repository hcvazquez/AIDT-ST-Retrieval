package test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import external.wrappers.GoogleWrapper;
import metasearch.cache.CacheContentManager;
import ner.HiperlinkMatching;
import ner.Stanford_CRF;
import ner.StringMatching;
import ner.UpperCaseMatching;
import ranking.RankedItem;
import ranking.Ranking;
import util.ConfigManager;
import util.PackageManager;

    

public class TestNER {
	
	static int max_results = Integer.valueOf(ConfigManager.getInstance().getProperty("max_results"));
	
	public static void main(String[] args) {
		

		
		/*GoogleWrapper google = new GoogleWrapper(max_results,new HiperlinkMatching());
		List<String> data = CacheContentManager.getInstance().loadContentFromCache(google,"extract barcode from image");
		Ranking ranking = google.processData(data,new StringMatching());//TODO data null
		for (RankedItem i : ranking.getRankingList()) {
			System.out.println(i.getName()+":"+i.getScore());			
		}*/
		
		String html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/0.txt"));

		//System.out.println(html);
		Document tech = (Document) Jsoup.parse((String)html);
		
		//Stanford_CRF hm = new Stanford_CRF();
		UpperCaseMatching hm = new UpperCaseMatching();
		//HiperlinkMatching hm = new HiperlinkMatching();
		//System.out.println(hm.isUpperCase("QuaggaJS"));
		
		List<String> entities = hm.getNamedEntities(tech);
		
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/1.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));

		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/2.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/3.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/4.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/5.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/6.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/7.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/8.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/9.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/10.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));

		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/11.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/12.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/13.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/14.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/15.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/16.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/17.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/18.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		html = CacheContentManager.getInstance().loadFileContent(new File("content_cache/bing.com_200/extract barcode from image/19.txt"));
		tech = (Document) Jsoup.parse((String)html);
		entities.addAll(hm.getNamedEntities(tech));
		
		for(String entity:entities){
			System.out.println(entity+" -> "+PackageManager.getInstance().getHomePage(entity));
		}
	}

}
