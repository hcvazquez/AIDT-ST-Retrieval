package aggregators;

import java.util.HashMap;
import java.util.List;

import ranking.RankedItem;
import ranking.Ranking;

public class M1 extends MarkovChainAggregator {

	@Override
	public String getName() {
		return "M1";
	}

	@Override
	public void setName(String name) {}

	@Override
	public double[][] getTransitionMatrix(List<RankedItem> uniqueItems, List<Ranking> rankings) {
		
		double[][] transitionMatrix = new double[uniqueItems.size()][uniqueItems.size()];
		
		for(int e=0 ; e < uniqueItems.size() ; e++){
			RankedItem ri = uniqueItems.get(e);
			HashMap<String,Double> higherRanked = new HashMap<String, Double>();
			double countTransitions = 0.0;
			for(Ranking r:rankings){
				if(r.contains(ri)){
					RankedItem P = r.getRankingList().get(r.getRankingList().indexOf(ri));
					for(RankedItem Q:r.getRankingList()){
						if(P.getScore()<=Q.getScore()){
							if(higherRanked.get(Q.getName())==null){
								higherRanked.put(Q.getName(),1.0);
							}else{
								higherRanked.put(Q.getName(),higherRanked.get(Q.getName())+1.0);
							}
							countTransitions ++;
						}
					}				
				}
			}
			for(int i=0;i<uniqueItems.size();i++){
				transitionMatrix[e][i] = higherRanked.get(uniqueItems.get(i).getName())!=null ? higherRanked.get(uniqueItems.get(i).getName()) /countTransitions : 0.0;
			}
			
		}
		
		return transitionMatrix;
	}

}
