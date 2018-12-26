package aggregators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ranking.RankedItem;
import ranking.Ranking;

public class M3 extends MarkovChainAggregator {

	@Override
	public String getName() {
		return "M3";
	}

	@Override
	public void setName(String name) {}

	@Override
	public double[][] getTransitionMatrix(List<RankedItem> uniqueItems, List<Ranking> rankings) {
		
		double[][] transitionMatrix = new double[uniqueItems.size()][uniqueItems.size()];
		
		for(int e=0 ; e < uniqueItems.size() ; e++){
			RankedItem ri = uniqueItems.get(e);
			List<Ranking> rankingPicks = new ArrayList<Ranking>();
			for(Ranking r:rankings){
				if(r.getRankingList().contains(ri)){
					rankingPicks.add(r);
				}
			}
			for(Ranking r:rankingPicks){
				double countTransitions = 0.0;
				HashMap<String,Double> higherRanked = new HashMap<String, Double>();
				if(r.getRankingList().contains(ri)){
					RankedItem P = r.getRankingList().get(r.getRankingList().indexOf(ri));
					for(RankedItem Q:r.getRankingList()){
						if(P.getScore()<Q.getScore() || P.equals(Q)){
							if(higherRanked.get(Q.getName())==null){
								higherRanked.put(Q.getName(),1.0);
							}else{
								higherRanked.put(Q.getName(),higherRanked.get(Q.getName())+1.0);
							}
							countTransitions++;
						}
					}
				}
				for(int i=0;i<uniqueItems.size();i++){
					if(r.getRankingList().contains(uniqueItems.get(i))){//la prob de pickear un ranking q contenga el item y la prob de pickear un item mejor rankeado
						if(higherRanked.get(uniqueItems.get(i).getName())!=null){
							double rank_prob = 1.0/((double)rankingPicks.size());
							double item_prob = higherRanked.get(uniqueItems.get(i).getName()) /countTransitions;
							transitionMatrix[e][i] += rank_prob * item_prob;
						}else{
							transitionMatrix[e][i] += 0.0;
						}
					}
				}
			}
			
		}
		
		return transitionMatrix;
	}

}
