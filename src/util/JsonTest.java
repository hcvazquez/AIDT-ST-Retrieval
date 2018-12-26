package util;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonTest {
	
	public static final String URL = "https://www.wordsapi.com/mashape/words?when=2017-04-25T14:07:08.030Z&encrypted=8cfdb28de722979be89407beeb58bebeaeb0250930fc97b8&limit=400000";

	public static void main(String[] args) throws Exception {
		
	    String content = new Scanner(new File("resources/wordsAPI-words.json")).useDelimiter("\\Z").next();
	    
	    JsonObject obj = new JsonParser().parse(content).getAsJsonObject();
	    JsonObject directDependencies = obj.getAsJsonObject("results");
		//Set<Map.Entry<String, JsonElement>> entries = directDependencies.entrySet();//will return members of your object
	    JsonElement data = directDependencies.get("data");
	    JsonArray entries = data.getAsJsonArray();//will return members of your object
		for (JsonElement entry: entries) {
		    System.out.println(entry.getAsString());
		}
	}
}
