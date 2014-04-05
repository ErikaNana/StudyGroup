package edu.uhmanoa.studybuddies.utils;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uhmanoa.studybuddies.db.Classmate;

/*
 * This class is responsible for parsing responses 
 * to get the corresponding urls for roster grabbing
 * 
 */

//do a check at each one
public class RosterUtils {
	private static String currentClass;
	
	//parse https://laulima.hawaii.edu/portal/site/MAN.86621.201430
	public static String getMailToolURL(String response) {
		Document doc = Jsoup.parse(response);
		Elements rows = doc.select("a.icon-sakai-mailtool");
		Element link = rows.get(0);
		try {
			System.out.println("size:  " + rows.size());
			String url = link.attr("HREF");
			
			//get the class
			String title = doc.select("title").text();
			//parse
			//remove Laulima
			title = title.substring(10);
			//remove MAN if it exists
			title = title.replace("MAN", "");
			title = title.trim();
			String[] split = title.split(" ");
			title = split[0];
			//remove section number if there is
			if(title.split("-").length > 2) {
				title = title.substring(0,title.length()-4);
			}
			//format so can store and retrieve properly
			currentClass = title.replace("-", " ");
			return url;
		}
		catch(Exception e) {
			return "";
		}
	}
	
	//get https://laulima.hawaii.edu/portal/tool/ab30e251-47d1-4453-a461-37f3cb3ed53f?panel=Main
	public static String getRolesSectionGroupsPage(String response) {
		Document doc = Jsoup.parse(response);
		Elements rows = doc.select("div.title > a");
		System.out.println(rows);
		Element link = rows.first();
		String url = link.attr("HREF");
		return url;
	}
	
	//get https://laulima.hawaii.edu/portal/tool/ab30e251-47d1-4453-a461-37f3cb3ed53f/userGroup?type=role
	public static String getStudentRole(String response) {
		Document doc = Jsoup.parse(response);
		Elements rows = doc.select("div.rcpt_select > a");
		Element link = rows.first();
		String url = link.attr("HREF");
		return url;
	}
	
	//get https://laulima.hawaii.edu/portal/tool/ab30e251-47d1-4453-a461-37f3cb3ed53f/users?id=Student&amp;type=role
	public static String getStudentsPage(String response) {
		Document doc = Jsoup.parse(response);
		Elements rows = doc.select("div.userGroupsList > a");
		String url = "";
		for (Element row: rows) {
			if (row.attr("href").contains("Student")){
				url = row.attr("href");
				break;
			}
		}
		return url;
	}
	
	//returns a list of Classmate objects
	//need to account for two first names ex. Jin Hao
	public static ArrayList<Classmate> getClassmates(String response){
		ArrayList<Classmate> classmates = new ArrayList<Classmate>();
		Document doc = Jsoup.parse(response);
		Elements rows = doc.getElementsByTag("label");
		
		for (Element row: rows) {
			String rawName = row.text();
			String[] elements = rawName.split(",");
			String lastName = elements[0].trim();
			String firstAndEmail = elements[1];
			String[] splitFirstAndEmail = firstAndEmail.split("\\(");
			String firstName = splitFirstAndEmail[0].trim();
			String email = splitFirstAndEmail[1].replace(")", "");
			Classmate classmate = new Classmate(firstName + " " + lastName, email, currentClass);
			classmates.add(classmate);
		}
		return classmates;
	}
	
	public static String getCurrentClass() {
		return currentClass;
	}
}
