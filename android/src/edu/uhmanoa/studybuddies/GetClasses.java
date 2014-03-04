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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

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
	
	//get class availability
	String BASE_URL = "https://www.sis.hawaii.edu/uhdad";
	
	String mCookieValue = "";
	String mLoginResponse = "";
	
	//key = className, value = data (crn, url, searchName)
	//key = EE 496, value = [80823,url,EE]
	HashMap<String,String[]>classInfo;
	
	//key searchName, value = url to search
	HashMap<String, String> classUrls;
	
	//key = searchName, value = crn's to search
	HashMap<String, ArrayList<String>> crnAndDeptInfo;
	
	HashMap<String, Course> courses;
	
	//the department that we're currently getting CRNs from
	String currentDept;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		classInfo = new HashMap<String,String[]>();
		classUrls = new HashMap<String, String>();
		crnAndDeptInfo = new HashMap<String, ArrayList<String>>();
		courses = new HashMap<String,Course>();
	
		Intent thisIntent = this.getIntent();
		//get cookies 
		mCookieValue = thisIntent.getStringExtra(Authenticate.COOKIE_TYPE);

		//get classInfo
		mLoginResponse = thisIntent.getStringExtra(Authenticate.LOGIN_RESPONSE);
		getClassAndCRN(mLoginResponse);
		
		Connect getSemester = new Connect(CONNECT_CURRENT_SEMESTER);
		getSemester.execute(new String [] {ROOT_URL});
		
	}
	
	private class Connect extends AsyncTask <String, Void, String>{
		
		int which = 0;
		
		public Connect(int which) {
			this.which = which;
		}
		
		@Override
		protected String doInBackground(String... urls) {
			Document resDoc = null;
			String response = "";
			
			try {				
				//Connect to the page with all of the classes
				Connection.Response res = Jsoup.connect(urls[0])
						.method(Method.GET)
						.execute();
				resDoc = res.parse();
				response = resDoc.toString();
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
					
					String link = getCurrentSemesterLink(response);
		        	link = BASE_URL + link;
		        	
		        	//connect to the pages with departments
		        	Connect getSemester = new Connect(CONNECT_DEPARTMENTS);
		    		getSemester.execute(new String [] {link});
		    		
					break;
				case CONNECT_DEPARTMENTS:
/*					Log.w("response", response);*/
					getDepartmentLinks(response);
					
					//go to individual department pages and get time
					Set<String> classes = classUrls.keySet();
					Log.w("length of classes", classes.size() + "");
					for (String className:  classes) {
/*						Connect getTimes = new Connect(GET_TIME_FROM_DEPARTMENT);*/
						Log.w("className", className);
/*						currentDept = className;
						getTimes.execute(new String [] {BASE_URL + classUrls.get(className)});*/
					}
					break;
				case GET_TIME_FROM_DEPARTMENT:
					getDayAndTime(response);
/*					Log.w("response", response);*/
					break;
			}
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
		ArrayList<String> crns = crnAndDeptInfo.get(currentDept);
/*		Log.w("current dept", currentDept);
		
		for (String crn: crns) {
			Log.w("crn", crn);
		}*/
		//use the searchName and the CRNS to find the times
		//h1 tag has the searchName
		//seperate between even and odd and then iterate
		
		Document doc = Jsoup.parse(response);
		Elements rows = doc.select("tr");
		ArrayList<Element> rowsArray = convertToArray(rows);
		
		//trim the fat off the arrays
		rowsArray.remove(0);
		int lengthOfRows = rowsArray.size();
		System.out.println(lengthOfRows);
		for (int i = lengthOfRows - 17; i < lengthOfRows; i++) {
			rowsArray.remove(rowsArray.size()-1); //always remove the last element
		}
		Course course = null;
		HashMap<String,Course> courses = new HashMap<String,Course>();

		for (Element row: rowsArray) {
			Elements columns = row.select("td");
			
			//convert the columns to array for easier trimming
			ArrayList<Element> columnsArray = convertToArray(columns);
			int size = columnsArray.size();
			System.out.println("size of columns:  " + size);
			if (size == 30) { //for some reason this is needed
				break;
			}
			System.out.println("-----------------");
			System.out.println("another check:  " + size);
			Element firstElement = columnsArray.get(0);
			Element secondElement = columnsArray.get(1);
			String firstElementText = firstElement.text();
			String secondElementText = secondElement.text();
			
			//find crns in the first column or the second column
			Pattern p = Pattern.compile("[0-9]{5}");
			Matcher crnMatcherFirst = p.matcher(firstElementText);
			Matcher crnMatcherSecond = p.matcher(secondElementText);
			String crn = "";
			
			if (crnMatcherFirst.find()) {
				crn = crnMatcherFirst.group(0);
			}
			else if (crnMatcherSecond.find()) {
				crn = crnMatcherSecond.group(0);
			}
			
			if (!crn.equals("")) {
				System.out.println("crn:  " + crn);
				//start of a new class
				course = new Course(crn);
			}
			course = addToClass(columnsArray,course);
			if (crns.contains(crn)) {
				courses.put(crn,course);
			}

			System.out.println("---------");
		}
/*		Log.w("here", "i'm here");*/
/*		Log.w("course keys", courses.keySet().size() + "");*/
/*		for (String key: courses.keySet()) {
			Log.w("course key", key);
			Log.w("getDayAndTime","key:  " + key + "\n" + courses.get(key));
		}*/
	}
	//gets crnAndDeptInfo and classUrls
	public void getDepartmentLinks(String response) {		
		Log.w("response", response);
		Document doc = Jsoup.parse(response);
		Elements links = doc.getElementsByTag("a");
		
		for (Element link: links) {
			String url = link.attr("href");
			if (url.contains("&") && !(url.contains("frames"))) {
				
				//remove the first dot
				url = url.replaceFirst(".", "");
/*				Log.w("url", url);*/
				Set<String> keys = classInfo.keySet();

				//get urls for depts, and map crns to depts
				for (String key : keys) {
/*					Log.w("keys in deptlinks", key);*/
					String[] data = classInfo.get(key);
					String dept = data[SEARCH_NAME];
/*					Log.w("dept", dept);*/
					if (url.contains("=" + dept)) {
						//add to hashmap
						if (dept.equals("EE")) {
							Log.w("Here","IT'S HERE");
						}
						ArrayList<String> crn = new ArrayList<String>();
						//check if key already exists
						if (crnAndDeptInfo.containsKey(dept)) {
							crn = crnAndDeptInfo.get(dept);
							crn.add(data[CRN]);
						}
						else {
							crn.add(data[CRN]);
						}
						crnAndDeptInfo.put(dept, crn);
						
						//get the urls to search
						if (!classUrls.containsKey(key)) {
							classUrls.put(dept, url);
						}
/*						Log.w("url",url);*/
					}
				}
			}
		}
		
		Set<String> checkKeys = classUrls.keySet();
		for (String key: checkKeys) {
			Log.w("key url","key:  " + key);
			Log.w("value url","value:  " + classUrls.get(key));
		}
/*		Log.w("classDepts", classDepts.size() + "");*/
	}
	public static ArrayList<Element> convertToArray(Elements elements) {
		ArrayList<Element> elementsArray = new ArrayList<Element>();
		for (Element element: elements) {
			//get rid of nbsp
			if (!element.text().equals("\u00a0")) {
				elementsArray.add(element);
			}
		}
		return elementsArray;
	}
	
	public static Course addToClass(ArrayList<Element> array, Course course) {
		int arrayLength = array.size();
		String classTime = array.get(arrayLength - 3).text();
		String days = array.get(arrayLength - 4).text();
		course.addDay(days);
		course.addTime(classTime);
		return course;
	}
	
	public String getCurrentSemesterLink(String response) {
		Document doc = Jsoup.parse(response);
		Elements links = doc.getElementsByTag("a");
		Element link = links.get(6);
		
		//check if it is summer
		if (link.text().contains("Summer")) {
			link = links.get(7);
		}
		String url = link.attr("HREF");
		
		//remove the dot
		url = url.replaceFirst(".", "");
		return url;
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
			
			Matcher matcher = classNamePattern.matcher(text);
			if (matcher.find()) {
				className = matcher.group(0);
/*				Log.w("Class Name"," name:  " + className);
				Log.w("title", text);*/
				matcher = crn.matcher(text);
				
				if (matcher.find()) {
					crnMatch = matcher.group(0);
					
					//strip . from crn
					crnMatch = crnMatch.replace(".", "");
					
					//strip - from class name
					className = className.replace("-", " "); 
					
					matcher = searchClassPattern.matcher(className);
					if (matcher.find()) {
						String searchName = matcher.group(0);
						String [] data = {crnMatch,url,searchName};
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
		/*Log.w("classInfo", classInfo.toString());*/
/*		for (String key: classInfo.keySet()) {
			Log.w("classInfo", "key:  " + key);
			String[] elements = classInfo.get(key);
			for (String element: elements) {
				Log.w("classInfo", element);
			}
		}*/
		Log.w("classInfo", "classInfo size:  " + classInfo.size());
	}
}
