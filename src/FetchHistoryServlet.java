

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * Servlet implementation class FetchHistory
 */
@WebServlet("/FetchHistory")
public class FetchHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(FetchHistoryServlet.class);      
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FetchHistoryServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	private class ServletRtv{
		int result;
		int historySize;
		ArrayList<UserSearchHistory> userHistoryList;
		public ServletRtv(){
			result = 0;
			historySize = 0;
			userHistoryList = new ArrayList<UserSearchHistory>();
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CurrUser currUser = SessionManager.retrieveUsr(request);

		ServletRtv servletRtv = new ServletRtv();
		
		if (currUser == null){
			log.error("WTF: this servlet should not be called now!");
			servletRtv.result = 1;
		} else {

			String username = currUser.getUserName();
			servletRtv.historySize = currUser.history.size();
			if (currUser.history.size() == 0){
				servletRtv.historySize = 0;
			}else{
				DBCollection coll = DBHandle.getHistoryColl();
				for (String searchID : currUser.history){
					BasicDBObject query = new BasicDBObject().append("searchID", searchID);
					DBCursor cur = coll.find(query);
					if (cur.hasNext()){
						BasicDBObject historyDoc = (BasicDBObject) cur.next();
						UserSearchHistory usrHistory = new UserSearchHistory();
						usrHistory.searchID = (String) historyDoc.get("searchID");
						usrHistory.lastVisitTime = (Date) historyDoc.get("lastVisitTime");
						usrHistory.url = (String) historyDoc.get("url");
						usrHistory.domain = (String) historyDoc.get("domain");
						usrHistory.cntCouterpartUrl = (Integer) historyDoc.get("cntCouterpartUrl");
						usrHistory.cntGoodLink = (Integer) historyDoc.get("cntGoodLink");
						usrHistory.cntBadLink = (Integer) historyDoc.get("cntBadLink");
						usrHistory.cntTimeout = (Integer) historyDoc.get("cntTimeout");
						usrHistory.cntBadUrl = (Integer) historyDoc.get("cntBadUrl");
						usrHistory.detail = (ArrayList<CounterpartUrlInfo>) historyDoc.get("detail");
						servletRtv.userHistoryList.add(usrHistory);
					} else {
						servletRtv.result = 1;
						log.error("WTF: why no such searchID [" + searchID + "]");
					}
				}
			}
		
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	
		String json = new Gson().toJson(servletRtv);
		log.info(json);
		response.getWriter().write(json);
	}


}
