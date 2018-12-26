package internal.lucene;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONArray;
import org.json.JSONObject;

import external.wrappers.NPMUtilWrapper;
import metasearch.cache.CacheContentManager;
import util.ConfigManager;

public class Indexer {

   private IndexWriter writer;
   private Proxy proxy;
   private NPMUtilWrapper nrw;


   public Indexer(String indexDirectoryPath, Proxy proxy) throws IOException{
	   
	  this.proxy = proxy;
	  
	  nrw = new NPMUtilWrapper();
      //this directory will contain the indexes
      Directory indexDirectory = 
         FSDirectory.open(Paths.get(indexDirectoryPath));

      //create the indexer
      IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
      writer = new IndexWriter(indexDirectory,config);
   }

   public void close() throws CorruptIndexException, IOException{
      writer.close();
   }

   private Document getDocument(JSONObject json) throws IOException{
     
	  Document document = new Document();
	  String id = (String)json.get(LuceneConstants.ID);
      document.add(new StringField(LuceneConstants.ID, id , Field.Store.YES));
      document.add(new TextField(LuceneConstants.DESCR, getDescription(json) , Field.Store.YES));
      document.add(new TextField(LuceneConstants.KEYS, getKeywords(json) , Field.Store.YES));
      document.add(new TextField(LuceneConstants.README, getReadmeString(id) , Field.Store.YES));

      return document;
   }    
   
   public int createIndex(String jsondata) {
	    try {
	      FileReader fr = new FileReader(jsondata);
	      BufferedReader br = new BufferedReader(fr);
	      String linea;
	      int count = 0;
	      while((linea = br.readLine()) != null){
	    	  char last = linea.charAt(linea.length()-1);
	    	  if(','==last){
	    		  linea = linea.substring(0, linea.length()-1);
	    	  }
	    	  try{
	    		JSONObject o = new JSONObject(linea);
	    		indexFile(o);
	    	  }catch(Exception e) {
	    	      System.out.println("Excepcion parseando json "+ linea + ": " + e);
	  	      }
	    	  System.out.println(count++);
	      }
	      fr.close();
	    }
	    catch(Exception e) {
	      System.out.println("Excepcion leyendo fichero "+ jsondata + ": " + e);
	    }
	    return writer.numDocs();	
	}

   private String getKeywords(JSONObject json){
	   List array = null;
	   String keys = "";
	   try{
		   array = ((JSONArray) ((JSONObject)json.get(LuceneConstants.JSON_VALUE)).get(LuceneConstants.KEYS)).toList();
		   for(int i=0;i<array.size();i++){
			   String key = (String) array.get(i);
			   keys+=" "+key;
		   }
	   }catch(Exception e){
		   
	   }
	   return keys;
   }
   
   private String getDescription(JSONObject json){
	   String desc = "";
	   try{
		   desc = (String)((JSONObject)json.get(LuceneConstants.JSON_VALUE)).get(LuceneConstants.DESCR);
	   }catch(Exception e){
		   
	   }
	   return desc;
   }
   
   

   private String getReadmeString(String id){
	   String readme = "";
	   try{
		   readme = CacheContentManager.getInstance().loadFileContent(new File(ConfigManager.getInstance().getProperty("readme_dir")+"/"+id+".txt"));
	   }catch(Exception e){
		   
	   }
	   return readme;
   }
   
   private void indexFile(JSONObject json) throws IOException{
      System.out.println("Indexing "+  (String)json.get("id"));
      Document document = getDocument(json);
      System.out.println(document.get(LuceneConstants.KEYS));
      writer.addDocument(document);
   }
   
   
}