package edu.uhmanoa.studybuddies.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import edu.uhmanoa.studybuddies.R;
import edu.uhmanoa.studybuddies.db.Classmate;
import edu.uhmanoa.studybuddies.db.ClassmateAdapterDefault;
import edu.uhmanoa.studybuddies.db.ClassmatesDataSource;

public class ViewGroupClass extends Activity {
	ListView mListOfClassmatesListView;
	ClassmateAdapterDefault mAdapter;
	
	//for the scroll view listener
	int mCurrentVisibleItemCount;
	int mCurrentScrollState;
	int mTotalItemCount;
	int mCurrentFirstVisibleItem;
	int mNumberOfItemsFit;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classmates_for_group);
		
		Intent thisIntent = this.getIntent();
		String courseName = thisIntent.getStringExtra("className");
		
		ClassmatesDataSource classmatesDb = new ClassmatesDataSource(this);
		classmatesDb.open();
		
		ArrayList<Classmate> invited = classmatesDb.getInvited(courseName);
		
		//views
		TextView stats = (TextView) findViewById(R.id.studentStats);
		mListOfClassmatesListView = (ListView) findViewById(R.id.listOfInvited);
		
		stats.setText(invited.size() + " students invited/confirmed");
		//set up the listView
		mAdapter = new ClassmateAdapterDefault(this, R.id.listOfStudents,invited);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_group_class, menu);
		return true;
	}

}
