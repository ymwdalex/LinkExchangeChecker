

import java.util.ArrayList;

public class AutoSuggestResponse {
	String query;
	ArrayList<String> suggestions;
	
	public AutoSuggestResponse(String strQuery){
		query = strQuery;
		suggestions = new ArrayList<String>();
	}
	
	public void addSuggestion(String str){
		suggestions.add(str);
	}
}

