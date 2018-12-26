package metasearch;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import aggregators.Aggregator;
import metasearch.cache.CacheManager;
import ner.EntityExtractor;
import ranking.Ranking;

public class MetaSearcherWithCache implements MetaSearcher{
	
	List<Searcher> searchers;
	Aggregator aggregator;
	
	public MetaSearcherWithCache(List<Searcher> searchers, Aggregator aggregator) {
		super();
		this.searchers = searchers;
		this.aggregator = aggregator;
	}

	public Ranking search(String query, Proxy proxy, List<Searcher> searchers, Aggregator aggregator){

		List<Ranking> rankings = new ArrayList<Ranking>();
		
		CacheManager cache = CacheManager.getInstance();
		
		for (Searcher searcher : searchers) {
			if(cache.isCached(searcher, query)){
				rankings.add(cache.getCached(searcher, query));
			}else{
				Ranking ranking = searcher.search(query, proxy);
				rankings.add(ranking);
				cache.save(searcher,query, ranking);
			}
		}
		
		cache.saveCache();

		return aggregator.aggregate(rankings);
	}
	
	public Ranking search(String query, Proxy proxy){

		List<Ranking> rankings = new ArrayList<Ranking>();
		
		CacheManager cache = CacheManager.getInstance();
		
		for (Searcher searcher : searchers) {
			if(cache.isCached(searcher, query)){
				rankings.add(cache.getCached(searcher, query));
			}else{
				Ranking ranking = searcher.search(query, proxy);
				rankings.add(ranking);
				if(ranking.size()!=0){
					cache.save(searcher,query, ranking);
				}
			}
		}
		
		cache.saveCache();

		return aggregator.aggregate(rankings);
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getContentId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<String> acquireData(String query, Proxy proxy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ranking processData(List<String> contents) {
		// TODO Auto-generated method stub
		return null;
	}
}