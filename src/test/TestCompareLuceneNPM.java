package test;

import java.net.InetSocketAddress;
import java.net.Proxy;

import external.wrappers.NPMSearchWrapper;
import external.wrappers.NPMWrapper;
import internal.lucene.LuceneSearch;
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
    

public class TestCompareLuceneNPM {

	public static void main(String[] args) {
		
		Proxy proxy = new Proxy(                                      //
			    Proxy.Type.HTTP,                                      //
			    InetSocketAddress.createUnresolved("proxy.exa.unicen.edu.ar", 8080) //
			);
		
		
		NPMSearchWrapper npm1 = new NPMSearchWrapper(200/*, NPMWrapper.OPTIMAL*/);
		//NPMWrapper npm2 = new NPMWrapper(11, NPMWrapper.POPULARITY);
		//NPMWrapper npm3 = new NPMWrapper(11, NPMWrapper.QUALITY);
		//NPMWrapper npm4 = new NPMWrapper(11, NPMWrapper.MAINTENANCE);
		
		System.out.println();
		String query = "create web animations";
		System.out.println("Executing Query: " + query);
		
		LuceneSearch tester = new LuceneSearch(200,proxy);
		Ranking resultRetrival = tester.search(query, proxy);
		Ranking resultNPM = npm1.search(query, proxy);
		resultRetrival.sort();
		resultNPM.sort();
		
		System.out.println("Resultados");
		System.out.println("Solo Lucene");
		for(RankedItem tech:resultRetrival.getRankingList()){
			if(!resultNPM.contains(tech)){
				System.out.println(tech.getName()+":"+tech.getScore());
			}
		}
		System.out.println("Solo NPM");
		for(RankedItem tech:resultNPM.getRankingList()){
			if(!resultRetrival.contains(tech)){
				System.out.println(tech.getName()+":"+tech.getScore());
			}
		}
		System.out.println("Compartidos");
		for(RankedItem tech:resultRetrival.getRankingList()){
			if(resultNPM.contains(tech)){
				System.out.println(tech.getName()+":"+tech.getScore());
			}
		}
		System.out.println();
		

	}

}
