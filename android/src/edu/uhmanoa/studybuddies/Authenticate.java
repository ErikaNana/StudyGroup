package edu.uhmanoa.studybuddies;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

//first you need to get the page so you can get the uuid and post on the same instance

//try using BANSSO and badprotocol cookies, probably not, since tamper data didn't affect it
//try setting the headers --> uPortal-version header
//nope never mind --> figure out what is the difference between a response header and a request header
//try changing the user agent, logging in on wifi, it probably has to do with the headers
public class Authenticate extends Activity implements OnClickListener {
	
	Button mLoginButton;
	String mLoginResponse;
	EditText mUserNameBox;
	EditText mPasswordBox;
	Context mContext;
	ProgressDialog pd;
	String mUserName;
	String mPassword;
	String mCookieValue;
	
	//urls
	public static final String POST_LOGIN_URL = "https://myuh.hawaii.edu/cp/home/login";
	public static final String HOME_LOGIN = "https://myuh.hawaii.edu/cp/home/displaylogin";
	public static final String OK_URL = "http://myuh.hawaii.edu/render.userLayoutRootNode.uP";
	
	//Request header stuff
	public static final String REFERRER = "http://myuh.hawaii.edu/cps/welcome/loginok.html";
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0";
	public static final String ACCEPT_HEADER = "Accept";
	public static final String ACCEPT_HEADER_VALUE = "	text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String ACCEPT_ENCODING_HEADER_VALUE = "gzip, deflate";
	public static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
	public static final String ACCEPT_LANGUAGE_HEADER_VALUE = "en-US,en;q=0.5";
	public static final String CONNECTION_HEADER = "Connection";
	public static final String CONNECTION_HEADER_VALUE = "keep-alive";
	public static final String HOST_HEADER = "Host";
	public static final String HOST_HEADER_VALUE = "myuh.hawaii.edu";
	
	//error codes
	public static final int WRONG_INPUT_ERROR = 1;
	public static final int CONNECTION_ERROR = 2;
	
	Map<String, String> loginCookies = null;
	
	/**Values for data passed into the intent*/
	public static final String COOKIES = "Cookies";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authenticate);
		mLoginButton = (Button) findViewById(R.id.loginButton);
		mLoginButton.setOnClickListener(this);
		
		mUserNameBox = (EditText) findViewById(R.id.inputUserName);
		mPasswordBox = (EditText) findViewById(R.id.inputPassword);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authenticate, menu);
		return true;
	}

	@Override
	public void onClick(View button) {
		if (button.getId() == R.id.loginButton) {

/*			mUserName = mUserNameBox.getText().toString();
			mPassword = mPasswordBox.getText().toString();*/
			mUserName = "enana";
			mPassword = "fUcktbs!1!";
			login();
		}
	}
	
	//establishing the connection
	
	private class connectToWebsite extends AsyncTask <String, Void, String>{

		@Override
		protected String doInBackground(String... urls) {
			Document doc = null;
			Document getDoc = null;
			String getResponse = "";
			String uuid = "";
			
			try {
				//get the page response first
				Connection.Response get = Jsoup.connect(urls[0])
						.method(Method.GET)
						.execute();
				
				getDoc = get.parse();
				getResponse = getDoc.toString();
				
				//get the uuid
				//change this later to regex because this might be slow
				Scanner scanner = new Scanner(getResponse);
				
				scanner.useDelimiter("\n");
				while (scanner.hasNext()) {
					String thing = scanner.next();

					if (thing.contains("document.cplogin.uuid.value")) {
						uuid = thing.substring(33, 69);
						Log.w("uuid", uuid);
						break;
					}
				}
				scanner.close();

				Connection.Response res = Jsoup.connect(urls[1])
						.data("user", mUserName)
						.data("pass", mPassword)
						.data("uuid",uuid)
						.method(Method.POST)
						.execute();

				doc = res.parse();
				loginCookies = res.cookies();
				mLoginResponse = doc.toString();

				//get the cookies
/*				mCookieValue = res.cookie(COOKIE_TYPE);*/
/*				Log.w("cookie", mCookieValue);*/
				
				//do more connecting?
				Connection.Response follow = Jsoup.connect(urls[2])
						.method(Method.GET)
						.cookies(loginCookies)
						.referrer(REFERRER)
						.userAgent(USER_AGENT)
						.header(ACCEPT_HEADER, ACCEPT_HEADER_VALUE)
						.header(ACCEPT_ENCODING, ACCEPT_ENCODING_HEADER_VALUE)
						.header(ACCEPT_LANGUAGE_HEADER,ACCEPT_LANGUAGE_HEADER_VALUE)
						.header(CONNECTION_HEADER, CONNECTION_HEADER_VALUE)
						.header(HOST_HEADER, HOST_HEADER_VALUE)
						.followRedirects(true)
						.ignoreHttpErrors(true)
						.data("uP_root", "root")
						.timeout(5000)
						.execute();
				
				getDoc = follow.parse();
				getResponse = getDoc.toString();
				Log.w("response",getResponse);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mLoginResponse;
		}
        @Override
        protected void onPostExecute(String response) {
	        if (response != null) {          		
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
	        }
        }
    }
	
	public void launchGetClasses() {
        Intent launchGetClasses = new Intent(this,GetClasses.class);
		Set<Entry<String, String>> entries = loginCookies.entrySet();
		
		//store the cookies
		Bundle cookies = new Bundle();
		for (Entry<String, String> entry : entries) {
			cookies.putString(entry.getKey(), entry.getValue());
		}
		launchGetClasses.putExtra(COOKIES, cookies);
		startActivity(launchGetClasses);
	}
	public void login() {
		//this requires min APK of 14 or higher
		pd = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
		pd.setTitle("Connecting..."); //make this a random fact later haha
		pd.setMessage("Please wait.");
		pd.setIndeterminate(true);
		pd.show();
		connectToWebsite connect = new connectToWebsite();
		connect.execute(new String [] {HOME_LOGIN, POST_LOGIN_URL, OK_URL, mUserName, mPassword});
	}
	
	public void showErrorDialog(int typeOfError) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK).setTitle(R.string.app_name);
		pd.dismiss();
		switch(typeOfError) {
			case WRONG_INPUT_ERROR:{
				builder.setMessage("Username and/or password is incorrect.  Please try again!");
				builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// reset both fields
						mUserNameBox.setText("");
						mPasswordBox.setText("");
						//shift focus back to user name field
						mUserNameBox.requestFocus();
						return;
					}
				});
				break;
			}
			case CONNECTION_ERROR:{
				builder.setMessage("Connection failed.  Please check your internet connection.");
				builder.setPositiveButton("Check settings", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
						startActivity(intent);
					}
				});
				builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						pd.dismiss();
						login();
					}
				});
				break;
			}
		}
		AlertDialog dialog = builder.create();
		//so dialog doesn't get closed when touched outside of it
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
}
