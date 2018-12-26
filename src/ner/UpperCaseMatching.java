package ner;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import ranking.RankedItem;
import util.PackageManager;
import util.StopWordManager;

public class UpperCaseMatching implements EntityExtractor{
	
	public static final String STRING_MATCHING = "str_match";
	
	@Override
	public List<String> getNamedEntities(Document tech) {
		
		tech.outputSettings(new Document.OutputSettings().prettyPrint(false));

		tech.select("br").append("\\n");
		tech.select("p").prepend("\\n\\n");
		String text = "";
		text = tech.html().replaceAll("\\\\n", "\n");
		text = Jsoup.clean(text, "", Whitelist.none(),
				new Document.OutputSettings().prettyPrint(false));
		
		return getNamedEntities(text);
		
	}

	@Override
	public String getTechniqueName() {
		return STRING_MATCHING;
	}

	@Override
	public List<String> getNamedEntities(String text) {
		
		List<String> entities = new ArrayList<String>();
		
		String[] words = text.split(" ");
		PackageManager pkgm = PackageManager.getInstance();
		StopWordManager swm = StopWordManager.getInstance();

		for (String word : words) {
			if (!"".equals(word) && !swm.isStopWord(word.trim().toLowerCase()) 
					&& pkgm.isPkgName(word.trim().toLowerCase())
					&& isUpperCase(word)) {
				entities.add(word);
			}
		}
		
		return entities;
		
	}

	public boolean isUpperCase(String word) {
		String UCRegEx = "([A-Z][a-z]*){1,}";
		if(word.matches(UCRegEx)){
			return true;
		}	
		return false;
	}
	
	
	
}
