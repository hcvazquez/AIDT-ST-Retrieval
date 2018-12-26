package test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

import aggregators.Aggregator;
import aggregators.WeightedFirstRankingAgregator;
import external.wrappers.BingWrapper;
import external.wrappers.GoogleWrapper;
import external.wrappers.NPMSearchWrapper;
import external.wrappers.NPMWrapper;
import internal.lucene.LuceneSearch;
import metasearch.MetaSearcher;
import metasearch.MetaSearcherImp;
import metasearch.Searcher;
import ner.StringMatching;
import ranking.RankedItem;
import ranking.Ranking;

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
    

public class TestSingleQuery {

	public static void main(String[] args) {
		
		Proxy proxy = new Proxy(                                      //
			    Proxy.Type.HTTP,                                      //
			    InetSocketAddress.createUnresolved("proxy.exa.unicen.edu.ar", 8080) //
			);
		
		LuceneSearch lucene = new LuceneSearch(200,proxy);	

		NPMWrapper npm1 = new NPMWrapper(200, NPMWrapper.OPTIMAL);
		NPMWrapper npm2 = new NPMWrapper(200, NPMWrapper.POPULARITY);
		NPMWrapper npm3 = new NPMWrapper(200, NPMWrapper.QUALITY);
		NPMWrapper npm4 = new NPMWrapper(200, NPMWrapper.MAINTENANCE);
		
		GoogleWrapper goo = new GoogleWrapper(50,new StringMatching());
		BingWrapper bing = new BingWrapper(50,new StringMatching());
		//YARNWrapper yarn = new YARNWrapper();
		//DuckWrapper duck = new DuckWrapper();
		NPMSearchWrapper npms = new NPMSearchWrapper(50);
		ArrayList<Searcher> searchers = new ArrayList<Searcher>();
		searchers.add(lucene);
		searchers.add(goo);
		searchers.add(npm1);
		searchers.add(npms);
		//searchers.add(npm2);
		//searchers.add(npm3);
		//searchers.add(npm4);
		searchers.add(bing);
		//searchers.add(duck);
		
		Double[] weights = {0.20,0.20,0.20,0.20,0.20};
		Aggregator aggregator = new WeightedFirstRankingAgregator(Arrays.asList(weights));
		
		
		String query = "rpg game 2d";
		
		MetaSearcher meta = new MetaSearcherImp(searchers, aggregator, 200/*max_result*/);
		Ranking results = null;
		results = meta.search(query, proxy);
		
		System.out.println();
		System.out.println("RESULTADOS");
		for(RankedItem tech:results.getRankingList()){
				System.out.println(tech.getName()+":"+tech.getScore());
		}
		
		results.saveRankingInFile("results/"+query+".txt");
		
		/*executeQuery("bounded buffer");
		executeQuery("quick sort");
		executeQuery("depth first search");
		executeQuery("regular expression");
		executeQuery("tic tac toe");
		executeQuery("ftp server");
		executeQuery("tcp server");
		executeQuery("rmi server");
		executeQuery("chat server");
		executeQuery("ftp client");*/
		
		

	}

}
