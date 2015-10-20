import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;


public class UserManager {
	private static Log log = LogFactory.getLog(UserManager.class);
	
	public static int authorizeUser(String usrName, String password){
		DBCollection coll = DBHandle.getUsrColl();
				
		BasicDBObject query = new BasicDBObject();
		query.put("userName", usrName);

		DBCursor cur = coll.find(query);
        if(cur.hasNext()) {
        	BasicDBObject doc = (BasicDBObject) cur.next();
        	if (doc.get("password").equals(password)){
        		// Has user, and password is right
        		return 0;
        	} else {
        		// password error
        		return 2;
        	}
        } else {
        	// No such user
        	return 1;
        }
	}
	
	public static boolean existUsrName(String usrName){
		DBCollection coll = DBHandle.getUsrColl();
		
		BasicDBObject query = new BasicDBObject();
		query.put("userName", usrName);

		DBCursor cur = coll.find(query);

        if(cur.hasNext()) {
            return true;
        } else {
        	return false;
        }
	}
	
	public static void insertUser(String usrName, 
								  String signup_email,
								  String signup_password){
		DBCollection coll = DBHandle.getUsrColl();
		
		BasicDBObject doc = new BasicDBObject();
        doc.put("userName", usrName);
        doc.put("userEmail", signup_email);
        doc.put("password", signup_password);
        doc.put("history", new HashSet<String>());
        coll.insert(doc);
	}	
	
	public static void showAllUser(){
		DBCollection coll = DBHandle.getUsrColl();
		
		DBCursor cur = coll.find();
        while (cur.hasNext()) {
        	System.out.println( cur.next());
        }		
	}
	
	public static int getCurrUser(String type){
		DBCollection coll = DBHandle.getCurrUsr();
		
		BasicDBObject query = new BasicDBObject();
		query.put("type", type);
		DBCursor cur = coll.find(query);

        if(cur.hasNext()) {
        	BasicDBObject obj = (BasicDBObject) cur.next();
        	return obj.getInt("number");
        } else {
        	return 0;
        }
	}

	public static void updateUserNum(String type, int num){
		DBCollection coll = DBHandle.getCurrUsr();
		
		//find type = onlineUser, and increase its "number" value by 1
		BasicDBObject newDocument = new BasicDBObject().append("$set", 
				new BasicDBObject().append("number", num));
 
		coll.update(new BasicDBObject().append("type", type), newDocument);		
	}
	
	public static void changeUserNum(String type, int num){
		DBCollection coll = DBHandle.getCurrUsr();
		
		//find type = onlineUser, and increase its "number" value by 1
		BasicDBObject newDocument = new BasicDBObject().append("$inc", 
				new BasicDBObject().append("number", num));
 
		coll.update(new BasicDBObject().append("type", type), newDocument);		
	}
	
	public static void publishOnlineUserNum(){
		String json = new Gson().toJson(getOnlineUserNum());
        try {
                Pusher.triggerPush("UserNumberChannel", "updateOnlineUser", json);
        } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } 		
	}
	
	public static void publishTotalUserNum(){
		String json = new Gson().toJson(getTotalUserNum());
        try {
                Pusher.triggerPush("UserNumberChannel", "updateTotalUser", json);
        } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } 		
	}

	public static void publishSearchingUserNum(){
		String json = new Gson().toJson(getSearchingUserNum());
        try {
                Pusher.triggerPush("UserNumberChannel", "updateSearchingUser", json);
        } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } 		
	}
	
	public static int getOnlineUserNum(){
		return getCurrUser("onlineUser");
	}
	
	public static void setOnlineUserNum(int num){
		updateUserNum("onlineUser", num);
	}
	
	public static void incOnlineUserNumByOne(){
		changeUserNum("onlineUser", 1);
	}
	
	public static void decOnlineUserNumByOne(){
		changeUserNum("onlineUser", -1);
	}
	
	public static int getTotalUserNum(){
		return getCurrUser("totalUser");
	}

	public static int getRealTotalUserNum(){
		DBCollection coll = DBHandle.getUsrColl();
		
		DBCursor cur = coll.find();
		int i = 0;
        while (cur.hasNext()) {
        	cur.next();
        	i++;
        }	
        return i;
	}
	
	public static void setTotalUserNum(int num){
		updateUserNum("totalUser", num);
	}
	
	public static void incTotalUserNumByOne(){
		changeUserNum("totalUser", 1);
	}
	
	public static void decTotalUserNumByOne(){
		changeUserNum("totalUser", -1);
	}
	
	public static int getSearchingUserNum(){
		return getCurrUser("searchingUser");
	}
	
	public static void setSearchingUserNum(int num){
		updateUserNum("searchingUser", num);
	}
	
	public static void incSearchingUserNumByOne(){
		changeUserNum("searchingUser", 1);
	}
	
	public static void decSearchingUserNumByOne(){
		changeUserNum("searchingUser", -1);
	}	
}
