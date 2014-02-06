package edu.uhmanoa.studybuddies;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class GetClasses extends Activity {
	public static final String ROOT_URL = "https://www.sis.hawaii.edu/uhdad/avail.classes?i=MAN";
	String START_URL = "https://www.sis.hawaii.edu/uhdad";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_classes);
		//go to the home page and get the name
		getName names = new getName();
		names.execute(new String [] {ROOT_URL});
		
	}
	
	private class getName extends AsyncTask <String, Void, String>{

		@Override
		protected String doInBackground(String... urls) {
			Document getDoc = null;
			Document resDoc = null;
			String urlToClasses = "";
			String url = "";
			String classesResponse = "";
			try {
				//get the page response first
				Connection.Response get = Jsoup.connect(urls[0])
						.method(Method.GET)
						.execute();
				
				getDoc = get.parse();
				Elements body = getDoc.getElementsByTag("A");
				Element fullUrl = body.get(6);
				
				//get the link
				Pattern p = Pattern.compile("\".*\"");
				Matcher m = p.matcher(cleanURL(fullUrl.toString()));
				if (m.find()) {
					url = m.group(0);
				}
				String parsedURL = url.substring(2, url.length()-1);
				urlToClasses = START_URL + parsedURL;
				
				//Connect to the page with all of the classes
				Connection.Response res = Jsoup.connect(urlToClasses)
						.method(Method.GET)
						.execute();
				resDoc = res.parse();
				classesResponse = resDoc.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return classesResponse;
		}
        @Override
        protected void onPostExecute(String response) {
        	Log.w("response", response);
	        /*if (response != null) {          		
	        	if (response.contains("Successful")) {
	                    pd.dismiss();
	                    Log.w("authenticate","YAY!!!");
	                    launchGetClasses();
	        		}
	        		else {
	        			showErrorDialog(WRONG_INPUT_ERROR);
	        		}
	        }
	        else {
	        	showErrorDialog(CONNECTION_ERROR);
	        }*/
        }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_classes, menu);
		return true;
	}
	
	//returns the link of the current class
	public String getCurrentSemesterLink(String response) {
		return "";
	}

	//replaces escaped amps with actual amp
	public String cleanURL(String url) {
		return url.replace("amp;", "");
	}
}
