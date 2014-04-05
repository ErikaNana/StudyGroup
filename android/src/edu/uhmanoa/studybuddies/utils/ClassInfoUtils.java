package edu.uhmanoa.studybuddies.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uhmanoa.studybuddies.db.Course;
import edu.uhmanoa.studybuddies.ui.GetClasses;

// do try catches with these in activity method later on
public class ClassInfoUtils {
	public static String getCurrentSemesterLink(String response) {
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
	//gets crnAndDeptInfo and classUrls
	public static HashMap<String, String> getDepartmentLinks(String response, HashMap<String,String[]>classInfo, HashMap<String, String> classUrls) {		
		Document doc = Jsoup.parse(response);
		Elements links = doc.getElementsByTag("a");
		
		for (Element link: links) {
			String url = link.attr("href");
			if (url.contains("&") && !(url.contains("frames"))) {
				
				//remove the first dot
				url = url.replaceFirst(".", "");
				Set<String> keys = classInfo.keySet();

				//get urls for depts, and map crns to depts
				for (String key : keys) {
					String[] data = classInfo.get(key);
					String dept = data[GetClasses.SEARCH_NAME];
					if (url.contains("=" + dept)) {
						//get the urls to search
						if (!classUrls.containsKey(key)) {
							classUrls.put(dept, url);
						}
					}
				}
			}
		}
		return classUrls;
	}
	
	public static Course addToClass(ArrayList<Element> array, Course course) {
		int arrayLength = array.size();
		String classTime = array.get(arrayLength - 3).text();
		String days = array.get(arrayLength - 4).text();
		course.addDay(days);
		course.addTime(classTime);
		return course;
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
}
