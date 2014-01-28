package edu.uhmanoa.studybuddies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class Initial extends Activity {
	
	public static final String FIRST_USE = "firstUse";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initial);
		
		TextView authenticate = (TextView) findViewById(R.id.authenticate);
		SharedPreferences prefs = this.getSharedPreferences(FIRST_USE, Context.MODE_PRIVATE);
		
		//use a default value if there is none
		boolean firstUse = prefs.getBoolean(FIRST_USE, true);
		
		if (firstUse) {
			authenticate.setText("I'm a first time user!");
			Intent launchAuthenticate = new Intent(this,Authenticate.class);
			startActivity(launchAuthenticate);
		}
		else {
			authenticate.setText("I've been here before");
			Intent launchHome = new Intent(this, Home.class);
			startActivity(launchHome);
			
		}
		//check to see if this the user's first time in the app
/*		TextView authenticate = (TextView) findViewById(R.id.authenticate);
		authenticate.setText("woof");*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authenticate, menu);
		return true;
	}

}
