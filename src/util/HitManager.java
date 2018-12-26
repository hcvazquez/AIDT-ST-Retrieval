package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HitManager {

	private static HitManager instance = null;
	private List<String> queries = null;
	private HashMap<String, List<String>> queriesXhits = null;
	private HashMap<String, List<String>> queriesXNOhits = null;

	protected HitManager() {
		queries = new ArrayList<String>();
		queriesXhits = new HashMap<String, List<String>>();
		queriesXNOhits = new HashMap<String, List<String>>();
		loadQueriesXhits();
	}

	public static HitManager getInstance() {
		if (instance == null) {
			instance = new HitManager();
		}
		return instance;
	}

	protected void loadQueriesXhits() {
		String fichero = ConfigManager.getInstance().getProperty("queries_dir")+"/queriesXhits";
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(fichero);
			br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] queriesXhits = linea.split(":=:");
				String q = queriesXhits[0];
				queries.add(q);

				List<String> hits = new ArrayList<String>();
				if (queriesXhits.length > 1) {
					hits.addAll(Arrays.asList(queriesXhits[1].split(",")));
				}
				this.queriesXhits.put(q, hits);

				List<String> nohits = new ArrayList<String>();
				if (queriesXhits.length > 2) {
					nohits.addAll(Arrays.asList(queriesXhits[2].split(",")));
				}
				this.queriesXNOhits.put(q, nohits);

			}
		} catch (Exception e) {
			System.out.println("Excepcion leyendo fichero " + fichero + ": " + e);
		} finally {
			try {
				if (null != fr)
					fr.close();
			} catch (Exception e2) {
				System.out.println("Excepcion cerrando fichero " + fichero + ": " + e2);
			}
		}
	}

	public void saveQueriesXhits() {
		String fichero = ConfigManager.getInstance().getProperty("queries_dir")+"/queriesXhits";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			fw = new FileWriter(fichero);
			pw = new PrintWriter(fw);
			for (String query : queries) {
				String linea = query + ":=:";
				for (int i = 0; i < queriesXhits.get(query).size(); i++) {
					linea = linea + queriesXhits.get(query).get(i);
					if (i < queriesXhits.get(query).size() - 1) {
						linea = linea + ",";
					}
				}
				linea = linea + ":=:";
				for (int i = 0; i < queriesXNOhits.get(query).size(); i++) {
					linea = linea + queriesXNOhits.get(query).get(i);
					if (i < queriesXNOhits.get(query).size() - 1) {
						linea = linea + ",";
					}
				}
				pw.println(linea);
			}
		} catch (Exception e) {
			System.out.println("Excepcion leyendo fichero " + fichero + ": " + e.getMessage());
		} finally {
			try {
				if (null != fw)
					fw.close();
			} catch (Exception e2) {
				System.out.println("Excepcion cerrando fichero " + fichero + ": " + e2.getMessage());
			}
		}
	}

	public List<String> getQueries() {
		return queries;
	}

	public void addHit(String query, String hit) {
		if (queriesXhits.get(query) != null) {
			if (!queriesXhits.get(query).contains(hit)) {
				queriesXhits.get(query).add(hit);
			}
		} else {
			queriesXhits.put(query, new ArrayList<String>());
			queriesXhits.get(query).add(hit);
		}
	}

	public void addNOHit(String query, String hit) {
		if (queriesXNOhits.get(query) != null) {
			if (!queriesXNOhits.get(query).contains(hit)) {
				queriesXNOhits.get(query).add(hit);
			}
		} else {
			queriesXNOhits.put(query, new ArrayList<String>());
			queriesXNOhits.get(query).add(hit);
		}
	}

	public boolean isAHit(String q, String pkg) {
		if (queriesXhits.get(q) != null && queriesXhits.get(q).contains(pkg)) {
			return true;
		}
		return false;
	}

	public boolean isNotAHit(String q, String pkg) {
		if (queriesXNOhits.get(q) != null && queriesXNOhits.get(q).contains(pkg)) {
			return true;
		}
		return false;
	}

}