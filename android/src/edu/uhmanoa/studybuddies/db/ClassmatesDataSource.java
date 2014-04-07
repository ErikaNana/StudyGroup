package edu.uhmanoa.studybuddies.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/*This class is responsible for storing and retrieving Classmate objects*/
/*
 * 			 classmates.db
 * -----------------------------------------------
 * email |  name |  className  | group membership
 * -----------------------------------------------
 */
public class ClassmatesDataSource {

	//Database fields
	private SQLiteDatabase database;
	private ClassmateSQLiteHelper helper;
/*	private String [] allColumns = {ClassmateSQLiteHelper.COLUMN_EMAIL, ClassmateSQLiteHelper.COLUMN_NAME, ClassmateSQLiteHelper.COLUMN_CLASS_NAME, ClassmateSQLiteHelper.COLUMN_GROUP_MEMBERSHIP.toString()};*/
	
	public static int PENDING_CREATION = 1;
	public static int NOT_PENDING_CREATION = 0;
	public static int CONFIRMED_CREATION = 1;
	public static int NOT_CONFIRMED_CREATION = 0;
	public ClassmatesDataSource(Context context) {
		helper = new ClassmateSQLiteHelper(context);
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
		database.delete(ClassmateSQLiteHelper.TABLE_NAME, null, null);
	}
	
	public void addClassmate(Classmate classmate, String className) {
		ContentValues values = new ContentValues();
		values.put(ClassmateSQLiteHelper.COLUMN_EMAIL, classmate.getEmail());
		values.put(ClassmateSQLiteHelper.COLUMN_NAME, classmate.getName());
		values.put(ClassmateSQLiteHelper.COLUMN_CLASS_NAME, className);
		values.put(ClassmateSQLiteHelper.COLUMN_PENDING_CREATION, classmate.isPendingCreation); //default to not in group
		values.put(ClassmateSQLiteHelper.COLUMN_CONFIRMED_CREATION, classmate.isConfirmedCreation()); //default to not in group
		
		database.insert(ClassmateSQLiteHelper.TABLE_NAME, null, values);
	}
	
	//updates the group membership of classmate
	//need to test if this works
	public void updatePending(Classmate classmate, int pending) {
		//go to the row with that classmate
		//update the field
		SQLiteDatabase db = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		if (pending == PENDING_CREATION) {
			values.put(ClassmateSQLiteHelper.COLUMN_PENDING_CREATION, 1);
		}
		if (pending == NOT_PENDING_CREATION) {
			values.put(ClassmateSQLiteHelper.COLUMN_PENDING_CREATION, 0);
		}
				
		db.update(ClassmateSQLiteHelper.TABLE_NAME, values, ClassmateSQLiteHelper.COLUMN_EMAIL + " = ?", new String[] {classmate.email});
	}
	
	public void updateConfirmed(Classmate classmate, int confirmed) {
		//go to the row with that classmate
		SQLiteDatabase db = helper.getReadableDatabase();
		ContentValues values = new ContentValues();

		if (confirmed == CONFIRMED_CREATION) {
			values.put(ClassmateSQLiteHelper.COLUMN_CONFIRMED_CREATION, 1);
		}
		if (confirmed == NOT_CONFIRMED_CREATION) {
			values.put(ClassmateSQLiteHelper.COLUMN_CONFIRMED_CREATION, 0);
		}
		db.update(ClassmateSQLiteHelper.TABLE_NAME, values, ClassmateSQLiteHelper.COLUMN_EMAIL + " = ?", new String[] {classmate.email});
	}
	
	//gets a classmate given an email
	//THIS NEEDS TO BE FIXED
	public Classmate getClassmate(String getEmail) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + ClassmateSQLiteHelper.TABLE_NAME + "WHERE " + ClassmateSQLiteHelper.COLUMN_EMAIL + " = " + getEmail;
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c!= null) {
			c.moveToFirst();
		}
		
		//get info out of db
		int nameIndex = c.getColumnIndex(ClassmateSQLiteHelper.COLUMN_NAME);
		int emailIndex = c.getColumnIndex(ClassmateSQLiteHelper.COLUMN_EMAIL);
		int classNameIndex = c.getColumnIndex(ClassmateSQLiteHelper.COLUMN_CLASS_NAME);
		int pendingIndex = c.getColumnIndex(ClassmateSQLiteHelper.COLUMN_PENDING_CREATION);
		int confirmedIndex = c.getColumnIndex(ClassmateSQLiteHelper.COLUMN_CONFIRMED_CREATION);
		
		String name = c.getString(nameIndex);
		String email = c.getString(emailIndex);
		String className = c.getString(classNameIndex);
		int pending = c.getInt(pendingIndex);
		int confirmed = c.getInt(confirmedIndex);
		
		Classmate classmate = new Classmate(name, email, className, pending, confirmed);
		return classmate;
	}
	
	//gets all the classmates for a particular class
	public ArrayList<Classmate> getClassmates(String className){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + ClassmateSQLiteHelper.TABLE_NAME +  " WHERE " + ClassmateSQLiteHelper.COLUMN_CLASS_NAME + " = ?", new String[] {className});
		cursor.moveToFirst();
		ArrayList<Classmate> retrieved = new ArrayList<Classmate>();
		while(!cursor.isAfterLast()) {
			//create classMate from each row in cursor
			String name = cursor.getString(1);
			String email = cursor.getString(0);
			String courseName = cursor.getString(2);
			int pending = cursor.getInt(3);
			int confirmed = cursor.getInt(4);
			
			Classmate classmate = new Classmate(name, email,courseName, pending, confirmed);
			retrieved.add(classmate);
			cursor.moveToNext();
		}
		cursor.close();
		return retrieved;
	}
}
