package edu.uhmanoa.studybuddies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "classinfo.db";
	public static final String TABLE_NAME = "classinfo";
/*	public static final String COLUMN_ID = "_id";*/
	public static final String COLUMN_DAYS = "days";
	public static final String COLUMN_TIMES = "times";
	public static final String COLUMN_CLASS_NAME = "className";
	
	/*
	 * 			   classinfo
	 * ------------------------------------
	 * id  |  className |  days |  times  |
	 * -------------------------------------
	 */
	
	//Fix this because maybe you don't need the id afterall
	private static final int DATABASE_VERSION = 1;
	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table" + TABLE_NAME + "(" + COLUMN_CLASS_NAME + " text not null," + COLUMN_DAYS + " text not null," + COLUMN_TIMES + " text not null" + ")";
	
	public SQLiteHelper(Context context) {
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
