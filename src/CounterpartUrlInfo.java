

public class CounterpartUrlInfo {

	String url;
	String text;
	boolean valide;
	boolean share;
	int status;
	
	public void setStatus(int status){
		this.status = status; 
	}
	
	public void setValide(boolean valide){
		this.valide = valide;
	}
	
	public void setAttr(boolean share){
		this.share = share;
	}
	
	public CounterpartUrlInfo(String url, String t){
		this.url = url;
		this.text = t;
	}
}
