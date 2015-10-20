


import java.io.IOException;
import java.util.ArrayList;
//import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

/**
 * Servlet implementation class MainServlet
 */
@SuppressWarnings("unused")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(MainServlet.class);
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MainServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.error("get");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String urlAddress = request.getParameter("urlToCrawl").toString();
		String urlFilter = request.getParameter("urlFilter").toString();
		String randomStr = request.getParameter("randomNum").toString();
		Boolean isSave = new Boolean(request.getParameter("isSave"));
		log.debug("Search: urlToCrawl[" + urlAddress + "] urlFilter[" + urlFilter + "] isSave[" + isSave + "]");
		
		AnalysisPage analyseJob = null;
		CurrUser currUser = SessionManager.retrieveUsr(request);
		// if login, searching and save this site, just quit
		if (currUser != null && isSave){
			String searchingKeyword = urlAddress + "_" + urlFilter;
			Boolean noSuchSearching;
			synchronized (currUser) {
				// don't forgot clear when search end
				noSuchSearching = currUser.currSearching.add(searchingKeyword);
	        }			
			if (!noSuchSearching){
				log.info("User is searching this site and save this site, quit it");
				// should be converted to JSON
				String info = "Ohh...";
				response.setContentType("html/text");
				response.setCharacterEncoding("UTF-8");
				// dummy response
				response.getWriter().write(info);
				return;
			}
		}

		if ( currUser == null){
			// no user login, cannot save	
			isSave = false;
			analyseJob = new AnalysisPage(currUser, "", urlAddress, urlFilter, randomStr, isSave);
		}else{
			// 
			analyseJob = new AnalysisPage(currUser, currUser.getUserName(), urlAddress, urlFilter, randomStr, isSave);
		}
		
		ArrayList<CounterpartUrlInfo> urlLists = analyseJob.preWork();
		String info = null;
		if (urlLists == null){
			info = "0";
		} else {
			info = Integer.toString(urlLists.size());
		}
		
		response.setContentType("html/text");
		response.setCharacterEncoding("UTF-8");
		// dummy response
		response.getWriter().write(info);
		
		if (urlLists != null) {
			UserManager.incSearchingUserNumByOne();
			UserManager.publishSearchingUserNum();			
			analyseJob.exec();
		}
	}

}


