package edu.uhmanoa.studybuddies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;


public class GetClasses extends Activity {
	public static final String ROOT_URL = "https://www.sis.hawaii.edu/uhdad/avail.classes?i=MAN";
	public static final String COOKIE = "JSESSIONID";
	public static final int CRN = 0;
	public static final int URL = 1;
	public static final int SEARCH_NAME = 2;
	
	//to connect to different urls
	public static final int CONNECT_CURRENT_SEMESTER = 3;
	public static final int CONNECT_DEPARTMENTS = 4;
	public static final int GET_TIME_FROM_DEPARTMENT = 5;
	public static final int GET_MAIN_CLASS_PAGE = 6;
	public static final int GET_MAIL_TOOL = 7;
	public static final int GET_ROLES_SECTIONS_GROUPS = 8;
	public static final int GET_JUST_ROLES = 9;
	public static final int GET_STUDENTS_PAGE = 10;
	public static final int GET_STUDENTS = 11;
	
	//for dialogs
	public static final int GET_CLASS_INFO = 12;
	public static final int GET_ROSTERS = 13;
	
	//get class availability
	String BASE_URL = "https://www.sis.hawaii.edu/uhdad";
	
	String mCookieValue = "";
	String mLoginResponse = "";
	
	//key = className, value = data (crn, url, searchName)
	//key = EE 496, value = [80823,url,EE]
	//maybe should make the key the crn, and name where the crn is
	HashMap<String,String[]>classInfo;
	
	//key searchName, value = url to search
	HashMap<String, String> classUrls;
	
	//key = courseName, course object
	HashMap<String, Course> courses;
	
	//arraylist of crns
	ArrayList<String> crns;
	
	//keep track if done hitting dept urls
	int counter = 0;
	
	ProgressDialog pd;
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
	
	//database
	private CoursesDataSource coursesDb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		classInfo = new HashMap<String,String[]>();
		classUrls = new HashMap<String, String>();
		courses = new HashMap<String,Course>();
		crns = new ArrayList<String>();
		
		//database
		coursesDb = new CoursesDataSource(this);
		//delete any existing info
		coursesDb.deleteAll();
		coursesDb.open();
		
		//views
		setContentView(R.layout.get_classes);
		mNumberOfClasses = (TextView) findViewById(R.id.numberOfClasses);
		mListOfClassesListView = (ListView) findViewById(R.id.listOfClasses);
		mListOfClasses = new ArrayList<Course>();
		
		Intent thisIntent = this.getIntent();
		//get cookies 
		mCookieValue = thisIntent.getStringExtra(Authenticate.COOKIE_TYPE);

		//get classInfo
		createDialog(GET_CLASS_INFO);
		mLoginResponse = thisIntent.getStringExtra(Authenticate.LOGIN_RESPONSE);
		getClassAndCRN(mLoginResponse);
		
		Connect getSemester = new Connect(CONNECT_CURRENT_SEMESTER, GET_CLASS_INFO);
		getSemester.execute(new String [] {ROOT_URL});
		
		//set the adapter
		mAdapter = new CourseAdapter(this, R.id.listOfClasses, mListOfClasses);
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
				launchGetDescription(mCourseLookingAt);
			}	
		});
	}
	
	private class Connect extends AsyncTask <String, Void, String>{
		
		int which = 0;
		int type = 0;
		
		public Connect(int which, int type) {
			this.which = which;
			this.type = type;
		}
		
		@Override
		protected String doInBackground(String... urls) {
			Document resDoc = null;
			String response = "";
			
			try {
				//get the cookie from previous authentication and hit each url in classInfo 
				//get the rosters
				//update classinfo hashmap
				if (type == GET_CLASS_INFO) {
					//Connect to the page with all of the classes
					Connection.Response res = Jsoup.connect(urls[0])
							.method(Method.GET)
							.execute();
					resDoc = res.parse();
					response = resDoc.toString();	
				}
				else { 
					resDoc = Jsoup.connect(urls[0])
							.cookie(Authenticate.COOKIE_TYPE, mCookieValue)
							.get();

					response = resDoc.toString();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return response;
		}
        @Override
        protected void onPostExecute(String response) {
			switch(which) {
				case CONNECT_CURRENT_SEMESTER:
					
					String link = ClassInfoUtils.getCurrentSemesterLink(response);
		        	link = BASE_URL + link;
		        	
		        	//connect to the pages with departments
		        	Connect getSemester = new Connect(CONNECT_DEPARTMENTS, GET_CLASS_INFO);
		    		getSemester.execute(new String [] {link});
		    		
					break;
				case CONNECT_DEPARTMENTS:
					classUrls = ClassInfoUtils.getDepartmentLinks(response, classInfo, classUrls);
					
					//go to individual department pages and get time
					Set<String> classes = classUrls.keySet();
					
					//initialize classes, if there are times then they will be overidden
					Log.w("crn length", crns.size() + "");
					for (String courseName: classInfo.keySet()) {
						Course course = new Course(courseName);
						courses.put(courseName, course);
					}
					for (String className:  classes) {
						Connect getTimes = new Connect(GET_TIME_FROM_DEPARTMENT, GET_CLASS_INFO);
						getTimes.execute(new String [] {BASE_URL + classUrls.get(className)});
					}
					Log.w("donezo", "donezo");
					break;
				case GET_TIME_FROM_DEPARTMENT: //gets the classes for each department
					getDayAndTime(response);
					counter++;
					//set up activity to display the classes
					Log.w("counter","counter:  " + counter);
					if (counter == classUrls.keySet().size()) {
						Log.w("donezo", "we're finished");
						displayClasses();
						counter = 0;
						pd.dismiss();
						getRosters();
					}
					break;
/*					public static final int GET_ROLES_SECTIONS_GROUPS = 8;
					public static final int GET_JUST_ROLES = 9;
					public static final int GET_STUDENTS = 10*/
				case GET_MAIN_CLASS_PAGE:
					Log.w("GET_MAIN", "main");
					String mailURL = RosterUtils.getMailToolURL(response);
					Connect getRolesSectionGroupsPage = new Connect(GET_ROLES_SECTIONS_GROUPS, GET_ROSTERS);
					getRolesSectionGroupsPage.execute(new String [] {mailURL});
					break;
				
				case GET_ROLES_SECTIONS_GROUPS:
					Log.w("GET_sections", "sections");
					String getRolesSectionGroupsPageURL = RosterUtils.getRolesSectionGroupsPage(response);
					Connect getRoles = new Connect(GET_JUST_ROLES, GET_ROSTERS);
					getRoles.execute(new String [] {getRolesSectionGroupsPageURL});
					break;
					
				case GET_JUST_ROLES:
					Log.w("GET_Roles", "roles");
					String getStudentRoleURL = RosterUtils.getStudentRole(response);
					Connect getStudentRole = new Connect(GET_STUDENTS_PAGE, GET_ROSTERS);
					getStudentRole.execute(new String [] {getStudentRoleURL});
					break;
				
				case GET_STUDENTS_PAGE:
					Log.w("GET_student page", "student page");
					String getStudentsURL = RosterUtils.getStudentsPage(response);
					Connect getStudentsPage = new Connect(GET_STUDENTS, GET_ROSTERS);
					getStudentsPage.execute(new String [] {getStudentsURL});
					break;
					
				case GET_STUDENTS:
					Log.w("response", response);
					pd.dismiss();
					//get list of students
					//pass that to formatRosters
			}
        }
    }
	private void launchGetDescription(Course mCourseLookingAt) {
		Toast.makeText(getBaseContext(), mCourseLookingAt.toString(), Toast.LENGTH_SHORT).show();
		
	}
	//this method gets classmate objects and puts them in the database
	public void updateWithRosters() {
		
	}
	public void getRosters() {
		createDialog(GET_ROSTERS);
		//just get the first one for now
		String[] testData = classInfo.get("EE 496");
		String url = testData[URL];
		
		Connect overallPage = new Connect(GET_MAIN_CLASS_PAGE, GET_ROSTERS);
		overallPage.execute(new String [] {url});
		//get the next relevant url from the response and keep going until you get the students
	}
	public void createDialog(int type) {
		pd = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
		switch(type) {
			case GET_CLASS_INFO:
				pd.setTitle("Gathering your class data..."); //make this a random fact later haha
				pd.setMessage("This might take a while");
				break;
			case GET_ROSTERS:
				pd.setTitle("Getting your classmates...");
				pd.setMessage("Can't have a group without people right?");
				break;
		}
		pd.setIndeterminate(true);
		pd.show();
	}
	public void displayClasses() {
		//Set the text for number of classes found
		mNumberOfClasses.setText("Click a class to get started");
		
		//put the classes in the database
		for (String courseName: courses.keySet()) {
			Course course = courses.get(courseName);
			coursesDb.addCourse(course);
		}
		
		//for some reason can't just straight up assign it
		ArrayList<Course> classes = coursesDb.getAllCourses();
		for (Course course: classes) {
			mListOfClasses.add(course);
		}
		
		coursesDb.close();
		//update the view
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_classes, menu);
		return true;
	}
	
	//maybe this should return a hashmap of crn to time
	public void getDayAndTime(String response) {
		try {
			//use the searchName and the CRNS to find the times
			//h1 tag has the searchName
			
			Document doc = Jsoup.parse(response);
			Elements rows = doc.select("tr");
			ArrayList<Element> rowsArray = ClassInfoUtils.convertToArray(rows);
			
			//trim the fat off the arrays
			rowsArray.remove(0);
			int lengthOfRows = rowsArray.size();
			System.out.println(lengthOfRows);
			for (int i = lengthOfRows - 17; i < lengthOfRows; i++) {
				rowsArray.remove(rowsArray.size()-1); //always remove the last element
			}
			Course course = null;

			for (Element row: rowsArray) {
				Elements columns = row.select("td");
				
				//convert the columns to array for easier trimming
				ArrayList<Element> columnsArray = ClassInfoUtils.convertToArray(columns);
				int size = columnsArray.size();
				System.out.println("size of columns:  " + size);
				if (size == 30) { //for some reason this is needed
					break;
				}
				System.out.println("-----------------");
				System.out.println("another check:  " + size);
				//deal with comments in the page
				if (size < 2) {
					continue;
				}
				Element firstElement = columnsArray.get(0);
				Element secondElement = columnsArray.get(1);
				String firstElementText = firstElement.text();
				String secondElementText = secondElement.text();
				
				//find crns in the first column or the second column
				Pattern p = Pattern.compile("[0-9]{5}");
				Matcher crnMatcherFirst = p.matcher(firstElementText);
				Matcher crnMatcherSecond = p.matcher(secondElementText);
				String crn = "";
				String className = "";
				if (crnMatcherFirst.find()) {
					crn = crnMatcherFirst.group(0);
					className = columnsArray.get(1).text();
				}
				else if (crnMatcherSecond.find()) {
					crn = crnMatcherSecond.group(0);
					className = columnsArray.get(2).text();
				}
				if (!crn.equals("")) {
					//start of a new class
					course = new Course(className);
				}
				//this also handles multiple days
				course = ClassInfoUtils.addToClass(columnsArray,course);
				if (crns.contains(crn)) {
					course.addCRN(crn);
					courses.put(className,course);
				}
			}
		}
		catch (Exception e) {
			// show an error dialog
			e.printStackTrace();
		}
	}		
	
	//get the classAndCRN
	public void getClassAndCRN(String response) {
		Document doc = Jsoup.parse(response);
		Elements rows = doc.select("div.termContainer");
		System.out.println("rows size:  " + rows.size());
		//always get the the most current one
		Element currentClasses = rows.get(rows.size()-1);
		Elements classLinks = currentClasses.select("a");
		for (Element link: classLinks) {
			String url = link.attr("abs:href");
			if (url.contains("#")) {
				continue;
			}
			String text = link.attr("title");
			
			Pattern classNamePattern = Pattern.compile("[MAN ]*[A-Z]+-[0-9]+");
			Pattern crn = Pattern.compile("\\.[0-9]+");
			Pattern searchClassPattern = Pattern.compile("[A-Z]+");
			
			String className = "";
			String crnMatch = "";
			
			//Maybe restructure this part later on so don't repeat code
			Matcher matcher = classNamePattern.matcher(text);
			if (matcher.find()) {
				className = matcher.group(0);
				//strip - from class name
				className = className.replace("-", " "); 
				matcher = crn.matcher(text);
				
				if (matcher.find()) {
					crnMatch = matcher.group(0);
					
					//strip . from crn
					crnMatch = crnMatch.replace(".", "");
					
					matcher = searchClassPattern.matcher(className);
					if (matcher.find()) {
						String searchName = matcher.group(0);
						String [] data = {crnMatch,url,searchName};
						crns.add(crnMatch);
						classInfo.put(className, data);
					}	
				}
				else { //no CRN, might be online class
					if (className.contains("MAN ")) {
						className = className.replace("MAN ", "");
						matcher = searchClassPattern.matcher(className);
						if (matcher.find()) {
							String searchName = matcher.group(0);
							String [] data = {"None", url, searchName};
							classInfo.put(className, data);
						}
					}
				}
			}
		}
	}
}
