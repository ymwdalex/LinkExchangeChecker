import java.util.HashSet;
import java.util.Set;

public class CurrUser {
	private String sessionID = null;
	private String usrname = null;
	Set<String> history = null;
	Set<String> currSearching = null;
	public CurrUser(String sessionID, String usrname){
		this.sessionID = sessionID;
		this.usrname = usrname;
		this.history = new HashSet<String>();
		this.currSearching = new HashSet<String>();
	}
	public String getSessionID(){
		return sessionID;
	}
	public String getUserName(){
		return usrname;
	}	
}
