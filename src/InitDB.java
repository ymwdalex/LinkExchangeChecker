

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * Application Lifecycle Listener implementation class InitDB
 *
 */
@WebListener
public class InitDB implements ServletContextListener {

	private static Log log = LogFactory.getLog(InitDB.class);
	
    /**
     * Default constructor. 
     */
    public InitDB() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        
    	DBCollection coll = DBHandle.getCurrUsr();
		DBCursor cur = coll.find();
        if (!cur.hasNext()) {
        	log.info("No CurrUser collection yet, create:");
        	BasicDBObject newDocument = new BasicDBObject();
        	newDocument.put("type", "totalUser");
        	newDocument.put("number", 0);
        	coll.insert(newDocument);
        	newDocument = new BasicDBObject();
        	newDocument.put("type", "searchingUser");
        	newDocument.put("number", 0);
        	coll.insert(newDocument);
        	newDocument = new BasicDBObject();
        	newDocument.put("type", "onlineUser");
        	newDocument.put("number", 0);
        	coll.insert(newDocument);
        }
    	
    	UserManager.setSearchingUserNum(0);
    	UserManager.setOnlineUserNum(0);
    	UserManager.setTotalUserNum(UserManager.getRealTotalUserNum());
    	
    	log.info("Init Online User: " + UserManager.getCurrUser("onlineUser"));
    	log.info("Init Searching User: " + UserManager.getSearchingUserNum());
    	log.info("Init Total User: " + UserManager.getTotalUserNum());
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
