package aggregators;

import java.util.ArrayList;
import java.util.List;

import ranking.RankedItem;
import ranking.Ranking;

public class FilterExternalRankAgregator implements Aggregator {
	
	protected List<Ranking> filters;
	protected String name;

	public FilterExternalRankAgregator() {
		super();
	}

	public void setFilters(List<Ranking> filters) {
		this.filters = filters;
	}

	@Override
	public Ranking aggregate(List<Ranking> rankings) {
		
		List<RankedItem> result = new ArrayList<RankedItem>();
		
		if (rankings != null && rankings.size() > 0){ 
			for(Ranking ranking:rankings){
				for(RankedItem item:ranking.getRankingList()){
					for(Ranking filter:filters){
						if(!result.contains(item) && filter.contains(item)){
							Double weightedScore = item.getScore()/**weights.get(i)*/;
							result.add(0,new RankedItem(item.getName(), weightedScore));
						}else{
							Double weightedScore = item.getScore()/**weights.get(i)*/;
							result.add(new RankedItem(item.getName(), weightedScore));
						}
					}
				}
			}
		}else System.out.println("Warning: Ranking list (r) need to be: > 0 ");
		
		return new Ranking(result);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return 	"Filter_";
	}
	
	
	


}
