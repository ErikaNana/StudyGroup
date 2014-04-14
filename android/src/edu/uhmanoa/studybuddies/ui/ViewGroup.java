package edu.uhmanoa.studybuddies.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import edu.uhmanoa.studybuddies.R;
import edu.uhmanoa.studybuddies.db.ClassmatesDataSource;

public class ViewGroup extends Activity {
	//databases
	private ClassmatesDataSource classmatesDb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_group);
		
		classmatesDb = new ClassmatesDataSource(this);
		classmatesDb.open();
		
		ArrayList<String> pendingConfirmed = new ArrayList<String>();
		pendingConfirmed = classmatesDb.getPendingConfirmed();
		Log.w("pending", pendingConfirmed.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_group, menu);
		return true;
	}

}
