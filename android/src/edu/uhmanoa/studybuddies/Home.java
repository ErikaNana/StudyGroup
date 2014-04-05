package edu.uhmanoa.studybuddies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		Button createGroup = (Button) findViewById(R.id.createGroup);
		Button viewGroups = (Button) findViewById(R.id.viewGroups);
		createGroup.setOnClickListener(this);
		viewGroups.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id) {
			case R.id.createGroup:
				Intent launchCreateGroup = new Intent(this, CreateGroup.class);
				startActivity(launchCreateGroup);
				break;
			case R.id.viewGroups:
				/*Intent launchCreateGroup = new Intent(this, Home.class);*/
				break;
		}
	}
}
