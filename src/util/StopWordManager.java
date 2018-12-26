package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class StopWordManager {
	
	   private static StopWordManager instance = null;
	   private ArrayList<String> stopwords = null;
	   
	   protected StopWordManager() {
		   stopwords = new ArrayList<String>();
		   loadStopWords();
	   }
	   public static StopWordManager getInstance() {
	      if(instance == null) {
	         instance = new StopWordManager();
	      }
	      return instance;
	   }
	   
	   protected void loadStopWords() {
			String fichero = "resources/stopwords_en.txt";
		    try {
		      FileReader fr = new FileReader(fichero);
		      BufferedReader br = new BufferedReader(fr);
		      String linea;
		      while((linea = br.readLine()) != null){
		    	 stopwords.add(linea);
		    	  
		      }     
		      fr.close();      
		    }
		    catch(Exception e) {
		      System.out.println("Excepcion leyendo fichero "+ fichero + ": " + e);
		    }	
		}
	   
	   public boolean isStopWord(String name){
		   return stopwords.contains(name);
	   }
	   
	}