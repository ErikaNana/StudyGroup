package edu.uhmanoa.studybuddies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClassmateSQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "classmates.db";
	public static final String TABLE_NAME = "classmates";
	public static final String COLUMN_EMAIL = "email"; //this will be the identifier since this is unique
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_CLASS_NAME = "className";
	public static final String COLUMN_PENDING_CREATION = "pendingCreation"; //pending invite
	public static final String COLUMN_CONFIRMED_CREATION = "confirmedCreation"; //this is for listview persistence //this stores integers since there are no booleans
	public static final String COLUMN_JOINED = "joined";
	/*
	 * 			 classmates.db
	 * ------------------------------------------------------------
	 * email |  name |  className  | pending | confirmed | joined
	 * -------------------------------------------------------------
	 */
	
	private static final int DATABASE_VERSION = 4;
	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_EMAIL + " text not null," + COLUMN_NAME + " text not null," + COLUMN_CLASS_NAME + " text not null," + COLUMN_PENDING_CREATION + " int not null,"+ COLUMN_CONFIRMED_CREATION + " int not null," + COLUMN_JOINED + " int not null" + ")";
	
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
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(db);
	}
}
