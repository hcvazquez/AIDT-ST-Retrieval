package external.wrappers;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import metasearch.Searcher;
import metasearch.cache.CacheRankingManager;
import ner.EntityExtractor;
import ranking.RankedItem;
import ranking.Ranking;
import util.PackageManager;

public class NPMSearchWrapper extends SearchWrapperAbs implements Searcher {
	// We need a real browser user agent or Google will block our request with a
	// 403 - Forbidden
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	public int MAX_RESULTS = 20;
	public String RANK_TYPE = "";
	public static final String KEYWORDS = "keywords";
	
	public static final String NPMSEARCH =  "npmsearch.com";

	public NPMSearchWrapper(int results) {
		this.MAX_RESULTS = results;
	}
	
	public NPMSearchWrapper(int results, String type) {
		this.MAX_RESULTS = results;
		RANK_TYPE = type;
	}

	@Override
	public List<String> downloadResultContent(String query, Proxy proxy){
		List<String> result = new ArrayList<String>();
		String json = null;
		try {
			if (proxy != null) {
				json = Jsoup.connect("http://npmsearch.com/query?fields=name&sort=rating:desc&q=\"" + query + "\"&rows="
						+ MAX_RESULTS).proxy(proxy).ignoreContentType(true).execute().body();
			} else {
				json = Jsoup.connect("http://npmsearch.com/query?fields=name&sort=rating:desc&q=\"" + query + "\"&rows="
						+ MAX_RESULTS).ignoreContentType(true).execute().body();
			}
			result.add(json);
		} catch (IOException e1) {
			System.out.println("Error estableciendo conexion con NPM.");
		}
		return result;
	}


	public Ranking search(String query, Proxy proxy) {
		
		if(RANK_TYPE.equals(KEYWORDS)){
			String[] keywords = query.split(" ");
			query= "keywords:"+keywords[0]+","+keywords[1];
		}

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
		// System.out.println("Connection with NPM finished...");
		if(contents.size()>0){
			JsonObject obj = new JsonParser().parse(((String)contents.get(0))).getAsJsonObject();
			JsonArray names = obj.getAsJsonArray("results");
			int rank = 1;
			for (int i = 0; i < names.size() && results.size() < MAX_RESULTS ; i++) {
				JsonElement entry = names.get(i);
				if (entry != null && entry.isJsonObject()) {
					String pkg = ((JsonObject) entry).get("name").getAsString();
					if(pm.isPkgName(pkg)){
						RankedItem ri = new RankedItem(pkg,(double) (MAX_RESULTS - (rank - 1)));
						if (!results.contains(ri)) {
							results.add(ri);
						}
					}
				}
				rank++;
			}

		}

		return 	new Ranking(results);
	}
	
	public List<String> acquireData(String query, Proxy proxy){
		
		//System.out.println("Acquiring data from npmsearch.com...");

		List<String> content = getResultContent(query, proxy,this);
		
		//System.out.print(" ...connection SUCCESSFUL...");
		
		return content;
	}

	@Override
	public String getId() {
		return NPMSEARCH + RANK_TYPE;
	}
	
	@Override
	public String getContentId() {
		return NPMSEARCH+"_"+MAX_RESULTS;
	}


}