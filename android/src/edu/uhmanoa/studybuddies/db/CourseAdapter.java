package edu.uhmanoa.studybuddies.db;

import java.util.ArrayList;

import edu.uhmanoa.studybuddies.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CourseAdapter extends ArrayAdapter<Course>{
	ArrayList<Course> mListOfCourses;
	Activity mActivity;

	public CourseAdapter(Activity activity, int textViewResourceId, ArrayList<Course> listOfCourses) {
        super(activity, textViewResourceId, listOfCourses);
        mActivity = activity;
        mListOfCourses = listOfCourses;
    }
	/**Makes ListView more efficient since Android recycles views in a ListView
	 * Code might call findViewById() frequently during the scrolling of ListView, which
	 * can slow performance. 
	 * ViewHolder stores each of the component views inside the tag field of the layout,
	 * so can immediately access them without the need to look them up repeatedly*/
	public static class ViewHolder{
		TextView courseTitle;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.single_class_row, null);
			holder = new ViewHolder();
			holder.courseTitle = (TextView) view.findViewById(R.id.courseTitle);
			view.setTag(holder);
		}
		else {
			//returns the object stored in this view as a tag
			holder = (ViewHolder) view.getTag();
		}
		final Course course = mListOfCourses.get(position);
		if (course != null) {
			//set course title
			holder.courseTitle.setText(course.getName());
		}
		return view;
	}

}
