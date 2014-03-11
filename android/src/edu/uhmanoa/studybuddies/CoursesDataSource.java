package edu.uhmanoa.studybuddies;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*This class is responsible for storing and retrieving Course objects*/
public class CoursesDataSource {

	//Database fields
	private SQLiteDatabase database;
	private SQLiteHelper helper; 
	private String[] allColumns = {SQLiteHelper.COLUMN_CLASS_NAME, SQLiteHelper.COLUMN_DAYS, SQLiteHelper.COLUMN_TIMES};

	public CoursesDataSource(Context context) {
		helper = new SQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = helper.getWritableDatabase();
	}
	
	public void close() {
		helper.close();
	}
	
	public void deleteAll() {
		database = helper.getWritableDatabase();
		//null so delete all the rows
		database.delete(SQLiteHelper.TABLE_NAME, null, null);
	}
	public void addCourse (Course course) {
		ContentValues values = new ContentValues();
		//figure out what to do with column_id? Or maybe you don't need it
		values.put(SQLiteHelper.COLUMN_CLASS_NAME, course.getName());
		values.put(SQLiteHelper.COLUMN_DAYS, course.getStringOfDays());
		values.put(SQLiteHelper.COLUMN_TIMES, course.getStringOfTimes());
		
		database.insert(SQLiteHelper.TABLE_NAME, null, values);
	}
	
	public ArrayList<Course> getAllCourses(){
		ArrayList<Course> courses = new ArrayList<Course>();
		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			//create a course object from each row in the table
			String name = cursor.getString(0);
			String days = cursor.getString(1);
			String times = cursor.getString(2);
			Course course = new Course(name);
			course.setFullTime(times);
			course.setFullDays(days);
			courses.add(course);
			cursor.moveToNext();
		}
		cursor.close();
		return courses;
	}
	
	//maybe change this so can get course by name? (names are unique anyway, so maybe don't need autoincrement thing
	public Course getCourse(String name) {
		//see if this is the right thing to call
		SQLiteDatabase db = helper.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + SQLiteHelper.TABLE_NAME + "WHERE " + SQLiteHelper.COLUMN_CLASS_NAME + " = " + name;
		Log.w("CoursesDataSource", selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c != null) {
			c.moveToFirst(); //cursor should only return one thing anyway
		}
		
		//get info out of the db
		int nameIndex = c.getColumnIndex(SQLiteHelper.COLUMN_CLASS_NAME);
		int daysIndex = c.getColumnIndex(SQLiteHelper.COLUMN_DAYS);
		int timesIndex = c.getColumnIndex(SQLiteHelper.COLUMN_TIMES);
		
		String courseName = c.getString(nameIndex);
		String days = c.getString(daysIndex);
		String times = c.getString(timesIndex);
		
		//create a course to return
		Course course = new Course();
		course.setName(courseName);
		course.setFullDays(days);
		course.setFullTime(times);
		
		Log.w("getCourse", "print:  " + course);
		return course;
	}
	
	public void deleteCourse(Course course) {
		String name = course.getName();
		database.delete(SQLiteHelper.TABLE_NAME, SQLiteHelper.COLUMN_CLASS_NAME + " = " + name, null);
	}
}
