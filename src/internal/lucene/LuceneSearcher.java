package internal.lucene;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneSearcher {
	
   IndexSearcher indexSearcher;
   MultiFieldQueryParser queryParser;
   Query query;
   
   public LuceneSearcher(String indexDirectoryPath) 
      throws IOException{
	   
	  Directory dir = FSDirectory.open(Paths.get(indexDirectoryPath));
	  IndexReader reader = DirectoryReader.open(dir);
	  indexSearcher = new IndexSearcher(reader);

	  String[] fields = new String[]{/*LuceneConstants.ID,*/ LuceneConstants.KEYS, LuceneConstants.DESCR, LuceneConstants.README};

	  //Boosting
	  HashMap<String,Float> boosts = new HashMap<String,Float>();
	  boosts.put(fields[0], 1.0F);
	  boosts.put(fields[1], 1.0F);
	  boosts.put(fields[2], 1.0F);/*
	  boosts.put(fields[3], 1.0F);*/
	  
	  queryParser = new MultiFieldQueryParser(fields,
    		  new StandardAnalyzer()
    		  ,boosts);
      //queryParser.setDefaultOperator(Operator.AND);
   }
   
   public TopDocs search( String searchQuery, int results) 
      throws IOException, ParseException{
      query = queryParser.parse(searchQuery);
      return indexSearcher.search(query, results);
   }

   public Document getDocument(ScoreDoc scoreDoc) 
      throws CorruptIndexException, IOException{
      return indexSearcher.doc(scoreDoc.doc);	
   }
}