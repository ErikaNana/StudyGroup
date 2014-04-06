package edu.uhmanoa.studybuddies.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

import edu.uhmanoa.studybuddies.R;
import edu.uhmanoa.studybuddies.db.Classmate;
import edu.uhmanoa.studybuddies.db.ClassmateAdapter;
import edu.uhmanoa.studybuddies.db.ClassmatesDataSource;
import edu.uhmanoa.studybuddies.utils.JsonUtils;

public class Classmates extends Activity{
	ListView mListOfClassmatesListView;
	ArrayList<Classmate> mListOfClassmates;
	ClassmateAdapter mAdapter;
	TextView mNumberOfStudents;
	Button createGroup;
	
	//databases
	private ClassmatesDataSource classmatesDb;
		
	//for the scroll view listener
	int mCurrentVisibleItemCount;
	int mCurrentScrollState;
	int mTotalItemCount;
	int mCurrentFirstVisibleItem;
	int mNumberOfItemsFit;
	Classmate mStudentLookingAt;
	ArrayList<Classmate> clicked;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_group);
		
		Intent thisIntent = this.getIntent();
		String courseName = thisIntent.getStringExtra("courseName");
		
		//initialize
		clicked = new ArrayList<Classmate>();
		createGroup = (Button) findViewById(R.id.createGroupWithClassmates);
		
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
				Classmate student = (Classmate) parent.getAdapter().getItem(position);
				if (clicked.contains(student)) {
					clicked.remove(student);
					student.setMembership(false);
					//refresh the view to reflect changes
					mAdapter.notifyDataSetChanged();
				}
				else {
					clicked.add(student);
					view.setBackgroundResource(R.drawable.gradient_bg_hover);
					student.setMembership(true);
					mAdapter.notifyDataSetChanged();
					
					
				}
			}	
		});
		mNumberOfStudents.setText(mListOfClassmates.size() + " students to choose from");
		
		//set up createGroups button
		createGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//just to be safe
				if (v.getId() == createGroup.getId()) {
					Log.w("clicked",clicked.toString());
					//format and push data
					String json = JsonUtils.getJson(clicked);
					postParams(json);
					Log.w("json", json);
				}
			}
		});
	}
	
	public void postParams(String json) {
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://study-group-creator.herokuapp.com/create";
		Context context = this.getApplicationContext();
		try {
			StringEntity entity = new StringEntity(json);
			client.post(context, url, entity, "application/json", new ResponseHandlerInterface() {
				
				@Override
				public void setUseSynchronousMode(boolean arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void setRequestURI(URI arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void setRequestHeaders(Header[] arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void sendSuccessMessage(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void sendStartMessage() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void sendRetryMessage() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void sendResponseMessage(HttpResponse response) throws IOException {
					HttpEntity entity = response.getEntity();
					printThis(EntityUtils.toString(entity));
				}
				@Override
				public void sendProgressMessage(int arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void sendFinishMessage() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void sendFailureMessage(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public URI getRequestURI() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Header[] getRequestHeaders() {
					// TODO Auto-generated method stub
					return null;
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void printThis(String stuff) {
		Log.w("finished", stuff);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.classmates, menu);
		return true;
	}
}
