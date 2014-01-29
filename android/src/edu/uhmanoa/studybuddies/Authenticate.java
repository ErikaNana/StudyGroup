package edu.uhmanoa.studybuddies;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
	
	public static final String POST_LOGIN_URL = "https://myuh.hawaii.edu/cp/home/login";
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
			
			try {
				Connection.Response res = Jsoup.connect(urls[0])
						.data("user", mUserName)
						.data("uuid", mPassword)
						.method(Method.POST)
						.execute();
				
				doc = res.parse();
				mLoginResponse = doc.toString();
				
				//get the cookie
				mCookieValue = res.cookie(COOKIE_TYPE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mLoginResponse;
		}
        @Override
        protected void onPostExecute(String response) {
                if (response != null) {
                        pd.dismiss();
                        Log.w("authenticate","YAY!!!");
                }
/*                if (response != null) {
                        if (response.contains("Welcome")) {
                                launchMainStudentMenu();
                        }
                        else {
                                showErrorDialog(WRONG_INPUT_ERROR);
                        }
                }
                else { //wasn't able to connect at all
                        showErrorDialog(CONNECTION_ERROR);
                }
        }*/
        }
    }
	
	public void login() {
		//this requires min APK of 14 or higher
		pd = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
		pd.setTitle("Connectiong..."); //make this a random fact later haha
		pd.setMessage("Please wait.");
		pd.setIndeterminate(true);
		pd.show();
		connectToWebsite connect = new connectToWebsite();
		connect.execute(new String [] {POST_LOGIN_URL, mUserName, mPassword});
	}
}
