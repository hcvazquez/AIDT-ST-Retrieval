package metasearch;

import java.io.IOException;
import java.net.Proxy;
import java.util.List;

import aggregators.Aggregator;
import ranking.Ranking;

public interface MetaSearcher extends Searcher{
	
	public Ranking search(String query, Proxy proxy, List<Searcher> searchers, Aggregator aggregator) throws IOException;
	
	public Ranking search(String query, Proxy proxy);

}
