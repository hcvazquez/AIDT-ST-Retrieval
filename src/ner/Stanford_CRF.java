package ner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import util.PackageManager;
import util.StopWordManager;

public class Stanford_CRF implements EntityExtractor{
	
	public static final String STANFORD_CRF = "stanford_CRF";
	private static final String ORGANIZATION = "ORGANIZATION";
	private static final String PERSON = "PERSON";
	private static final String LOCATION = "LOCATION";
	
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
		return STANFORD_CRF;
	}

	@Override
	public List<String> getNamedEntities(String text) {
		
		List<String> entities = new ArrayList<String>();
		
		String[] sentences = text.split("\n");
		PackageManager pkgm = PackageManager.getInstance();
		StopWordManager swm = StopWordManager.getInstance();
		String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
	    AbstractSequenceClassifier<CoreLabel> classifier;
		try {
			classifier = CRFClassifier.getClassifier(serializedClassifier);
			for (String sentence : sentences) {
				if(!"".equals(sentence.trim())){
					String result = classifier.classifyToString(sentence);
					List<String> words = getClasses(result);
					for(String word:words){
						word = word.trim().toLowerCase();
						if (!"".equals(word) && !swm.isStopWord(word) && pkgm.isPkgName(word)) {
							entities.add(word);
						}
					}
				}
			}
		} catch (ClassCastException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entities;
		
	}

	private List<String> getClasses(String result) {
		List<String> entities = new ArrayList<String>();
		String[] arr = result.split("/");
		for(int i=1;i<arr.length;i++){
			if(PERSON.equals(arr[i]) || ORGANIZATION.equals(arr[i]) || LOCATION.equals(arr[i])){
				entities.add(arr[i-1]);
			}
		}
		return entities;
	}
	
}
