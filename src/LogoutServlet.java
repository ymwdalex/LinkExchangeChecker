

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
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(LogoutServlet.class);	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("logout doGet");

		int rtv = 0;
		if (SessionManager.retrieveUsr(request) == null){
			log.error("This session has no user login yet!");
			rtv = 101;
		} else {
			CurrUser currUser = SessionManager.retrieveUsr(request);
			log.info("Logout User[" + currUser.getUserName() + "]");
			HttpSession session = request.getSession(false);
			// remove user should be in SessionListener, manually remove or automatically
			//SessionManager.removeUsr(session.getId(), currUser);
			session.invalidate();
			rtv = 1;
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	
		String json = new Gson().toJson(rtv);
		response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.error("Why a POST method here???");
	}

}
