

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;


/**
 * Servlet implementation class AutoSuggestion
 */
@WebServlet("/AutoSuggestion")
public class AutoSuggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(AutoSuggestion.class);   
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoSuggestion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Get AutoSuggestion");
		CurrUser currUser = SessionManager.retrieveUsr(request);
		String queryStr = request.getParameter("term").toString();
//		AutoSuggestResponse autoSuggestResponse = new AutoSuggestResponse(queryStr);
		ArrayList<String> autoSuggestResponse = new ArrayList<String>();
		
		if (currUser == null){
			log.info("Who care you: go to register!");
			
		} else {
			for (String item : currUser.history){
				String url = item.substring(0, item.indexOf('_'));
				log.info(url);
				if (url.startsWith(queryStr)){
					autoSuggestResponse.add(url);
				}
			}
		}

		String json = new Gson().toJson(autoSuggestResponse);
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);							
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
