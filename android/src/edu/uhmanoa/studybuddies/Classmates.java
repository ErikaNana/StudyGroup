package edu.uhmanoa.studybuddies;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Classmates extends Activity {
	ListView mListOfClassmatesListView;
	ArrayList<Classmate> mListOfClassmates;
	ClassmateAdapter mAdapter;
	TextView mNumberOfStudents;
	
	//databases
	private ClassmatesDataSource classmatesDb;
		
	//for the scroll view listener
	int mCurrentVisibleItemCount;
	int mCurrentScrollState;
	int mTotalItemCount;
	int mCurrentFirstVisibleItem;
	int mNumberOfItemsFit;
	Classmate mStudentLookingAt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classmates);
		
		Intent thisIntent = this.getIntent();
		String courseName = thisIntent.getStringExtra("courseName");
		
		//databases
		classmatesDb = new ClassmatesDataSource(this);
		classmatesDb.open();
		
		//views
		mNumberOfStudents = (TextView) findViewById(R.id.studentsToChooseFrom);
		mListOfClassmatesListView = (ListView) findViewById(R.id.listOfStudents);
		mListOfClassmates = new ArrayList<Classmate>();
		
		//set up the listView
		mListOfClassmates = classmatesDb.getClassmates(courseName);
		mAdapter = new ClassmateAdapter(this, R.id.listOfStudents, mListOfClassmates);
		mListOfClassmatesListView.setAdapter(mAdapter);
		
		//set a scroll listener
		mListOfClassmatesListView.setOnScrollListener(new OnScrollListener() {
			
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
		mListOfClassmatesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Toast.makeText(getApplicationContext(), "what", Toast.LENGTH_SHORT).show();
				//mStudentLookingAt = (Classmate) mListOfClassmatesListView.getItemAtPosition(position);
				//change this for later take them to activity with their classmates listed in a listView
			}	
		});
		mNumberOfStudents.setText(mListOfClassmates.size() + " students to choose from");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.classmates, menu);
		return true;
	}

}