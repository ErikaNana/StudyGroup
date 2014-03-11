import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sun.io.Converters;

public class Test_Class_Time_Day_Grab{
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String output = "";
		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader("ics.html")));
			s.useDelimiter("(\n)");
			while (s.hasNext()) {
				output = output + s.next();
			}
			
			//For testing
			ArrayList<String>crns = new ArrayList<String>();
			crns.add("85248");
			crns.add("85251");

			Document doc = Jsoup.parse(output);
			
			//seperate between even and odd and then iterate
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
			for (String key: courses.keySet()) {
				System.out.println("key:  " + key + "\n" + courses.get(key));
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (s != null) {
				s.close();
			}
		}
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
}
