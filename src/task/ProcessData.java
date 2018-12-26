package task;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import external.wrappers.BingWrapper;
import external.wrappers.GoogleWrapper;
import external.wrappers.NPMSearchWrapper;
import external.wrappers.NPMWrapper;
import internal.lucene.LuceneSearch;
import metasearch.Searcher;
import metasearch.cache.CacheContentManager;
import metasearch.cache.CacheRankingManager;
import ner.HiperlinkMatching;
import ner.Stanford_CRF;
import ner.StringMatching;
import ranking.Ranking;
import util.ConfigManager;
import util.QueryManager;

/*
Q1 bounded buffer 5
Q2 quick sort 10
Q3 depth first search 5
Q4 regular expression 3
Q5 tic tac toe 3
Q6 ftp server 5
Q7 tcp server 10
Q8 rmi server 5
Q9 chat server 7
Q9 ftp client 4
*/

/**
 * Procesa los datos para obtener rankings
 * 
 * @author Usuarioç
 *
 */

public class ProcessData {

	public static void main(String[] args) {

		Proxy proxy = new Proxy( //
				Proxy.Type.HTTP, //
				InetSocketAddress.createUnresolved("proxy.exa.unicen.edu.ar", 8080) //
		);
		
		int max_queries = Integer.valueOf(ConfigManager.getInstance().getProperty("max_queries"));
		int max_results = Integer.valueOf(ConfigManager.getInstance().getProperty("max_results"));
		
		
		/**
		 * External searchers
		 */

		NPMWrapper npm = new NPMWrapper(max_results, NPMWrapper.OPTIMAL);
		GoogleWrapper google = new GoogleWrapper(max_results,new HiperlinkMatching());
		BingWrapper bing = new BingWrapper(max_results,new HiperlinkMatching());
		GoogleWrapper google2 = new GoogleWrapper(max_results,new StringMatching());
		BingWrapper bing2 = new BingWrapper(max_results,new StringMatching());
		GoogleWrapper google3 = new GoogleWrapper(max_results,new Stanford_CRF());
		BingWrapper bing3 = new BingWrapper(max_results,new Stanford_CRF());
		NPMSearchWrapper npmsearch = new NPMSearchWrapper(max_results);
		
		List<Searcher> searchers = new ArrayList<Searcher>();
		searchers.add(npm);
		searchers.add(google);
		searchers.add(bing);
		searchers.add(google2);
		searchers.add(bing2);
		searchers.add(google3);
		searchers.add(bing3);
		searchers.add(npmsearch);

		/*for (Searcher searcher : searchers) {
			
			System.out.println("Analizando "+ searcher.getId());
			
			//for (int i = 0 ; i < max_queries ; i++) {

				String query = "mobile app framework";//QueryManager.getInstance().getQueries().get(i);

				System.out.println("Query "+ query);
				
				Ranking ranking = CacheRankingManager.getInstance().loadRankingFromCache(searcher, query);
				if (ranking == null) {
					List<String> data = CacheContentManager.getInstance().loadContentFromCache(searcher,query);
					ranking = searcher.processData(data);//TODO data null
					CacheRankingManager.getInstance().saveRankingInCache(ranking, searcher, query);
				}
				
				System.out.println();
				
			//}
		}*/
		
		
		
		
		/**
		 * Internal Searchers
		 * 
		 */
		
		LuceneSearch lucene = new LuceneSearch(200,proxy);
		List<Searcher> internal_searchers = new ArrayList<Searcher>();
		internal_searchers.add(lucene);
		for (Searcher searcher : internal_searchers) {
			
			System.out.println("Analizando "+ searcher.getId());
			
			//for (int i = 0 ; i < max_queries ; i++) {

				String query = "mobile app framework";//QueryManager.getInstance().getQueries().get(i);

				System.out.println("Query "+ query);
				
				Ranking ranking = CacheRankingManager.getInstance().loadRankingFromCache(searcher, query);
				if (ranking == null) {
					List<String> data = new ArrayList<String>();
					data.add(query);
					ranking = searcher.processData(data);//TODO data null
					CacheRankingManager.getInstance().saveRankingInCache(ranking, searcher, query);
				}
				
				System.out.println();
				
			//}
		}
		

	}

}
