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

import metasearch.Searcher;
import metasearch.cache.CacheRankingManager;
import ranking.RankedItem;
import ranking.Ranking;
import util.PackageManager;
import util.StopWordManager;

@Deprecated
public class CoverallsWrapper{
	// We need a real browser user agent or Google will block our request with a
	// 403 - Forbidden
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";

	public static void main(String[] args) throws Exception {

	}

	public static String search(String url, Proxy proxy) {
			
			HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
			List<RankedItem> ranking = new ArrayList<RankedItem>();

			String percentage = "unknown";
			// Fetch the page
			Document doc;
			try {

				if (proxy != null) {
					doc = Jsoup.connect(url).proxy(proxy)
							.userAgent(USER_AGENT).timeout(0).get();
				} else {
					doc = Jsoup.connect(url)
							.userAgent(USER_AGENT).timeout(0).get();
				}

				for (Element result : doc.select("div#repoShowPercentage")) {

					return result.text();
					
				}


			} catch (IOException e1) {
				try {

					if (proxy != null) {
						doc = Jsoup.connect(url).proxy(proxy)
								.userAgent(USER_AGENT).timeout(0).get();
					} else {
						doc = Jsoup.connect(url)
								.userAgent(USER_AGENT).timeout(0).get();
					}

					for (Element result : doc.select("div#repoShowPercentage")) {

						return result.text();
						
					}


				} catch (IOException e2) {
					percentage = "ERROR";
				}
			}
		return percentage;

	}

	public String getName() {
		return "coveralls.io";
	}

}