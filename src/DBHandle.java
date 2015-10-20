import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class DBHandle {

	private static Log log = LogFactory.getLog(DBHandle.class);
//	final static DB db = connectToMongo("localhost" , 27017,  "AWT");
	final static DB db = connectToMongo("ec2-79-125-80-17.eu-west-1.compute.amazonaws.com" , 27017,  "AWT");
		
	final static DBCollection userInfoCollection = db.getCollection("UserInfo");
	final static DBCollection currUserCollection = db.getCollection("CurrUser");
	final static DBCollection historyInfoCollection = db.getCollection("HistoryInfo");
	
	public static DB getDB(){
		return db;
	}
	
	public static DBCollection getUsrColl(){
		return userInfoCollection;
	}
	
	public static DBCollection getCurrUsr(){
		return currUserCollection;
	}
	
	public static DBCollection getHistoryColl(){
		return historyInfoCollection;
	}
	
	//connetion to the database
	private static DB connectToMongo(String host, int port, String dbName){
		Mongo m;
		try {
			m = new Mongo(host , port );
			DB db = m.getDB( dbName );
			log.info("Connect " + host + ":"+ port + "_" + dbName);
			return db;
		} catch (Exception e) {
			log.error("got in the exception mongodb connection");
			return null;
		}
	}
}
	
//	public static Set<String> getAllCollection(){
//		return db.getCollectionNames();
//	}
//	
//	public static String getDBCollectionName(String urlAddress, String urlFilter, String crawlerDepth){
//		String dbCollectionName = "";
//		
//		if (!urlAddress.isEmpty()){
//			if (urlAddress.length() > 50){
//				dbCollectionName = urlAddress.substring(0, 49);
//			} else {
//				dbCollectionName = urlAddress;
//			}
//		}
//
//		if (!urlFilter.isEmpty()){
//			if (urlFilter.length() > 25){
//				dbCollectionName = dbCollectionName + "_" + urlFilter.substring(0, 25);
//			} else {
//				dbCollectionName = dbCollectionName + "_" + urlFilter;
//			}
//		}
//		if (!crawlerDepth.isEmpty()){
//			if (crawlerDepth.length() > 5){
//				dbCollectionName = dbCollectionName + "_" + crawlerDepth.substring(0, 5);
//			} else {
//				dbCollectionName = dbCollectionName + "_" + crawlerDepth;
//			}
//		}
//		
//		return dbCollectionName;
//	}
//	
//	public static boolean noSuchCollection(String urlAddress, String urlFilter, String crawlerDepth){
//		String dbCollectionName = getDBCollectionName(urlAddress, urlFilter, crawlerDepth);
//		System.out.println("Get/Create collection" + dbCollectionName);
//		
//		if (db.collectionExists(dbCollectionName)){
//			return false;
//		} else {
//			DBCollection coll = db.getCollection(dbCollectionName);
//			BasicDBObject doc = new BasicDBObject();
//			doc.put("urlAddress", urlAddress);
//			doc.put("domain", urlFilter);
//			doc.put("depth", Integer.parseInt(crawlerDepth));
//			doc.put("timestamp", new Date());
//
//			ArrayList<String> info = new ArrayList<String>();
//			doc.put("urls", info);			
//
//			coll.insert(doc);
//			
//			return true;
//		}
//	}
//
//	//add a new player
//	public static void addURL(String collectionName, String url){
//		DBCollection coll = db.getCollection(collectionName);
//		DBObject obj = coll.findOne();
//		ArrayList<String> urls =  (ArrayList<String>)obj.get("urls");
//		urls.add(url);
//		obj.put("urls", urls);
//	}

//	public static ArrayList<urlStruct> lookupUrl(String urlAddress){
//		DBCollection coll = db.getCollection("urls");
//		BasicDBObject query = new BasicDBObject();
//		query.put("urlAddress", urlAddress);
//		DBCursor cursor = coll.find(query);
//		while(cursor.hasNext()) {
//			DBObject tobj = cursor.next();
//			String url = (String)tobj.get("url");
//			String session = (String)tobj.get("session");
//			int number =  (Integer) tobj.get("number");
//			urlStruct tmpStruct = new urlStruct();
//			tmpStruct.add(new urlStruct(urlAddress, session, number));
//			
//			System.out.println(tmpStruct);
//		}
//
//
//	}

	
//	// Removes a player by the number of session
//	public void removeUrlBySession(String session){
//		DBCollection coll = db.getCollection("urls");
//
//		BasicDBObject document = new BasicDBObject();
//		document.put("session", session);
//		coll.remove(document);
//	}	

	
//	public static DocStruct getLastDoc(String collectionName){
//		//		ArrayList<Player> savePlayers = new ArrayList<Player>();
//
//		DBCollection coll = db.getCollection(collectionName);
//		DBCursor cursor = coll.find();
//		
//		DocStruct tmpStruct = new DocStruct(cursor);
//
//		return tmpStruct;
//	}
//
//	
//	//returns an array with all the players
//	public static ArrayList<DocStruct> getArrayOfUrl(String collectionName){
//		//		ArrayList<Player> savePlayers = new ArrayList<Player>();
//
//		DBCollection coll = db.getCollection("collectionName");
//		DBCursor cursor = coll.find();
//
//		ArrayList<DocStruct> tmpStruct = new ArrayList<DocStruct>();
//		while(cursor.hasNext()) {
//			DBObject tobj = cursor.next();
//			String urlAddress = (String)tobj.get("urlAddress");
//			String session = (String)tobj.get("session");
//			int number =  (Integer) tobj.get("number");
//
////			tmpStruct.add(new DocStruct(urlAddress, session, number));
//			
//			System.out.println(urlAddress);
//		}
//
//		return tmpStruct;
//	}
