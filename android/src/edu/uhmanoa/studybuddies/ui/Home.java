package edu.uhmanoa.studybuddies.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.uhmanoa.studybuddies.R;
import edu.uhmanoa.studybuddies.db.Classmate;
import edu.uhmanoa.studybuddies.db.ClassmatesDataSource;
import edu.uhmanoa.studybuddies.db.Course;
import edu.uhmanoa.studybuddies.db.CoursesDataSource;

public class Home extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		Button createGroup = (Button) findViewById(R.id.createGroup);
		Button viewGroups = (Button) findViewById(R.id.viewGroups);
		
		//for testing
		setTest();
			
		createGroup.setOnClickListener(this);
		viewGroups.setOnClickListener(this);
		
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
