

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Application Lifecycle Listener implementation class SessionListener
 *
 */
@WebListener
public class SessionListener implements HttpSessionListener {
	private static Log log = LogFactory.getLog(SessionListener.class);
    /**
     * Default constructor. 
     */
    public SessionListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
    	HttpSession session = arg0.getSession();
        String id = session.getId();
        
        SessionManager.removeUsr(id);
    	log.info("session[" + id + "] destory");
    	UserManager.decOnlineUserNumByOne();
		UserManager.publishOnlineUserNum();
    }
	
}
