package aggregators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ranking.RankedItem;
import ranking.Ranking;

public class CordorcetAggregator implements Aggregator {

	@Override
	public Ranking aggregate(List<Ranking> rankings) {
		
		List<RankedItem> uniqueItems = getUniqueItems(rankings);
		double[][] winLooseMatrix = getWinLooseMatrix(uniqueItems, rankings);
		
		List<RankedItem> uniqueClone = new ArrayList<RankedItem>(); //fix synchonization problem
		uniqueClone.addAll(uniqueItems);
		Ranking result = new Ranking(uniqueClone);
		
		result.setComparator(new Comparator<RankedItem>() {
				@Override
				public int compare(RankedItem r1, RankedItem r2) {
					return compareTo(r1,r2) * -1;
				}
				
				public int compareTo(RankedItem r1, RankedItem r2){
					int r1x = uniqueItems.indexOf(r1);
					int r2x = uniqueItems.indexOf(r2);
					if(winLooseMatrix[r1x][0]>winLooseMatrix[r2x][0]){
						return 1;
					}else if(winLooseMatrix[r1x][0]<winLooseMatrix[r2x][0]){
						return -1;
					}else if(winLooseMatrix[r1x][1]<winLooseMatrix[r2x][1]){
						return 1;
					}else if(winLooseMatrix[r1x][1]>winLooseMatrix[r2x][1]){
						return -1;
					}
					return r1.compareTo(r2);
				}		
		});
		
		result.sort();
		
		return result;
	}
	
	public double[][] getWinLooseMatrix(List<RankedItem> uniqueItems, List<Ranking> rankings) {
		
		double[][] winLooseMatrix = new double[uniqueItems.size()][2];
		
		for(int P=0 ; P < uniqueItems.size() ; P++){
			int win = 0;
			int loose = 0;
			for(int Q=0;Q<uniqueItems.size();Q++){
				for(Ranking r:rankings){
					if(r.contains(uniqueItems.get(P)) && r.contains(uniqueItems.get(Q))){
						if(uniqueItems.get(P).getScore()>uniqueItems.get(Q).getScore()){
							win++;
						}else{
							loose++;
						}
					}
				}
			}
			winLooseMatrix[P][0] = win;
			winLooseMatrix[P][1] = loose;
		}			
		return winLooseMatrix;
	}

	@Override
	public String getName() {
		return "Cordorcet";
	}

	@Override
	public void setName(String name) {}
	
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
