package edu.uhmanoa.studybuddies.ui;

import edu.uhmanoa.studybuddies.R;
import edu.uhmanoa.studybuddies.R.layout;
import edu.uhmanoa.studybuddies.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ViewGroupClass extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classmates_for_group);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_group_class, menu);
		return true;
	}

}
