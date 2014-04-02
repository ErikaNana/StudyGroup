package edu.uhmanoa.studybuddies;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class Home extends Activity {
	TextView mNumberOfClasses;
	ListView mListOfClassesListView;
	ArrayList<Course> mListOfClasses;
	CourseAdapter mAdapter;
	
	//for the scroll view listener
	int mCurrentVisibleItemCount;
	int mCurrentScrollState;
	int mTotalItemCount;
	int mCurrentFirstVisibleItem;
	int mNumberOfItemsFit;
	Course mCourseLookingAt;
	
	//databases
	private CoursesDataSource coursesDb;
	private ClassmatesDataSource classmatesDb;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		//databases
		coursesDb = new CoursesDataSource(this);
		coursesDb.open();
		
		//views
		mNumberOfClasses = (TextView) findViewById(R.id.numberOfClassesHome);
		mListOfClassesListView = (ListView) findViewById(R.id.listOfClassesHome);
		mListOfClasses = new ArrayList<Course>();
		
		//set up the listView
		mListOfClasses = coursesDb.getAllCourses();
		mAdapter = new CourseAdapter(this, R.id.listOfClassesHome, mListOfClasses);
		mListOfClassesListView.setAdapter(mAdapter);
		
		//set a scroll listener
		mListOfClassesListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mCurrentScrollState = scrollState;
				checkScrollCompleted();
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mNumberOfItemsFit = visibleItemCount;
				mCurrentFirstVisibleItem = firstVisibleItem;
				mTotalItemCount = totalItemCount;
				
			}
			public void checkScrollCompleted() {
				if (mCurrentFirstVisibleItem ==(mTotalItemCount - mNumberOfItemsFit)) {
					if (mCurrentScrollState == SCROLL_STATE_IDLE) {
						//we're at the bottom
						return;
					}
				}
			}
		});
		//listen for a click
		mListOfClassesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				mCourseLookingAt = (Course) mListOfClassesListView.getItemAtPosition(position);
				//change this for later take them to activity with their classmates listed in a listView
				launchStudentsView(mCourseLookingAt);
			}	
		});
		
		//Set the text for number of classes found
		mNumberOfClasses.setText("Click a class to get started");
	}

	public void launchStudentsView(Course course) {
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
