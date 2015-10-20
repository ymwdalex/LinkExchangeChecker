

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.UrlValidator;
import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.twmacinta.util.MD5;

public class AnalysisPage {

	private static Log log = LogFactory.getLog(AnalysisPage.class);
	
	String username;
	String randomStr;
	String inputUrl;
	String domain;
	ArrayList<CounterpartUrlInfo> outUrls = null;
	String channelName = null;
	Boolean autoSave = false;
	String searchID = null;
	DBCollection docHistory = null;
	int statGood = 0;
	int statWithoutShare = 0;
	int statTimeout = 0;
	int statBadUrl = 0;
	CurrUser currUser = null;
	
	public AnalysisPage(CurrUser currUser, String username, String inputUrl, String domain, String randomStr, boolean isSave){
		this.inputUrl = inputUrl;
		this.randomStr = randomStr;
		this.domain = domain;
		this.outUrls = null;
		this.autoSave = isSave;
		String originalStr = inputUrl + "_" + domain + "_" + randomStr;
		MD5 md5 = new MD5();
	    try {
			md5.Update(originalStr, null);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("WTF: why MD5 fail with the string: [" + originalStr + "]");
		}
		this.channelName  = md5.asHex();
		log.info("[" + originalStr + "] ==> [" + channelName + "]");
		this.searchID = inputUrl + "_" + domain + "_" + username;
		this.username = username;
		this.currUser = currUser;
		
		statGood = 0;
		statWithoutShare = 0;
		statTimeout = 0;
		statBadUrl = 0;
	}

	private int isGoodSite(String url){
		Document outDoc = null;
		
		try {
			outDoc = Jsoup.connect(url).timeout(10*1000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Cannot fetch url: " + url);
			return 0;
		}
		Elements inLinks = outDoc.select("a[href]");
		for (Element inLink : inLinks) {
			if (inputUrl.contains(inLink.attr("abs:href"))){
				log.info("[" + url + "] Has my link: " + inLink.attr("abs:href"));
				return 1;
			}
		}
		log.info("[" + url + "] don't have my link");
		return 2;
	}

	private boolean urlValidate(String url){
		String[] schemes = {"http","https"};
		UrlValidator urlValidator = new UrlValidator(schemes);

		if (urlValidator.isValid(url)) {
			return true;
		} else {
			System.out.println("url is invalid: [" + url + "]");
			return false;
		}		
	}
	
	class JsonInf {
		int status; // 0:error  1:start  2:end
		int linkSize;
		int currentLink;
		String urlText;
		String url;
		int urlWorkStatus;
		int urlAnalyseResult;
	}
	
	public ArrayList<CounterpartUrlInfo> preWork(){
		Document doc = null;
		try {
			doc = Jsoup.connect(inputUrl).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Elements Links = doc.select("a[href]");	
		outUrls = new ArrayList<CounterpartUrlInfo>();
		for (Element link : Links) {
			String outLinkStr = link.attr("abs:href");
			String outLinkTxt = link.text();
			if (!outLinkStr.contains(domain)){
				System.out.println(outLinkStr);
				outUrls.add(new CounterpartUrlInfo(outLinkStr, outLinkTxt));
			}
		}
		log.info("Total number of Links: " + Links.size() + ", out link: " + outUrls.size());
		
		return outUrls;
	}
	
	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width-1) + ".";
		else
			return s;
	}
	
	private void sendMessage(CounterpartUrlInfo link, int searchStatus, int workState, int index){
		JsonInf jsonRtv = new JsonInf();
		jsonRtv.status = searchStatus; // not finish
		jsonRtv.currentLink = index;
		jsonRtv.url = link.url;
		jsonRtv.urlText = link.text;
		jsonRtv.urlWorkStatus = workState; // good link, analysing
		String json = new Gson().toJson(jsonRtv);
		System.out.println(json);
		try {
			Pusher.triggerPush(channelName, "update", json);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void exec() {
		Runnable r = new Runnable() {
			public void run() {
				CounterpartUrlInfo link = null;
				for (int i=0; i<outUrls.size(); i++){
					link = outUrls.get(i);
					if (urlValidate(link.url)){
						link.setValide(true);
						link.setStatus(1);
						// 1: good link, start working
						sendMessage(link, 1, 1, i);
						
						int rtv = isGoodSite(link.url);
						if (rtv == 1){
							link.setAttr(true);
							link.setStatus(2);
							// 2: good link, has my link
							sendMessage(link, 1, 2, i);
							statGood++;
						}else if (rtv == 2){
							link.setAttr(false);
							link.setStatus(3);
							// 3: bad link, don't have my link
							sendMessage(link, 1, 3, i);
							statWithoutShare++;
						} else if (rtv == 0){
							link.setStatus(0);
							sendMessage(link, 1, 0, i);
							statTimeout++;
						}
							
					}else{
						link.setStatus(0);
						link.setValide(false);
						// 0: bad link
						sendMessage(link, 1, 0, i);
						statBadUrl++;
					}		
					
					// update
					outUrls.set(i, link);
				}

				// save into DB
				if (autoSave){
					// update user history in History collection
					log.info("update user history in History collection");
					DBCollection coll = DBHandle.getHistoryColl();
					coll.findAndRemove(new BasicDBObject().append("searchID", searchID));
					
					ArrayList<BasicDBObject> outUrlsDetails = new ArrayList<BasicDBObject>();
					for (CounterpartUrlInfo link1 : outUrls){
						BasicDBObjectBuilder obj = BasicDBObjectBuilder.start()
							.add("url", link1.url)
							.add("text", link1.text)
							.add("status", link1.status);
						outUrlsDetails.add((BasicDBObject) obj.get());
					}
					
					BasicDBObjectBuilder historyDoc = BasicDBObjectBuilder.start()
						.add("searchID", searchID)
						.add("lastVisitTime", new Date())
						.add("url", inputUrl)
						.add("domain", domain)
						.add("cntCouterpartUrl", outUrls.size())
						.add("cntGoodLink", statGood)
						.add("cntBadLink", statWithoutShare)
						.add("cntTimeout", statTimeout)
						.add("cntBadUrl", statBadUrl)
						.add("detail", outUrlsDetails);
					coll.insert(historyDoc.get());
					
					// update user history in UsrInfo collection 
					log.info("update user history in UsrInfo collection");
					coll = DBHandle.getUsrColl();
					BasicDBObject query = new BasicDBObject();
					query.put("userName", username);
					DBCursor cur = coll.find(query);
					if(cur.hasNext()) {
						BasicDBObject usrDoc = (BasicDBObject) cur.next();
						usrDoc.removeField("history");
						currUser.history.add(searchID);
						usrDoc.append("history", currUser.history);
						coll.findAndModify(new BasicDBObject().append("userName", username), usrDoc);
			        } else {
			        	log.error("WTF: why no such document??? username[" + username + "]");
			        }
					
					// Remove this search from sessionID
					if (currUser != null){
						Boolean isRemoved;
						String searchJobStr = inputUrl + "_" + domain;
						synchronized (currUser) {
							// don't forgot clear when search end
							isRemoved = currUser.currSearching.remove(searchJobStr);
				        }
						if (!isRemoved){
							log.error("WTF? Why [" + searchJobStr + "] is not in searchingList??");
						}
					}
				}
				
				// end
				sendMessage(new CounterpartUrlInfo("",""), 2, 0, 0);				
				
				UserManager.decSearchingUserNumByOne();
				UserManager.publishSearchingUserNum();				
			} 
		};
		
		Thread t = new Thread(r);
		t.start();
	}	
}
