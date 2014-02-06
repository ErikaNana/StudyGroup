package edu.uhmanoa.studybuddies;

import java.io.IOException;
import java.util.Scanner;

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
		
	//error codes
	public static final int WRONG_INPUT_ERROR = 1;
	public static final int CONNECTION_ERROR = 2;
	
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
				mLoginResponse = doc.toString();
				
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
		connect.execute(new String [] {HOME_LOGIN, POST_LOGIN_URL, mUserName, mPassword});
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
