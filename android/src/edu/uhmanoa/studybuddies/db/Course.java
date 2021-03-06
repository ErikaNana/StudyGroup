package edu.uhmanoa.studybuddies.db;

import java.util.ArrayList;
import java.util.Collections;

//arrayLists always maintain the order
public class Course {
	private ArrayList<String> times;
	private ArrayList<String> days;
	private String crn;
	private String name;
	private String fullTime;
	private String fullDays;
	
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
	
	public void setName (String name) {
		this.name = name;
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
		return "Name:  " + name + " Times:  " + getStringOfTimes() + " Days:  " + getStringOfDays() + "\n";
	}
	
	//idk what these two methods would be for...just for testing attribute assignment?
	public String getStringOfTimes() {
		String finalTime = "";
		for (String time: times) {
			finalTime = finalTime + time + " ";
		}
		if (finalTime.equals("")) {
			return "None";
		}
		fullTime = finalTime;
		return fullTime;
	}
	
	public String getStringOfDays() {
		String finalDays = "";
		for (String day: days) {
			finalDays = finalDays + day + " ";
		}
		if (finalDays.equals("")) {
			return "None";
		}
		fullDays = finalDays;
		return fullDays;
	}
	
	//for recreating
	public void setTimes(String fullTime) {
		//delimit by space and recreate times
		String[] timeArray = fullTime.split(" ");
		times = new ArrayList<String>();
		Collections.addAll(times, timeArray);
		this.fullTime = fullTime;
	}
	
	public void setDays(String fullDays) {
		//delimit by space and recreate times
		String[] daysArray = fullDays.split(" ");
		days = new ArrayList<String>();
		Collections.addAll(days, daysArray);
		this.fullDays = fullDays;
	}
}
