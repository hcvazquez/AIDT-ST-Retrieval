package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ResultsManager {

	private static ResultsManager instance = null;


	protected ResultsManager() {
		
	}

	public static ResultsManager getInstance() {
		if (instance == null) {
			instance = new ResultsManager();
		}
		return instance;
	}

	public List<String> loadResults(String folder, String query, int max) {
		List<String> results = new ArrayList<String>();
		String fichero = "results/"+folder+"/"+query+".txt";
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			int count = 0;
			
			while ((linea = br.readLine()) != null && count<=max) {
				String[] result = linea.split(":");
				results.add(result[0]);
				count ++;
			}
			
			fr.close();
		} catch (Exception e) {
			System.out.println("Excepcion leyendo fichero " + fichero + ": " + e);
		}
		return results;
	}

}