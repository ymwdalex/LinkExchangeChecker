import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SessionManager {
	private static Log log = LogFactory.getLog(SessionManager.class);
	private static ConcurrentHashMap<String, CurrUser> sessions = 
		new ConcurrentHashMap<String, CurrUser>();
	
	public static void addUsr(CurrUser usr){
		sessions.put(usr.getSessionID(), usr);
	}
	
	public static boolean removeUsr(String sessionID){
		CurrUser usr = retrieveUsr(sessionID);
		boolean rtv = sessions.remove(sessionID, usr);
		if (rtv){
			log.info("Remove current user<" + sessionID + "," + usr.getUserName() + "> successfully");
		}else{
			log.error("Why sessionID and usr mismatch!!!: <" + sessionID + "," + usr.getUserName() + ">");
		}
		return rtv;
	}
	
	public static boolean removeUsr(String sessionID, CurrUser usr){
		boolean rtv = sessions.remove(sessionID, usr);
		if (rtv){
			log.info("Remove current user<" + sessionID + "," + usr.getUserName() + "> successfully");
		}else{
			log.error("Why sessionID and usr mismatch!!!: <" + sessionID + "," + usr.getUserName() + ">");
		}
		return rtv;
	}
	
	public static CurrUser retrieveUsr(String sessionID){
		return sessions.get(sessionID);
	}
	
	public static CurrUser retrieveUsr(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session == null){
			return null;
		} else {
			CurrUser curr = sessions.get(session.getId()); 
			if (curr == null){
				log.fatal("Why this happens???");
			}
			return curr;
		}
	}
}
