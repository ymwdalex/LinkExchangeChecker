import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.twmacinta.util.MD5;


public class DBdebug {
	private static Log log = LogFactory.getLog(DBdebug.class);
	@SuppressWarnings("null")
	public static void main(String [] args){
		
//		String originalStr = "http://www.zhizhihu.com" + "_" + "zhizhihu.com" + "_" + "5098";
//		MD5 md5 = new MD5();
//	    try {
//			md5.Update(originalStr, null);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    String hash = md5.asHex();
//	    log.error(hash);
//		MessageDigest md = null;
//		try {
//			md = MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			log.error("WTF: MessageDigest.getInstance error");
//		}
//		byte[] thedigest = null;
//		thedigest = originalStr.getBytes();
//		System.out.println(originalStr.getBytes());
//		thedigest = md.digest(originalStr.getBytes());
//		System.out.println("thedigest: " + thedigest );		
//		String channelName = new String(thedigest);
//		System.out.println("thedigest: " + channelName );	
//		DBCollection coll = DBHandle.getUsrColl();
//		Set<String> test = new HashSet<String>();
//		test.add("ppp");
//		test.add("qqq");
//		test.add("rrr");
//		BasicDBObject doc = new BasicDBObject();
//		doc.put("history", test);
//		coll.insert(doc);
		
//		String username = "Zhe Sun";
//		String searchID = "asdf_wer";
//		String domain = "asd";
//		String inputUrl = "233";
//		DBCollection coll = DBHandle.getUsrColl();
//		BasicDBObject query = new BasicDBObject();
//		query.put("userName", username);
//		DBCursor cur = coll.find(query);
//		
//        if(cur.hasNext()) {
//        	BasicDBObject usrDoc = (BasicDBObject) cur.next();
//
//        	BasicDBObjectBuilder historyDoc = BasicDBObjectBuilder.start()
//    		.add("searchID", searchID)
//    		.add("url", inputUrl)
//    		.add("domain", domain)
//    		.add("numOutUrl", 20);
//        	
//        	BasicDBObject docHistory = null;
//        	docHistory = (BasicDBObject) usrDoc.get("history");
//        	if (docHistory == null){
//        		usrDoc.put("history", historyDoc.get());
//        		coll.findAndModify(new BasicDBObject().append("userName", username), usrDoc);
//        		docHistory = (BasicDBObject) usrDoc.get("history");
//        	}else{
////        		docHistory.findAndRemove(new BasicDBObject().append("searchID", searchID));
////        		(docHistory).insert(historyDoc.get());
//        	}
//        } else {
//        	//log.error("WTF: why no such document??? username[" + username + "]");
//        }
		

//		UserManager.setTotalUserNum(2);
//		DBCollection coll = DBHandle.getUsrColl();
////		coll.remove(); 
//		DBCursor cur = coll.find();
//		while (cur.hasNext()) {
//			BasicDBObject usrDoc = (BasicDBObject) cur.next();
////			usrDoc.append("history", null);
//			String username = (String) usrDoc.get("userName");
//			coll.findAndRemove(new BasicDBObject().append("userName", username));
//		}
//		UserManager.showAllUser();
		
		DBCollection coll = DBHandle.getCurrUsr();
		DBCursor cur = coll.find();
//		while (cur.hasNext()) {
//			BasicDBObject usrDoc = (BasicDBObject) cur.next();
//			String username = (String) usrDoc.get("type");
//			coll.findAndRemove(new BasicDBObject().append("type", username));
//		}
//		
//		coll = DBHandle.getUsrColl();
//		cur = coll.find();
//		while (cur.hasNext()) {
//			BasicDBObject usrDoc = (BasicDBObject) cur.next();
//			String username = (String) usrDoc.get("userName");
//			coll.findAndRemove(new BasicDBObject().append("userName", username));
//		}
//		
//		coll = DBHandle.getHistoryColl();
//		cur = coll.find();
//		while (cur.hasNext()) {
//			BasicDBObject usrDoc = (BasicDBObject) cur.next();
//			String username = (String) usrDoc.get("searchID");
//			coll.findAndRemove(new BasicDBObject().append("searchID", username));
//		}
		
		while (cur.hasNext()) {
        	System.out.println( cur.next());
        }	
        
        coll = DBHandle.getHistoryColl();
		
		cur = coll.find();
        while (cur.hasNext()) {
        	System.out.println( cur.next());
        }
        
        coll = DBHandle.getUsrColl();
		
		cur = coll.find();
        while (cur.hasNext()) {
        	System.out.println( cur.next());
        }
        
//        coll = DBHandle.getCurrUsr();
//		cur = coll.find();
//        if (!cur.hasNext()) {
//        	log.info("No CurrUser collection yet, create:");
//        	BasicDBObject newDocument = new BasicDBObject();
//        	newDocument.put("type", "totalUser");
//        	newDocument.put("number", 2);
//        	coll.insert(newDocument);
//        	newDocument = new BasicDBObject();
//        	newDocument.put("type", "searchingUser");
//        	newDocument.put("number", 0);
//        	coll.insert(newDocument);
//        	newDocument = new BasicDBObject();
//        	newDocument.put("type", "onlineUser");
//        	newDocument.put("number", 0);
//        	coll.insert(newDocument);
//        }
//		while (cur.hasNext()) {
//			DBObject usrDoc = (DBObject) cur.next();
//		}
////		
//		
//		UserManager.showAllUser();
		
//		UserManager.changeUserNum("onlineUser", 1);
//		System.out.println(UserManager.getCurrUser("onlineUser"));
//		UserManager.changeUserNum("onlineUser", -1);
//		System.out.println(UserManager.getCurrUser("onlineUser"));		
//		DBCollection coll = DBHandle.getCurrUsr();
//		BasicDBObject doc = new BasicDBObject();
//		doc.put("type", "totalUser");
//		doc.put("number", 3);
//		coll.insert(doc);
//		BasicDBObject doc2 = new BasicDBObject();
//		doc2.put("type", "onlineUser");
//		doc2.put("number", 0);
//		coll.insert(doc2);
//		BasicDBObject doc3 = new BasicDBObject();		
//		doc3.put("type", "searchingUser");
//		doc3.put("number", 0);
//		coll.insert(doc3);		
//		
//		DBCursor cur = coll.find();
//        while (cur.hasNext()) {
//        	System.out.println( cur.next());
//        }		
//		BasicDBObject doc = new BasicDBObject();
//		doc.put("number", 3);
//		coll.remove(doc);
	}
}
