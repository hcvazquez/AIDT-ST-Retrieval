package aggregators;

import java.util.ArrayList;
import java.util.List;

import ranking.RankedItem;
import ranking.Ranking;

public class WeightedBordaFuse implements Aggregator {
	
	protected List<Double> weights;
	protected String name;

	public WeightedBordaFuse(List<Double> weights) {
		super();
		this.weights = weights;
		this.name = "WeightedBordaFuse";
	}
	
	@Override
	public Ranking aggregate(List<Ranking> rankings) {

		List<RankedItem> result = new ArrayList<RankedItem>();
		List<RankedItem> uniqueItems =  getUniqueItems(rankings);
		
		if (rankings != null && rankings.size() > 0){ 
			if(rankings.size() != 1) {				
				for (int i=0;i<rankings.size();i++){
					Ranking ranking = rankings.get(i);
					double weight = weights.get(i);
					double scoreRest = getPointsUnassigned(uniqueItems.size()-ranking.size());
					for(RankedItem ri:uniqueItems){
						double score = 0.0;
						if(ranking.contains(ri)){
							score = uniqueItems.size()-ranking.getRankingList().indexOf(ri);
						}/*else{
							score = scoreRest / ((double)uniqueItems.size()-ranking.size());
						}*/
						score = score * weight;
						if(result.contains(ri)){
							result.get(result.indexOf(ri)).setScore(result.get(result.indexOf(ri)).getScore()+score);
						}else{
							result.add(new RankedItem(ri.getName(), score));
						}
					}
				}
				
			}else{
				return rankings.get(0);
			}
		}
		return new Ranking(result);
	}

	public List<Double> getWeights() {
		return weights;
	}

	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}

	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<RankedItem> getUniqueItems(List<Ranking> rankings){
		
		List<RankedItem> uniqueItems = new ArrayList<RankedItem>();
		
		for(Ranking r:rankings){
			for(RankedItem ri:r.getRankingList()){
				if(!uniqueItems.contains(ri)){
					uniqueItems.add(ri);
				}
			}
		}
		
		return uniqueItems;
		
	}
	
	private int getPointsUnassigned(int i) {
		int result = 0;
		for(int e=1;e<i+1;e++){
			result +=e;
		}
		return result;
	}

}
