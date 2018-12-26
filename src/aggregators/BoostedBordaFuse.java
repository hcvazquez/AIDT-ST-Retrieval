package aggregators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ranking.RankedItem;
import ranking.Ranking;

public class BoostedBordaFuse implements Aggregator {
	
	protected String name;

	public BoostedBordaFuse() {
		super();
		this.name = "BoostedBordaFuse";
	}

	@Override
	public Ranking aggregate(List<Ranking> rankings) {

		List<RankedItem> result = new ArrayList<RankedItem>();
		List<RankedItem> uniqueItems =  getUniqueItems(rankings);
		HashMap<RankedItem,Integer> boostedItems = new HashMap<RankedItem,Integer>();
		
		if (rankings != null && rankings.size() > 0){ 
			if(rankings.size() != 1) {				
				for (Ranking ranking:rankings){			
					double scoreRest = getPointsUnassigned(uniqueItems.size()-ranking.size());
					for(RankedItem ri:uniqueItems){
						double score = 0.0;
						if(ranking.contains(ri)){
							score = uniqueItems.size()-ranking.getRankingList().indexOf(ri);
							if(boostedItems.get(ri)==null){
								boostedItems.put(ri, 1);
							}else{
								boostedItems.put(ri, boostedItems.get(ri)+1);
							}
						}else{
							score = scoreRest / ((double)uniqueItems.size()-ranking.size());
						}
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
		return applyBoosting(result, boostedItems);
	}

	private Ranking applyBoosting(List<RankedItem> result, HashMap<RankedItem,Integer> boostedItems) {
		for(RankedItem ri:result){
			ri.setScore(ri.getScore()*boostedItems.get(ri));
		}
		return new Ranking(result);
	}

	private int getPointsUnassigned(int i) {
		int result = 0;
		for(int e=1;e<i+1;e++){
			result +=e;
		}
		return result;
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


}
