package metasearch;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import aggregators.Aggregator;
import metasearch.cache.CacheRankingManager;
import ranking.Ranking;

public class MetaSearcherImp implements MetaSearcher{
	
	List<Searcher> searchers;
	Aggregator aggregator;
	
	List<Ranking> rankings;
	int ranking_size;
	
	public MetaSearcherImp(List<Searcher> searchers, Aggregator aggregator, int ranking_size) {
		super();
		this.searchers = searchers;
		this.aggregator = aggregator;
		this.ranking_size = ranking_size;
	}

	@Override
	public String getId() {
		return aggregator.getName();
	}
	
	@Override
	public String getContentId() {
		return aggregator.getName();
	}

	@Override
	public List<String> acquireData(String query, Proxy proxy) {
		
		rankings = new ArrayList<Ranking>();
		
		for (Searcher searcher : searchers) {
			Ranking ranking = CacheRankingManager.getInstance().loadRankingFromCache(searcher, query);
	        if(ranking == null){
	        	System.out.println("Warning: Ranking " + searcher.getId() + " no se puede cargar");
	        }else{
	        	/*if(ranking.size()>ranking_size){
	        		rankings.add(ranking.getRankingAtK(ranking_size));
	        	}else{*/
	        		rankings.add(ranking);
	        	//}
	        }
		}

		return null;
	}

	@Override
	public Ranking processData(List<String> contents) {
		Ranking result = aggregator.aggregate(rankings);
		if(result.size()>ranking_size){
    		rankings.add(result.getRankingAtK(ranking_size));
    	}		
		return result;
	}
	
	public Ranking search(String query, Proxy proxy, List<Searcher> searchers, Aggregator aggregator)
			throws IOException {

		List<Ranking> rankings = new ArrayList<Ranking>();
		
		for (Searcher searcher : searchers) {
			rankings.add(searcher.search(query, proxy));
		}

		return aggregator.aggregate(rankings);
	}
	
	public Ranking search(String query, Proxy proxy){

		List<Ranking> rankings = new ArrayList<Ranking>();
		
		for (Searcher searcher : searchers) {
			rankings.add(searcher.search(query, proxy));
		}

		return aggregator.aggregate(rankings);
	}
}