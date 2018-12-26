package internal.lucene;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.jsoup.Jsoup;

import external.wrappers.NPMUtilWrapper;
import metasearch.Searcher;
import metasearch.cache.CacheContentManager;
import metasearch.cache.CacheRankingManager;
import ner.EntityExtractor;
import ranking.RankedItem;
import ranking.Ranking;
import util.ConfigManager;

public class LuceneSearch implements Searcher {
	
	private static final String datasetURL = ConfigManager.getInstance().getProperty("dataset_url");
	private static final String download_path = ConfigManager.getInstance().getProperty("download_path");
	
	private static final String indexDir = ConfigManager.getInstance().getProperty("index_dir");
	public static final String jsonFilePath = ConfigManager.getInstance().getProperty("json_file_path");
	public static final String npm_filename = ConfigManager.getInstance().getProperty("npm_filename");

	Indexer indexer;
	LuceneSearcher searcher;

	int max_results = 200;
	private Proxy proxy;

	public LuceneSearch(int max_results, Proxy proxy) {
		this.max_results = max_results;
		this.proxy = proxy;
	}

	public void createIndex() throws IOException {
		indexer = new Indexer(indexDir,proxy);
		int numIndexed;
		long startTime = System.currentTimeMillis();
		numIndexed = indexer.createIndex(jsonFilePath);
		long endTime = System.currentTimeMillis();
		indexer.close();
		System.out.println(numIndexed + " File indexed, time taken: " + (endTime - startTime) + " ms");
	}

	public Ranking search(String query, Proxy proxy) {

		Ranking r = CacheRankingManager.getInstance().loadRankingFromCache(this, query);

		if (r != null) {
			return r;
		} else {
			
			/*List<String> filepath = acquireData(query, proxy);
			
			try {
				createIndex();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			List<String> queries= new ArrayList<String>();
			queries.add(query);
			
			r = processData(queries);
			
			CacheRankingManager.getInstance().saveRankingInCache(r, this, query);
		}
		return r;
	}

	@Override
	public String getId() {
		return "lucene";
	}
	
	@Override
	public String getContentId() {
		return "lucene";
	}
	
	@Override
	public Ranking processData(List<String> contents) {
		List<RankedItem> results = new ArrayList<RankedItem>();
		try {
			
			searcher = new LuceneSearcher(indexDir);
			long startTime = System.currentTimeMillis();
			TopDocs hits = searcher.search(contents.get(0), max_results);
			long endTime = System.currentTimeMillis();

			System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime));
			for (int i = 0; i < hits.scoreDocs.length; i++) {
				ScoreDoc scoreDoc = hits.scoreDocs[i];
				Document doc = searcher.getDocument(scoreDoc);
				String nameDoc = doc.get(LuceneConstants.ID);
				results.add(new RankedItem(nameDoc, (double) (max_results - i)));
			}

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return new Ranking(results);
	}

	@Override
	public List<String> acquireData(String query, Proxy proxy) {
		this.proxy = proxy;
		List<String> path = new ArrayList<String>();
		String json = null;
		try {
			int maxBodySize = 0;
			if (proxy != null) {
				json = Jsoup.connect(datasetURL).maxBodySize(maxBodySize).proxy(proxy).ignoreContentType(true).execute().body();
			} else {
				json = Jsoup.connect(datasetURL).maxBodySize(maxBodySize).ignoreContentType(true).execute().body();
			}

			if(json!=null){
				CacheContentManager.getInstance().saveFileContent(json, download_path, npm_filename);
				path.add(download_path+"/"+npm_filename);
				System.out.println("Done!");
			}
			
			acquireExtraData(proxy);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return path;
		 
	}
	
	public void acquireExtraData(Proxy proxy){
		System.out.println("Create packege name file");
		NPMUtilWrapper.createPackageNameFile(download_path+"/"+npm_filename);
		System.out.println("Create packege name file");
		NPMUtilWrapper.createHomePagesFile(download_path+"/"+npm_filename);
		System.out.println("Downloading Readme Content");
		NPMUtilWrapper.downloadAllReadmeContent(proxy);
	}
}