package aggregators;

import java.util.List;

import ranking.Ranking;

public interface Aggregator {
	public Ranking aggregate(List<Ranking> rankings);
	public String getName();
	public void setName(String name);
}
