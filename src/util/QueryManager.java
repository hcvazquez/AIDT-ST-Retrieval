package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class QueryManager {

	private static QueryManager instance = null;
	private List<String> queries = null;

	protected QueryManager() {
		queries = new ArrayList<String>();
		loadQueries();
	}

	public static QueryManager getInstance() {
		if (instance == null) {
			instance = new QueryManager();
		}
		return instance;
	}

	protected void loadQueries() {
		String fichero = ConfigManager.getInstance().getProperty("queries_dir")+"/queries";
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null) {
				queries.add(linea);
			}
			fr.close();
		} catch (Exception e) {
			System.out.println("Excepcion leyendo fichero " + fichero + ": " + e);
		}
	}

	public List<String> getQueries() {
		return queries;
	}

}