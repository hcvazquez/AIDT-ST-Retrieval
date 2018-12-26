package aggregators;

import java.util.ArrayList;
import java.util.List;

import ranking.RankedItem;
import ranking.Ranking;

public class SimpleFirstRankingAgregator implements Aggregator {

	protected String name = "SimpleFirstRankingAgregator";

	@Override
	public Ranking aggregate(List<Ranking> rankings) {
		List<RankedItem> result = new ArrayList<RankedItem>();
		if (rankings != null && rankings.size() > 0) {
			Ranking first = rankings.get(0);
			for (RankedItem item : first.getRankingList()) {
				// Por cada paquete del primer ranking si se encuentra en alguno
				// de los siguientes lo agrego.
				if (rankings.size() > 1) {
					for (int i = 1; i < rankings.size(); i++) {
						if (rankings.get(i).contains(item)) {
							result.add(item);
							break;
						}
					}
				}
			}
		}
		return new Ranking(result);

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;

	}

}
