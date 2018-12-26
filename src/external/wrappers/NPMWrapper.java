package external.wrappers;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import metasearch.Searcher;
import metasearch.cache.CacheRankingManager;
import ner.EntityExtractor;
import ranking.RankedItem;
import ranking.Ranking;
import util.PackageManager;

public class NPMWrapper extends SearchWrapperAbs implements Searcher {
	// We need a real browser user agent or Google will block our request with a
	// 403 - Forbidden
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";

	public int MAX_PAGE = 20;
	public String RANK = OPTIMAL;
	public int RESULTS = 20;

	public static final String OPTIMAL = "optimal";
	public static final String QUALITY = "quality";
	public static final String POPULARITY = "popularity";
	public static final String MAINTENANCE = "maintenance";
	
	public static final String NPMJS = "npmjs.com";

	public NPMWrapper(int results, String ranktype) {
		RESULTS = results;
		MAX_PAGE = (int) Math.ceil(results / 10);
		RANK = ranktype;
	}
	
	@Override
	public List<String> downloadResultContent(String query, Proxy proxy){
		List<String> result = new ArrayList<String>();
		for (int page = 1; page <= MAX_PAGE; page++) {
			Document doc = null;
			try {
				if (proxy != null) {
					doc = Jsoup.connect(
							"https://www.npmjs.com/search?q=" + query + "&page=" + page + "&ranking=" + RANK)
							.proxy(proxy).userAgent(USER_AGENT).timeout(0).get();
				} else {
					doc = Jsoup.connect(
							"https://www.npmjs.com/search?q=" + query + "&page=" + page + "&ranking=" + RANK)
							.userAgent(USER_AGENT).timeout(0).get();
				}
				result.add(doc.toString());
			} catch (IOException e1) {
				System.out.println("Error estableciendo conexion con NPM.");
			}
		}
		return result;
	}

	public Ranking search(String query, Proxy proxy) {

		Ranking r = CacheRankingManager.getInstance().loadRankingFromCache(this, query);

		if (r != null) {
			return r;
		} else {

			List<String> contents = acquireData(query, proxy);
			
			r = processData(contents);
			
			CacheRankingManager.getInstance().saveRankingInCache(r, this, query);

		}
		return r;
	}
	
	@Override
	public Ranking processData(List<String> contents) {
		
		List<RankedItem> results = new ArrayList<RankedItem>();
		
		System.out.println("Analizing Results...");
		
		PackageManager pm = PackageManager.getInstance();
		
		for (int page = 0; page < contents.size() && results.size() < RESULTS; page++) {
			
			Document doc = (Document) Jsoup.parse((String)contents.get(page));

			if (doc.select("h3 a").size() == 0) {
				break;
			}

			Elements elements = doc.select("h3 a");
			for (int e = 0; e < elements.size(); e++) {
				Element result = elements.get(e);

				if (result.className().startsWith("packageName")) {

					final String title = result.text();
					final String url = result.attr("href");
					
					if(pm.isPkgName(title)){
						RankedItem ri = new RankedItem(title, (double) (((page - 1) * 10) + e));//este score esta mal
						if (!results.contains(ri)) {
							System.out.println(title + " -> " + url);
							ri.setScore((double) (RESULTS-results.size())); // aca se corrige el score de arriba
							results.add(ri);
						}
					}

				}
			}

		}

		return new Ranking(results);

	}
	
	public List<String> acquireData(String query, Proxy proxy){
		
		//System.out.println("Acquiring data from NPM...");

		List<String> content = getResultContent(query, proxy,this);
		
		//System.out.print(" ...connection SUCCESSFUL...");
		
		return content;
	}

	@Override
	public String getId() {
		return NPMJS +"_"+ RANK;
	}
	
	@Override
	public String getContentId() {
		return NPMJS +"_"+ RANK + "_" + RESULTS;
	}

}