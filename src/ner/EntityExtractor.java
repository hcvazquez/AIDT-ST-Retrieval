package ner;

import java.util.List;

import org.jsoup.nodes.Document;

public interface EntityExtractor {
	
	public List<String> getNamedEntities(Document tech);
	
	public String getTechniqueName();

	List<String> getNamedEntities(String text);

}
