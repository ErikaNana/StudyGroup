package edu.uhmanoa.studybuddies;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ClassmateAdapter extends ArrayAdapter<Classmate>{
	ArrayList<Classmate> mListOfClassmates;
	Activity mActivity;

	public ClassmateAdapter(Activity activity, int textViewResourceId, ArrayList<Classmate> listOfClassmates) {
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
			if (classmate.isClicked()){
				view.setBackgroundResource(R.drawable.gradient_bg_hover);
			}
			else {
				view.setBackgroundResource(R.drawable.gradient_bg);
			}
		}

		return view;
	}

}
