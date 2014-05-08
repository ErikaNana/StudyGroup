package edu.uhmanoa.studybuddies.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import edu.uhmanoa.studybuddies.R;

public class Initial extends Activity {
	
	public static final String FIRST_USE = "firstUse";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initial);
		
		TextView authenticate = (TextView) findViewById(R.id.authenticate);
		SharedPreferences prefs = this.getSharedPreferences(FIRST_USE, Context.MODE_PRIVATE);
		//for debugging
		prefs.edit().putBoolean(Initial.FIRST_USE, true).apply();
/*		prefs.edit().putBoolean(Initial.FIRST_USE, false).apply();
		prefs.edit().putString(Authenticate.USER_NAME, "enana").apply();*/
		//use a default value if there is none
		boolean firstUse = prefs.getBoolean(FIRST_USE, true);
		
		Log.w("Initial","first use:  " + firstUse);
		//debugging
		prefs.edit().putBoolean(Initial.FIRST_USE, false).apply();
		
		//check to see if this is the user's first time in the app
		if (firstUse) {
			authenticate.setText("I'm a first time user!");
			//update prefs
			prefs.edit().putBoolean(Initial.FIRST_USE, false).apply();
			Intent launchAuthenticate = new Intent(this,Authenticate.class);
			startActivity(launchAuthenticate);
			//don't want to be accessed by back button
			finish();
		}
		else {
			authenticate.setText("I've been here before");
			Intent launchHome = new Intent(this, Home.class);
			startActivity(launchHome);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authenticate, menu);
		return true;
	}

}
