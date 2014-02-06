package edu.uhmanoa.studybuddies;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class GetClasses extends Activity {
	Map<String,String>cookieMap;
	public static final String ROOT_URL = "http://myuh.hawaii.edu/cp/home/next";
	public static final String REFERRER = "http://myuh.hawaii.edu/cps/welcome/loginok.html";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_classes);
		
		Intent thisIntent = this.getIntent();
		cookieMap = new HashMap<String, String>();
		//get the cookies for this session
		//convert the bundle into a map so can use with jsoup
		Bundle cookies = thisIntent.getBundleExtra(Authenticate.COOKIES);
		Log.w("cookies", cookies.size()+"");
		Set<String> keys = cookies.keySet();
		for (String key: keys) {
			cookieMap.put(key, cookies.getString(key));
		}		
		//go to the home page and get the name
		getName names = new getName();
		names.execute(new String [] {ROOT_URL});
		
	}
	
	private class getName extends AsyncTask <String, Void, String>{

		@Override
		protected String doInBackground(String... urls) {
			Document getDoc = null;
			String getResponse = "";
			try {
				//get the page response first
				Connection.Response get = Jsoup.connect(ROOT_URL)
						.cookies(cookieMap)
						.method(Method.GET)
						.execute();
				
				getDoc = get.parse();
				getResponse = getDoc.toString();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return getResponse;
		}
        @Override
        protected void onPostExecute(String response) {
/*        	Log.w("response", response);*/
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

}
