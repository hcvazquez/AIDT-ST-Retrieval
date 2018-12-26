package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PackageManager {

	private static PackageManager instance = null;
	private ArrayList<String> pkgNames = null;
	private HashMap<String,String> pkgHomePages = null;
	private HashMap<String,String> pkgHomePagesByPkgs = null;
	private HashMap<String,String> pkgDescs = null;

	protected PackageManager() {
		pkgNames = new ArrayList<String>();
		pkgHomePages = new HashMap<String,String>();
		pkgHomePagesByPkgs = new HashMap<String,String>();
		pkgDescs = new HashMap<String,String>();
		loadPkgDesc();
		loadPackageNames();
		loadHomePages();
	}

	public static PackageManager getInstance() {
		if (instance == null) {
			instance = new PackageManager();
		}
		return instance;
	}

	protected void loadHomePages() {
		String fichero = ConfigManager.getInstance().getProperty("pkg_homepages_file");
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] arr = linea.split(",");
				pkgHomePages.put(arr[1],arr[0]);
				pkgHomePagesByPkgs.put(arr[0],arr[1]);
			}
			fr.close();
		} catch (Exception e) {
			System.out.println("Excepcion leyendo fichero " + fichero + ": " + e);
		}
	}
	
	protected void loadPkgDesc() {
		String fichero = ConfigManager.getInstance().getProperty("pkg_description_file");
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] arr = linea.split(",");
				pkgDescs.put(arr[0],arr[1]);
			}
			fr.close();
		} catch (Exception e) {
			System.out.println("Excepcion leyendo fichero " + fichero + ": " + e);
		}
	}
	
	protected void loadPackageNames() {
		String fichero = ConfigManager.getInstance().getProperty("pkg_name_file");
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null) {
				pkgNames.add(linea);

			}
			fr.close();
		} catch (Exception e) {
			System.out.println("Excepcion leyendo fichero " + fichero + ": " + e);
		}
	}

	public boolean isPkgName(String name) {
		return pkgNames.contains(name);
	}
	
	public boolean isHomeName(String name) {
		return pkgHomePages.containsKey(name);
	}

	public ArrayList<String> getPkgNames() {
		return pkgNames;
	}
	
	public Set<String> getHomePages() {
		return pkgHomePages.keySet();
	}

	public String getPkgName(String homepage) {
		return pkgHomePages.get(homepage);
	}

	public String getHomePage(String entity) {
		return pkgHomePagesByPkgs.get(entity);
	}
	
	public String getDesc(String pkg) {
		return pkgDescs.get(pkg);
	}

}