package edu.uhmanoa.studybuddies;

import java.util.ArrayList;

//arrayLists always maintain the order
public class Course {
	ArrayList<String> times;
	ArrayList<String> days;
	String crn;
	String name;
	public Course(String name) {
		this.name = name;
		times = new ArrayList<String>();
		days = new ArrayList<String>();
		crn = "None";
	}
	
	public Course() {
		times = new ArrayList<String>();
		days = new ArrayList<String>();	
		name = "";
		crn = "None";
	}
	
	public void addCRN(String crn) {
		this.crn = crn;
	}
	public void addDay(String day) {
		days.add(day);
	}
	
	public void addTime(String time) {
		times.add(time);
	}
	
	public String getName() {
		return name;
	}
	public ArrayList<String> getTimes() {
		return times;
	}
	
	public ArrayList<String> getDays(){
		return days;
	}
	
	public String toString() {
		return "CRN:  " + crn + " Times:  " + getStringOfTimes() + " Days:  " + getStringOfDays() + "\n";
	}
	
	public String getStringOfTimes() {
		String finalTime = "";
		for (String time: times) {
			finalTime = finalTime + time + " ";
		}
		if (finalTime.equals("")) {
			return "None";
		}
		return finalTime;
	}
	
	public String getStringOfDays() {
		String finalDays = "";
		for (String day: days) {
			finalDays = finalDays + day + " ";
		}
		if (finalDays.equals("")) {
			return "None";
		}
		return finalDays;
	}
}
