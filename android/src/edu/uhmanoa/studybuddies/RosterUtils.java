package edu.uhmanoa.studybuddies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * This class is responsible for parsing responses 
 * to get the corresponding urls for roster grabbing
 * 
 */

public class RosterUtils {
	
	//parse https://laulima.hawaii.edu/portal/site/MAN.86621.201430
	public static String getMailToolURL(String response) {
		Document doc = Jsoup.parse(response);
		Elements rows = doc.select("a.icon-sakai-mailtool");
		Element link = rows.get(0);
		System.out.println("size:  " + rows.size());
		String url = link.attr("HREF");
		return url;
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
/*	public static ArrayList<>*/
}
