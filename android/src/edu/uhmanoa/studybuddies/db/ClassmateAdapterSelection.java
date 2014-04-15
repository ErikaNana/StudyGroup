package edu.uhmanoa.studybuddies.db;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.uhmanoa.studybuddies.R;

public class ClassmateAdapterSelection extends ArrayAdapter<Classmate>{
	ArrayList<Classmate> mListOfClassmates;
	Activity mActivity;

	public ClassmateAdapterSelection(Activity activity, int textViewResourceId, ArrayList<Classmate> listOfClassmates) {
        super(activity, textViewResourceId, listOfClassmates);
        mActivity = activity;
        mListOfClassmates = listOfClassmates;
    }
	/**Makes ListView more efficient since Android recycles views in a ListView
	 * Code might call findViewById() frequently during the scrolling of ListView, which
	 * can slow performance. 
	 * ViewHolder stores each of the component views inside the tag field of the layout,
	 * so can immediately access them without the need to look them up repeatedly*/
	public static class ViewHolder{
		public  TextView studentName;
	}
	@Override
	//this is what refreshes the views
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.single_classmate_row, null);
			holder = new ViewHolder();
			holder.studentName = (TextView) view.findViewById(R.id.studentName);
			view.setTag(holder);
		}
		else {
			//returns the object stored in this view as a tag
			holder = (ViewHolder) view.getTag();
		}
		final Classmate classmate = mListOfClassmates.get(position);
		if (classmate != null) {
			//set course title
			holder.studentName.setText(classmate.getName());
			//keep the ones that are selected, selected
			//pending as in pending group formation
			if (classmate.isPendingCreation()){
				view.setBackgroundResource(R.drawable.gradient_bg_hover);
			}
			else {
				view.setBackgroundResource(R.drawable.gradient_bg);
			}
			
			//if confirmed, change it and can't be clickable
			if (classmate.isConfirmedCreation()) {
				holder.studentName.setTextColor(Color.LTGRAY);
				view.setEnabled(false);
				view.setBackgroundResource(R.drawable.gradient_bg);
			}
		}

		return view;
	}

}
