package edu.uhmanoa.studybuddies.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

import edu.uhmanoa.studybuddies.R;
import edu.uhmanoa.studybuddies.db.Classmate;
import edu.uhmanoa.studybuddies.db.ClassmateAdapterSelection;
import edu.uhmanoa.studybuddies.db.ClassmatesDataSource;
import edu.uhmanoa.studybuddies.utils.JsonUtils;

public class Classmates extends Activity{
	ListView mListOfClassmatesListView;
	ArrayList<Classmate> mListOfClassmates;
	ClassmateAdapterSelection mAdapter;
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
	
	//json params
	ArrayList<Classmate> clicked;
	String userName;
	String courseName;
	ProgressDialog pd;
	
	SharedPreferences prefs;
	public static final String CREATED_GROUPS = "created";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_group);
		
		Intent thisIntent = this.getIntent();
		courseName = thisIntent.getStringExtra("courseName");
		Log.w("courseName", courseName);
	
		//initialize
		clicked = new ArrayList<Classmate>();
		createGroup = (Button) findViewById(R.id.createGroupWithClassmates);
		
		prefs = this.getSharedPreferences(Authenticate.USER_NAME, Context.MODE_PRIVATE);
		userName = prefs.getString(Authenticate.USER_NAME, "nothing");
		Log.w("userName", userName);
		//databases
		classmatesDb = new ClassmatesDataSource(this);
		classmatesDb.open();
		
		//views
		mNumberOfStudents = (TextView) findViewById(R.id.studentsToChooseFrom);
		mListOfClassmatesListView = (ListView) findViewById(R.id.listOfStudents);
		mListOfClassmates = new ArrayList<Classmate>();
		
		//set up the listView
		mListOfClassmates = classmatesDb.getClassmates(courseName);
		mAdapter = new ClassmateAdapterSelection(this, R.id.listOfStudents, mListOfClassmates);
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
				makeToast(student);
				if (!student.isConfirmedCreation()) {
					if (clicked.contains(student)) {
						clicked.remove(student);
						student.setPendingCreation(false);
						classmatesDb.updatePending(student, ClassmatesDataSource.NOT_PENDING_CREATION);
						//refresh the view to reflect changes
						mAdapter.notifyDataSetChanged();
					}
					else {
						clicked.add(student);
						view.setBackgroundResource(R.drawable.gradient_bg_hover);
						student.setPendingCreation(true);
						classmatesDb.updatePending(student, ClassmatesDataSource.PENDING_CREATION);
						mAdapter.notifyDataSetChanged();	
					}
				}
			}	
		});
		
		//set up createGroups button
		createGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//just to be safe
				if (v.getId() == createGroup.getId()) {
					Log.w("clicked",clicked.toString());
					//confirm students
					for (Classmate classmate: clicked) {
						classmate.setConfirmedCreation(true);
						classmatesDb.updateConfirmed(classmate, ClassmatesDataSource.CONFIRMED_CREATION);
						//refresh the view
						mAdapter.notifyDataSetChanged();
					}
					//format and push data
					String json = JsonUtils.getJson(clicked, userName, courseName);
					showPostingDialog();
					postParams(json);
					launchInvited();
/*					Log.w("json", json);*/
					
				}
			}
		});
		
		updateStudentCount();
	}
	
	public void launchInvited() {
		Log.w("no launch", "no launch");
		//go to view groups
		Intent intent = new Intent(this, ViewGroupClass.class);
		intent.putExtra("className", courseName);
		startActivity(intent);
		
	}
	public void makeToast(Classmate classmate) {
		Classmate found = classmatesDb.getClassmate(classmate);
		Toast.makeText(getApplicationContext(), "student:  " + found.email, Toast.LENGTH_SHORT).show();
	}
	public void updateStudentCount() {
		//only show available students
		int counter = 0;
		for (Classmate classmate: mListOfClassmates) {
			if (!classmate.isConfirmedCreation()) {
				counter++;
			}
		}
		String student = "student";
		if (counter != 1) {
			student = "students";
		}
		
		mNumberOfStudents.setText(counter + " " + student + " to choose from");
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
				}
				
				@Override
				public void sendRetryMessage() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void sendResponseMessage(HttpResponse response) throws IOException {
					HttpEntity entity = response.getEntity();
					String responseString = EntityUtils.toString(entity);
/*					printThis(responseString);*/
				}
				@Override
				public void sendProgressMessage(int arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void sendFinishMessage() {
					updateGroups(courseName);
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
	
	public void showPostingDialog() {
		pd = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
		pd.setTitle("Creating your group...");
		pd.setMessage("Random fact is random");
		pd.show();
	}
	
	public void updateGroups(String courseName) {
		//update createGroups 
		Log.w("in update", "updateGroups");
		Set<String> createdGroups = prefs.getStringSet(CREATED_GROUPS, null);
		if (createdGroups == null) {
			ArrayList<String> group = new ArrayList<String>();
			group.add(courseName);
			Set<String> set = new HashSet<String>();
			set.addAll(group);
			prefs.edit().putStringSet(CREATED_GROUPS, set);
		}
		else {
			//do something here later if deleting groups or adding more people or whatever
		}
		//update the view
		pd.dismiss();
		updateStudentCount();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.classmates, menu);
		return true;
	}
}
