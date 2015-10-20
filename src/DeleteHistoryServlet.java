

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
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * Servlet implementation class DeleteHistoryServlet
 */
@WebServlet("/DeleteHistoryServlet")
public class DeleteHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DeleteHistoryServlet.class);   
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteHistoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchID = request.getParameter("searchID").toString();
		log.info("Delete [" + searchID + "]");
		CurrUser currUser = SessionManager.retrieveUsr(request);

		int rtv = 0;
		if (currUser == null){
			log.error("WTF: this servlet should not be called if not login!");
			rtv = 1;
		} else {
			String username = currUser.getUserName();
			if (!currUser.history.contains(searchID)){
				rtv = 1;
				log.error("WTF: no such searchID [" + searchID + "] in user[" + username + "]");
			} else {
				log.info("Delete user history in History collection");
				DBCollection coll = DBHandle.getHistoryColl();
				coll.findAndRemove(new BasicDBObject().append("searchID", searchID));

				// update user history in UsrInfo collection 
				log.info("update user history in UsrInfo collection");
				coll = DBHandle.getUsrColl();
				BasicDBObject query = new BasicDBObject();
				query.put("userName", username);
				DBCursor cur = coll.find(query);
				if(cur.hasNext()) {
					BasicDBObject usrDoc = (BasicDBObject) cur.next();
					synchronized (currUser) {
						// don't forgot clear when search end
						currUser.history.remove(searchID);
					}
					usrDoc.removeField("history");
					usrDoc.append("history", currUser.history);
					coll.findAndModify(new BasicDBObject().append("userName", username), usrDoc);
				} else {
					log.error("WTF: why no such document??? username[" + username + "]");
				}
			}
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	
		String json = new Gson().toJson(rtv);
		log.info(json);
		response.getWriter().write(json);		
	}

}
