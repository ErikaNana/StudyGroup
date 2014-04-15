package edu.uhmanoa.studybuddies.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import edu.uhmanoa.studybuddies.R;
import edu.uhmanoa.studybuddies.db.ClassmatesDataSource;
import edu.uhmanoa.studybuddies.db.Course;
import edu.uhmanoa.studybuddies.db.CourseAdapter;
import edu.uhmanoa.studybuddies.db.CoursesDataSource;

public class ViewGroup extends Activity {
	//databases
	private ClassmatesDataSource classmatesDb;
	TextView mNumberOfClasses;
	ListView mListOfClassesListView;

	CourseAdapter mAdapter;
	
	//for the scroll view listener
	int mCurrentVisibleItemCount;
	int mCurrentScrollState;
	int mTotalItemCount;
	int mCurrentFirstVisibleItem;
	int mNumberOfItemsFit;
	Course mCourseLookingAt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		classmatesDb = new ClassmatesDataSource(this);
		CoursesDataSource coursesDb = new CoursesDataSource(this);
		classmatesDb.open();
		coursesDb.open();
		
		ArrayList<String> pendingConfirmed = new ArrayList<String>();
		pendingConfirmed = classmatesDb.getPendingConfirmed();
		ArrayList<Course> pendingConfirmedClasses = new ArrayList<Course>();
		
		for (String pending: pendingConfirmed) {
			Course course = coursesDb.getCourse(pending);
			pendingConfirmedClasses.add(course);
		}
		
		if (pendingConfirmed.size() == 0) {
			setContentView(R.layout.no_group_view);
			TextView textBox = (TextView) findViewById(R.id.noGroups);
			textBox.setText("No groups yet...What're you waiting for?");
		}
		else {
			setContentView(R.layout.group_classes);
			//views
			mNumberOfClasses = (TextView) findViewById(R.id.numberOfClassesGroup);
			mListOfClassesListView = (ListView) findViewById(R.id.listOfClassesGroup);

			
			//set up the listView
			mAdapter = new CourseAdapter(this, R.id.listOfClassesHome, pendingConfirmedClasses);
			mListOfClassesListView.setAdapter(mAdapter);
			//update the view
			if (mAdapter != null) {
				mAdapter.notifyDataSetChanged();
			}
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
					
				}	
			});
			
			//Set the text for number of classes found
			mNumberOfClasses.setText("Your Groups");
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
