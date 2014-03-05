package edu.uhmanoa.studybuddies;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/*This class is responsible for storing and retrieving Course objects*/
public class CoursesDataSource {

	//Database fields
	private SQLiteDatabase database;
	private SQLiteHelper helper; 
	private String[] allColumns = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_CLASS_NAME, SQLiteHelper.COLUMN_DAYS, SQLiteHelper.COLUMN_TIMES};
	
	public CoursesDataSource(Context context) {
		helper = new SQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = helper.getWritableDatabase();
	}
	
	public void close() {
		helper.close();
	}
}
