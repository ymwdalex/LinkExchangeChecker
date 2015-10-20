

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	
	private static Log log = LogFactory.getLog(LoginServlet.class);
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("login_username").toString();
		String password = request.getParameter("login_password").toString();
		log.info("Login: [" + username + "] [" + password + "]");

		int rtv = 0;
		if (SessionManager.retrieveUsr(request) != null){
			log.error("This session has user login already!");
			rtv = 100;
		} else {
			rtv = UserManager.authorizeUser(username, password); 
			if (rtv == 0){
				log.info("new session, first log in");
				// user name and password are right
				HttpSession session = request.getSession(true);
				CurrUser currUser = new CurrUser(session.getId(), username);
				
				// get history info from DB
				DBCollection coll = DBHandle.getUsrColl();
				BasicDBObject query = new BasicDBObject();
				query.put("userName", username);
				DBCursor cur = coll.find(query);
				if(cur.hasNext()) {
					BasicDBObject usrDoc = (BasicDBObject) cur.next();
					ArrayList<String> hisDoc= (ArrayList<String>)usrDoc.get("history");
					synchronized (currUser) {
						// don't forgot clear when search end
						currUser.history.addAll(hisDoc);
			        }	
				} else {
					rtv = 0;
					log.fatal("WTF: why no such document??? username[" + username + "]");
				}	
				
				SessionManager.addUsr(currUser);
								
				// update Notice Board
				UserManager.incOnlineUserNumByOne();
				UserManager.publishOnlineUserNum();				
			} else if (rtv == 1){
				log.info("User name not right");
			} else if (rtv == 2){
				log.info("Password not right");
			}
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	
		String json = new Gson().toJson(rtv);
		response.getWriter().write(json);
	}

}
