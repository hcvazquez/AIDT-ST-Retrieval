package metasearch.cache;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import metasearch.Searcher;
import util.ConfigManager;

public class CacheContentManager {

	private static CacheContentManager instance = null;
	
	private String cache_folder = ConfigManager.getInstance().getProperty("content_folder");
	
	private static final String EXT = ".txt";

	protected CacheContentManager() {
		//cache = new HashMap<String, Ranking>();
	}

	public static CacheContentManager getInstance() {
		if (instance == null) {
			instance = new CacheContentManager();
		}
		return instance;
	}

	public List<String> loadContentFromCache(Searcher searcher, String query) {
		List<String> contents = new ArrayList<String>();
		String path = cache_folder +"/"+ searcher.getContentId() +"/"+ query;
		File f = new File(path);
		if (f.exists()){
			File[] ficheros = f.listFiles();
			for (int i=0;i<ficheros.length;i++){
				contents.add(loadFileContent(ficheros[i]));
			}
			return contents;
		} else {
			return null;
		}
	}
	
	public String loadFileContent(File file) {
		String content = null;
		try {
			Scanner scanner = new Scanner(file);
			content = scanner.useDelimiter("\\Z").next();
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public void saveContentInCache(List<String> results, Searcher searcher, String query) {
		String path = cache_folder +"/"+ searcher.getContentId() +"/"+ query;
		File directory = new File(path);
		directory.mkdirs();
		for(int i=0;i<results.size();i++){
			String filename = String.valueOf(i)+ EXT;
			saveFileContent(results.get(i),path,filename);
		}
	}

	public void saveFileContent(String content, String path, String filename){
		try {
			File outputFile = new File(path+"/"+filename);
			FileWriter fileWriter = new FileWriter(outputFile, false); // true to append // false to overwrite.
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
