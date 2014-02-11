package edu.uhmanoa.studybuddies;

import java.util.HashMap;

public class Course {

	HashMap<String,String> data;
	String className;
	String time;
	
	public Course(String className) {
		this.className = className;
	}
		
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getName() {
		return className;
	}
	
}
