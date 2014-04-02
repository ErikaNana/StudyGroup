package edu.uhmanoa.studybuddies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClassmateSQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "classmates.db";
	public static final String TABLE_NAME = "classmates";
	public static final String COLUMN_EMAIL = "email"; //this will be the identifier since this is unique
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_CLASS_NAME = "className";
	public static final String COLUMN_GROUP_MEMBERSHIP = "groupMembership"; //this stores integers since there are no booleans
	
	/*
	 * 			 classmates.db
	 * -----------------------------------------------
	 * email |  name |  className  | group membership
	 * -----------------------------------------------
	 */
	
	private static final int DATABASE_VERSION = 1;
	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_EMAIL + " text not null," + COLUMN_NAME + " text not null," + COLUMN_CLASS_NAME + " text not null," + COLUMN_GROUP_MEMBERSHIP + " int not null" + ")";
	
	public ClassmateSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(db);
	}
}
