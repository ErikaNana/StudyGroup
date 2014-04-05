package edu.uhmanoa.studybuddies.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*This class is responsible for storing and retrieving Course objects*/
/*
 * 			 classinfo
 * -----------------------------
 * className |  days |  times  |
 * -----------------------------
 */
public class CoursesDataSource {

	//Database fields
	private SQLiteDatabase database;
	private ClassSQLiteHelper helper; 
	private String[] allColumns = {ClassSQLiteHelper.COLUMN_CLASS_NAME, ClassSQLiteHelper.COLUMN_DAYS, ClassSQLiteHelper.COLUMN_TIMES};

	public CoursesDataSource(Context context) {
		helper = new ClassSQLiteHelper(context);
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
		database.delete(ClassSQLiteHelper.TABLE_NAME, null, null);
	}
	public void addCourse (Course course) {
		ContentValues values = new ContentValues();
		values.put(ClassSQLiteHelper.COLUMN_CLASS_NAME, course.getName());
		values.put(ClassSQLiteHelper.COLUMN_DAYS, course.getStringOfDays());
		values.put(ClassSQLiteHelper.COLUMN_TIMES, course.getStringOfTimes());
		
		database.insert(ClassSQLiteHelper.TABLE_NAME, null, values);
	}
	
	public ArrayList<Course> getAllCourses(){
		ArrayList<Course> courses = new ArrayList<Course>();
		Cursor cursor = database.query(ClassSQLiteHelper.TABLE_NAME, allColumns, null, null, null, null, null);
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
	
	public Course getCourse(String name) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + ClassSQLiteHelper.TABLE_NAME +  " WHERE " + ClassSQLiteHelper.COLUMN_CLASS_NAME + " = ?", new String[] {name});
		
		if (c != null) {
			c.moveToFirst(); //cursor should only return one thing anyway
		}
		
		//get info out of the db
		int nameIndex = c.getColumnIndex(ClassSQLiteHelper.COLUMN_CLASS_NAME);
		int daysIndex = c.getColumnIndex(ClassSQLiteHelper.COLUMN_DAYS);
		int timesIndex = c.getColumnIndex(ClassSQLiteHelper.COLUMN_TIMES);
		
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
		String name = course.getName().replace(" ", "_");
		database.delete(ClassSQLiteHelper.TABLE_NAME, ClassSQLiteHelper.COLUMN_CLASS_NAME + " = " + name, null);
	}
}
