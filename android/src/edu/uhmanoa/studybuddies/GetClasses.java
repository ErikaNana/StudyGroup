package edu.uhmanoa.studybuddies;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class GetClasses extends Activity {
	public static final String ROOT_URL = "https://www.sis.hawaii.edu/uhdad/avail.classes?i=MAN";
	public static final String COOKIE = "JSESSIONID";
	public static final int CRN = 0;
	public static final int URL = 1;
	public static final int SEARCH_NAME = 2;
	
	//to connect to different urls
	public static final int CONNECT_CURRENT_SEMESTER = 3;
	public static final int CONNECT_DEPARTMENT = 4;
	
	//get class availability
	String BASE_URL = "https://www.sis.hawaii.edu/uhdad";
	
	String mCookieValue = "";
	String mLoginResponse = "";
	HashMap<String,String[]>classInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		classInfo = new HashMap<String,String[]>();
		
		Intent thisIntent = this.getIntent();
		//get cookies 
		mCookieValue = thisIntent.getStringExtra(Authenticate.COOKIE_TYPE);

		//get class names
		mLoginResponse = thisIntent.getStringExtra(Authenticate.LOGIN_RESPONSE);
		getClassAndCRN(mLoginResponse);
		
		//just test
		String[] testing = classInfo.get("ICS 414");
		String searchName = testing[SEARCH_NAME];
		
		Connect getSemester = new Connect(CONNECT_CURRENT_SEMESTER);
		getSemester.execute(new String [] {ROOT_URL});
		
	}
	
	private class Connect extends AsyncTask <String, Void, String>{
		
		int which = 0;
		
		public Connect(int which) {
			this.which = which;
		}
		
		@Override
		protected String doInBackground(String... urls) {
			Document resDoc = null;
			String semesterResponse = "";
			
			try {				
				//Connect to the page with all of the classes
				Connection.Response res = Jsoup.connect(urls[0])
						.method(Method.GET)
						.execute();
				resDoc = res.parse();
				semesterResponse = resDoc.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return semesterResponse;
		}
        @Override
        protected void onPostExecute(String response) {
			switch(which) {
			case CONNECT_CURRENT_SEMESTER:
				
				String link = getCurrentSemesterLink(response);
	        	link = BASE_URL + link;
	        	
	        	//connect to the pages with departments
	        	Connect getSemester = new Connect(CONNECT_DEPARTMENT);
	    		getSemester.execute(new String [] {link});
	    		
				break;
			case CONNECT_DEPARTMENT:
				Log.w("response", response);
				break;
		}
        }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_classes, menu);
		return true;
	}
	
	public String getCurrentSemesterLink(String response) {
		Document doc = Jsoup.parse(response);
		Elements links = doc.getElementsByTag("a");
		Element link = links.get(6);
		String url = link.attr("HREF");
		
		//remove the dot
		url = url.replaceFirst(".", "");
		return url;
	}
	//get the classAndCRN
	public void getClassAndCRN(String response) {
		Document doc = Jsoup.parse(response);
		Elements links = doc.getElementsByTag("a");
		
		for (Element link : links) {
			String url = link.attr("abs:href");
			if (url.isEmpty() || url.contains("logout")) {
				continue;
			}
			String text = link.attr("title");
			
			Pattern classNamePattern = Pattern.compile("[A-Z]+-[0-9]+");
			Pattern crnPattern = Pattern.compile("\\.[0-9]+");
			Pattern searchClassPattern = Pattern.compile("[A-Z]+");
			
			String className = "";
			String crnMatch = "";
			String searchName = "";
			
			Matcher matcher = classNamePattern.matcher(text);
			if (matcher.find()) {
				className = matcher.group(0);
				matcher = crnPattern.matcher(text);
				
				if (matcher.find()) {
					crnMatch = matcher.group(0);
					
					//strip . from crn
					crnMatch = crnMatch.replace(".", "");
					
					//strip - from class name
					className = className.replace("-", " "); 
					
					//get the class prefix to search
					matcher = searchClassPattern.matcher(className);
					if (matcher.find()) {
						searchName = matcher.group(0);
					}
				}					
				String [] data = {crnMatch, url, searchName};
				classInfo.put(className, data);
			}
		}
		
		Log.w("Classinfo", classInfo.toString());
	}
}
