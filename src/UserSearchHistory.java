import java.util.ArrayList;
import java.util.Date;

public class UserSearchHistory {
	String searchID;
	Date lastVisitTime;
	String url;
	String domain;
	int cntCouterpartUrl;
	int cntGoodLink;
	int cntBadLink;
	int cntTimeout;
	int cntBadUrl;
	ArrayList<CounterpartUrlInfo> detail;
}
