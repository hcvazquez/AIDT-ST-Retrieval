package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import external.wrappers.CoverallsWrapper;
import util.PackageManager;

public class TestBrowserStringMatching {

	public static void main(String[] args) {
		
		String dataDir = "C:/npm_data/documentsOriginal/documents/";	
		
		String query = "https://coveralls.io/";
		
		Proxy proxy = new Proxy( //
				Proxy.Type.HTTP, //
				InetSocketAddress.createUnresolved("proxy.exa.unicen.edu.ar", 8080) //
		);
		
//		System.out.println();
//		System.out.println("RESULTADOS");

		List<String> technologies = PackageManager.getInstance().getPkgNames();
		
		for(String tech:technologies){
				//System.out.println(tech);
			
					if(!"aux".equals(tech) && !tech.startsWith("aux.") &&
							!"com1".equals(tech) && !tech.startsWith("com1.") &&
							!"com2".equals(tech) && !tech.startsWith("com2.") &&
							!"com3".equals(tech) && !tech.startsWith("com3.") &&
							!"com4".equals(tech) && !tech.startsWith("com4.") &&
							!"con".equals(tech) && ! tech.startsWith("con.")  &&
							!"lpt1".equals(tech) && !tech.startsWith("lpt1.") &&
							!"lpt2".equals(tech) && !tech.startsWith("lpt2.") &&
							!"lpt3".equals(tech) && !tech.startsWith("lpt3.") &&
							!"nul".equals(tech) && !tech.startsWith("nul.") &&
							!"con".equals(tech) && ! tech.startsWith("con.")  &&
							!"prn".equals(tech) && !tech.startsWith("prn.") &&
							!"clock$".equals(tech) && ! tech.startsWith("clock$.")  &&
							"com.mastercard.test".compareTo(tech)<0){
						
					//System.out.println(tech);
					String fichero = dataDir + tech + ".txt";
					FileReader fr = null;
					BufferedReader br = null;
					try {
		
						fr = new FileReader(fichero);
						br = new BufferedReader(fr);
						String linea;
						
						while ((linea = br.readLine()) != null) {
							//System.out.print(".");
							if(linea.contains(query)){
								String[] array = linea.split("https://coveralls.io/");
								for(String s:array){
									if(!s.contains("badge")&&s.contains("branch")){
										System.out.println(tech + " " + CoverallsWrapper.search("https://coveralls.io/"+s.split("\\)")[0], null));
										break;
									}
								}
								//results.add(tech);						
								break;
							}
						}
	
					} catch (Exception e) {
						//System.out.println("Excepcion leyendo fichero " + fichero + ": " + e);
					} finally {
						try {
							if (null != fr)
								fr.close();
						} catch (Exception e2) {
							//System.out.println("Excepcion cerrando fichero " + fichero + ": " + e2);
						}
					}
				}

		}
		
//		for(String url:results){
//				CoverallsWrapper.search(url, proxy);
//				System.out.println(url + CoverallsWrapper.search(url, proxy));
//		}

	}

	private static List<String> getCoverage(String dataDir, String query) {
		
		List<String> technologies = PackageManager.getInstance().getPkgNames();
		List<String> results = new ArrayList<String>();
		
		for(String tech:technologies){

//				System.out.println(tech);
					if(!"aux".equals(tech)){
					String fichero = dataDir + tech + ".txt";
					FileReader fr = null;
					BufferedReader br = null;
					try {
		
						fr = new FileReader(fichero);
						br = new BufferedReader(fr);
						String linea;
						
						while ((linea = br.readLine()) != null) {
							System.out.print(".");
							if(linea.contains(query)){
								String[] array = linea.split("https://coveralls.io/");
								for(String s:array){
									if(!s.contains("badge")&&s.contains("branch")){
										System.out.println("https://coveralls.io/"+s.split("\\)")[0]);
										results.add("https://coveralls.io/"+s.split("\\)")[0]);
										break;
									}
								}
								//results.add(tech);						
								break;
							}
						}
	
					} catch (Exception e) {
						//System.out.println("Excepcion leyendo fichero " + fichero + ": " + e);
					} finally {
						try {
							if (null != fr)
								fr.close();
						} catch (Exception e2) {
							//System.out.println("Excepcion cerrando fichero " + fichero + ": " + e2);
						}
					}
				}

		}
		
		return results;
	}

}
