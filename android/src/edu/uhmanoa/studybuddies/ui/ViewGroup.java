package edu.uhmanoa.studybuddies.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import edu.uhmanoa.studybuddies.R;
import edu.uhmanoa.studybuddies.db.ClassmatesDataSource;

public class ViewGroup extends Activity {
	//databases
	private ClassmatesDataSource classmatesDb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		classmatesDb = new ClassmatesDataSource(this);
		classmatesDb.open();
		
		ArrayList<String> pendingConfirmed = new ArrayList<String>();
		pendingConfirmed = classmatesDb.getPendingConfirmed();
		
		if (pendingConfirmed.size() == 0) {
			setContentView(R.layout.no_group_view);
			TextView textBox = (TextView) findViewById(R.id.noGroups);
			textBox.setText("No groups yet...What're you waiting for?");
		}
		else {
			setContentView(R.layout.group_classes);
		}
		Log.w("pending", pendingConfirmed.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_group, menu);
		return true;
	}

}
