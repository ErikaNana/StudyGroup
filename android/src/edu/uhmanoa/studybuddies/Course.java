package edu.uhmanoa.studybuddies;

import java.util.ArrayList;

//arrayLists always maintain the order
public class Course {
	ArrayList<String> times;
	ArrayList<String> days;
	String course;
	
	public Course(String course) {
		this.course = course;
		times = new ArrayList<String>();
		days = new ArrayList<String>();
	}
	
	public Course() {
		times = new ArrayList<String>();
		days = new ArrayList<String>();	
	}
	
	public void addDay(String day) {
		days.add(day);
	}
	
	public void addTime(String time) {
		times.add(time);
	}
	
	public ArrayList<String> getTimes() {
		return times;
	}
	
	public ArrayList<String> getDays(){
		return days;
	}
	
	public String toString() {
		return "CRN:  " + course + " Times:  " + getStringOfTimes() + " Days:  " + getStringOfDays() + "\n";
	}
	
	public String getStringOfTimes() {
		String finalTime = "";
		for (String time: times) {
			finalTime = finalTime + time + " ";
		}
		return finalTime;
	}
	
	public String getStringOfDays() {
		String finalDays = "";
		for (String day: days) {
			finalDays = finalDays + day + " ";
		}
		return finalDays;
	}
}

