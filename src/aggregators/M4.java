package aggregators;

import java.util.HashMap;
import java.util.List;

import ranking.RankedItem;
import ranking.Ranking;

public class M4 extends MarkovChainAggregator {

	@Override
	public String getName() {
		return "M4";
	}

	@Override
	public void setName(String name) {}

	@Override
	public double[][] getTransitionMatrix(List<RankedItem> uniqueItems, List<Ranking> rankings) {
		
		double[][] transitionMatrix = new double[uniqueItems.size()][uniqueItems.size()];
		
		for(int P=0 ; P < uniqueItems.size() ; P++){
			HashMap<String,Double> higherRanked = new HashMap<String, Double>();
			double countTransitions = 0.0;
			for(int Q=0;Q<uniqueItems.size();Q++){
				int mayor = 0;
				int minor = 0;
				for(Ranking r:rankings){
					if(!uniqueItems.get(P).equals(uniqueItems.get(Q)) && r.contains(uniqueItems.get(P)) && r.contains(uniqueItems.get(Q))){
						if(uniqueItems.get(P).getScore()<uniqueItems.get(Q).getScore()){
							mayor++;
						}else{
							minor++;
						}
					}
				}
				if(mayor>minor){
					if(higherRanked.get(uniqueItems.get(Q).getName())==null){
						higherRanked.put(uniqueItems.get(Q).getName(),1.0);
					}else{
						higherRanked.put(uniqueItems.get(Q).getName(),higherRanked.get(uniqueItems.get(Q).getName())+1.0);
					}
				}else{
					if(higherRanked.get(uniqueItems.get(P).getName())==null){
						higherRanked.put(uniqueItems.get(P).getName(),1.0);
					}else{
						higherRanked.put(uniqueItems.get(P).getName(),higherRanked.get(uniqueItems.get(P).getName())+1.0);
					}
				}
				countTransitions ++;
			}
			for(int i=0;i<uniqueItems.size();i++){
				transitionMatrix[P][i] = higherRanked.get(uniqueItems.get(i).getName())!=null ? higherRanked.get(uniqueItems.get(i).getName()) /countTransitions : 0.0;
			}
			
		}
		
		return transitionMatrix;
	}

}
