package aggregators;

import java.util.ArrayList;
import java.util.List;

import ranking.RankedItem;
import ranking.Ranking;

public class WeightedFirstRankingAgregator implements Aggregator {
	
	protected List<Double> weights;
	protected String name;

	public WeightedFirstRankingAgregator(List<Double> weights) {
		super();
		this.weights = weights;
		this.name = "WeightedFirstRankingAgregator";
	}

	@Override
	public Ranking aggregate(List<Ranking> rankings) {

		List<RankedItem> result = new ArrayList<RankedItem>();
		
		if (rankings != null && rankings.size() > 0){ 
			if(rankings.size() != 1) {
				for (int i=0;i<rankings.size();i++){
					Ranking ranking = rankings.get(i);
					for(RankedItem item:ranking.getRankingList()){
						if(rankings.get(0).contains(item)){
							Double weightedScore = item.getScore()*weights.get(i);
							if(result.contains(item)){
								weightedScore = weightedScore + result.get(result.indexOf(item)).getScore();
								result.get(result.indexOf(item)).setScore(weightedScore);
							}else{
								result.add(new RankedItem(item.getName(), weightedScore));
							}
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
	
	
	


}
