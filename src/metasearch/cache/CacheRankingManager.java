package metasearch.cache;

import java.io.File;

import metasearch.Searcher;
import ranking.Ranking;
import util.ConfigManager;

public class CacheRankingManager {

	private static CacheRankingManager instance = null;
	
	private String cache_folder = ConfigManager.getInstance().getProperty("ranking_folder");
	private static final String EXT = ".txt";

	protected CacheRankingManager() {
		//cache = new HashMap<String, Ranking>();
	}

	public static CacheRankingManager getInstance() {
		if (instance == null) {
			instance = new CacheRankingManager();
		}
		return instance;
	}

	public Ranking loadRankingFromCache(Searcher searcher, String query) {
		Ranking ranking = new Ranking(searcher.getId() +"_"+ query, cache_folder +"/"+ searcher.getId() +"/"+ query + EXT);
		if(ranking.size()>0){
			return ranking;
		}
		return null;
	}

	public void saveRankingInCache(Ranking ranking, Searcher searcher, String query) {
		String path = cache_folder +"/"+ searcher.getId();
		File directory = new File(path);
		directory.mkdirs();
		ranking.saveRankingInFile(path +"/"+ query + EXT);
	}

}
