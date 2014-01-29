package edu.uhmanoa.studybuddies;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Authenticate extends Activity {

	public static final String POST_LOGIN_URL = "https://myuh.hawaii.edu/cp/home/displaylogin";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authenticate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authenticate, menu);
		return true;
	}

}
