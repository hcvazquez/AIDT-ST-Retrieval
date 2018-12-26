package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class HomeManager {

	private static HomeManager instance = null;
	private ArrayList<String> malformedURLs = null;

	protected HomeManager() {
		malformedURLs = new ArrayList<String>();
		loadMalformedURLs();
	}

	public static HomeManager getInstance() {
		if (instance == null) {
			instance = new HomeManager();
		}
		return instance;
	}

	protected void loadMalformedURLs() {
		String fichero = ConfigManager.getInstance().getProperty("url_file");
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null) {
				malformedURLs.add(linea);

			}
			fr.close();
		} catch (Exception e) {
			System.out.println("Excepcion leyendo fichero " + fichero + ": " + e);
		}
	}

	public boolean isMalformed(String name) {
		return !name.contains("http")|| malformedURLs.contains(name);
	}
	
	public ArrayList<String> getisMalformedURLs() {
		return malformedURLs;
	}


}