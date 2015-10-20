

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

/**
 * Servlet implementation class SessionTracker
 */
@WebServlet("/SessionTracker")
public class UserStatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(UserStatServlet.class);
	
	private class SessionTrackerRtv{
		int rtv;
		String usrname;
		int searchingUsrNum;
		int totalUsrNum;
		int onlineUsrNum;
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserStatServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("Welcome page retrieve");

		SessionTrackerRtv res = new SessionTrackerRtv();
		CurrUser currUser = SessionManager.retrieveUsr(request);
		
		if (currUser == null){
			res.rtv = 0;
		} else {
			log.info("old session, user[" + currUser.getUserName() + "]");
			res.rtv = 1;
			res.usrname = currUser.getUserName();
		}
		res.searchingUsrNum = UserManager.getSearchingUserNum();
		res.onlineUsrNum = UserManager.getOnlineUserNum();
		res.totalUsrNum = UserManager.getTotalUserNum();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	
		String json = new Gson().toJson(res);
		response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.error("SessionTrackerServlet should not have POST method!!!");
	}

}
