package edu.uhmanoa.studybuddies.ui;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
	CoursesDataSource coursesDb;
	ClassmatesDataSource classmatesDb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		Button createGroup = (Button) findViewById(R.id.createGroup);
		Button viewGroups = (Button) findViewById(R.id.viewGroups);
			
		createGroup.setOnClickListener(this);
		viewGroups.setOnClickListener(this);
		setTest();
	}
	
	public void setTest() {
		//for testing
		SharedPreferences prefs = this.getSharedPreferences(Authenticate.USER_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(Authenticate.USER_NAME, "enana").apply();;
		Log.w("stored", prefs.getString(Authenticate.USER_NAME, "BLAH"));
		//courses
		/*CoursesDataSource coursesDb = new CoursesDataSource(this);
		ClassmatesDataSource classmatesDb = new ClassmatesDataSource(this);*/
		coursesDb = new CoursesDataSource(this);
		classmatesDb = new ClassmatesDataSource(this);
		//classmate
		Classmate me = new Classmate("Erika Nana", "enana", "ICS 425");
		Classmate kelsie = new Classmate("Kelsie", "kelsie", "ICS 425");
		Classmate raffi = new Classmate("Raffi", "raffi", "ICS 425");
		//Course
		Course test = new Course("ICS 425");
		test.addTime("9:30-12:30");
		test.addDay("MWF");
		test.addDay("TR");
		test.addTime("12:30-1:30");
		
		Course test2 = new Course("ICS 491");
		test2.addTime("9:30-12:30");
		test2.addDay("MWF");
		test2.addDay("TR");
		test2.addTime("12:30-1:30");
		
		Log.w("course", test.toString());
		coursesDb.open();
		classmatesDb.open();
		
		coursesDb.deleteAll();
		classmatesDb.deleteAll();
		
		coursesDb.addCourse(test);
		coursesDb.addCourse(test2);
		
		classmatesDb.addClassmate(me, "ICS 425");
		classmatesDb.addClassmate(kelsie, "ICS 425");
		classmatesDb.addClassmate(raffi, "ICS 425");
		
		Classmate me2 = new Classmate("Erika Nana", "enana", "ICS 491");
		Classmate kelsie2 = new Classmate("Kelsie", "kelsie", "ICS 491");
		Classmate raffi2 = new Classmate("Raffi", "raffi", "ICS 491");
		classmatesDb.addClassmate(me2, "ICS 491");
		classmatesDb.addClassmate(kelsie2, "ICS 491");
		classmatesDb.addClassmate(raffi2, "ICS 491");
		
/*		coursesDb.close();
		classmatesDb.close();*/
		getScheduleJson();
	}
	public String getScheduleJson() {
		JsonArray courseArray = new JsonArray(); //array of courses
		ArrayList<Course> courses = coursesDb.getAllCourses();
		SharedPreferences prefs = this.getSharedPreferences(Authenticate.USER_NAME, Context.MODE_PRIVATE);
		String userName = prefs.getString(Authenticate.USER_NAME, "nobody");
		
		for (Course course: courses) {
			//create classmate json object
			JsonObject jsonCourse = new JsonObject();
			jsonCourse.addProperty("name", course.getName());
			JsonArray daysArray = new JsonArray();
			ArrayList<String> days = course.getDays();
			ArrayList<String> times = course.getTimes();
			
			for (int i = 0; i < days.size(); i++) {
				JsonObject jsonDays = new JsonObject();
				jsonDays.addProperty("day", days.get(i));
				jsonDays.addProperty("time", times.get(i));
				daysArray.add(jsonDays);
			}

			jsonCourse.add("courseInfo", daysArray);
			//add to array
			courseArray.add(jsonCourse);
		}
				
		JsonObject jsonContainer = new JsonObject();
		jsonContainer.addProperty("user", userName);
		jsonContainer.add("courses", courseArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		
		coursesDb.close();
		//string formatted json
		Log.w("schedule json", gson.toJson(jsonContainer));
		return gson.toJson(jsonContainer);
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
				Intent launchViewGroups = new Intent(this, ViewGroup.class);
				startActivity(launchViewGroups);
				break;
		}
	}
}
