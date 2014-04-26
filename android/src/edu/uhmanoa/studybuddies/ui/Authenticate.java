package edu.uhmanoa.studybuddies.ui;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.uhmanoa.studybuddies.R;
import edu.uhmanoa.studybuddies.db.Classmate;
import edu.uhmanoa.studybuddies.db.ClassmatesDataSource;
import edu.uhmanoa.studybuddies.db.Course;
import edu.uhmanoa.studybuddies.db.CoursesDataSource;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
	
	//username
	public static final String USER_NAME = "userName";
	
	//urls;
	public static final String LAULIMA_LOGIN = "https://laulima.hawaii.edu/portal/xlogin";

	
	//error codes
	public static final int WRONG_INPUT_ERROR = 1;
	public static final int CONNECTION_ERROR = 2;

	//passing to GetClasses activity

	/**Values for data passed into the intent*/
	public static final String LOGIN_RESPONSE = "loginResponse";
	public static final String COOKIE_TYPE = "JSESSIONID";
	
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
/*			mUserName = "enana";*/
			mPassword = "fUcktbs!1!";
			mUserName = mUserNameBox.getText().toString();
			login();
		}
	}
	
	public void finishActivity() {
		finish();
	}
	
	//establishing the connection
	
	private class connectToWebsite extends AsyncTask <String, Void, String>{

		@Override
		protected String doInBackground(String... urls) {
			Document doc = null;
			
			try {
				Connection.Response res = Jsoup.connect(urls[0])
						.data("eid", mUserName)
						.timeout(3000)
						.data("pw", mPassword)
						.data("submit","Login")
						.method(Method.POST)
						.execute();

				doc = res.parse();
				
				//get the cookie
				mCookieValue = res.cookie(COOKIE_TYPE);
				mLoginResponse = doc.toString();
/*				Log.w("response", doc.toString());*/
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mLoginResponse;
		}
        @Override
        protected void onPostExecute(String response) {
	        if (response != null) {          		
	        	if (response.contains("My Workspace")) {
	                    pd.dismiss();
/*	                    Log.w("authenticate","YAY!!!");*/
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
	
	public void setTest() {
		//for testing
		SharedPreferences prefs = this.getSharedPreferences(Authenticate.USER_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(Authenticate.USER_NAME, "enana").apply();;
		Log.w("stored", prefs.getString(Authenticate.USER_NAME, "BLAH"));
		//courses
		CoursesDataSource coursesDb = new CoursesDataSource(this);
		ClassmatesDataSource classmatesDb = new ClassmatesDataSource(this);
		
		//classmate
		Classmate me = new Classmate("Erika Nana", "enana", "ICS 425");
		Classmate kelsie = new Classmate("Kelsie", "kelsie", "ICS 425");
		Classmate raffi = new Classmate("Raffi", "raffi", "ICS 425");
		//Course
		ArrayList<String> times = new ArrayList<String>();
		times.add("9:30-12:30");
		ArrayList<String> days = new ArrayList<String>();
		days.add("MWF");
		Course test = new Course("ICS 425");
		
		coursesDb.open();
		classmatesDb.open();
		
		coursesDb.deleteAll();
		classmatesDb.deleteAll();
		
		coursesDb.addCourse(test);
		classmatesDb.addClassmate(me, "ICS 425");
		classmatesDb.addClassmate(kelsie, "ICS 425");
		classmatesDb.addClassmate(raffi, "ICS 425");
		
		coursesDb.close();
		classmatesDb.close();
	}
	public void launchGetClasses() {
		//store the user name in preferences for later reference
        
        SharedPreferences prefs = this.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(USER_NAME, mUserName);
		
        Intent launchGetClasses = new Intent(this,GetClasses.class);
        
        //pass on the cookie
        launchGetClasses.putExtra(COOKIE_TYPE, mCookieValue);
		
		//store the response
		launchGetClasses.putExtra(LOGIN_RESPONSE, mLoginResponse);
		
		//for testing
		setTest();
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
		connect.execute(new String [] {LAULIMA_LOGIN});
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
						finishActivity();
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

