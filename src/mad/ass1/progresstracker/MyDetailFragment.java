package mad.ass1.progresstracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MyDetailFragment extends Fragment {
	private TextView moduleCode;
	private TextView assignmentName;
	private TextView marksProportion;
	private TextView whenDue;
	private TextView progress;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.detail_layout, null);
		return v;
	}

	public void updateDetails(int value) {
		if (value < 0) {
//			moduleCode.setText("");
//			assignmentName.setText("");
//			marksProportion.setText("");
//			whenDue.setText("");
//			progress.setText("");
			
			return;
		}
		try{
		
		Assignment a = MyDBHelper.DBHelper.getAssignmentByID(value);
		
		moduleCode = (TextView) getActivity().findViewById(R.id.module_code);
		moduleCode.setText(a.getModuleCode());
		assignmentName = (TextView) getActivity().findViewById(R.id.assignment_name);
		assignmentName.setText(a.getAssignmentName());
		marksProportion = (TextView) getActivity().findViewById(R.id.marks_proportion);
		marksProportion.setText(a.getMarksProportion()+"");
		whenDue = (TextView) getActivity().findViewById(R.id.when_due);
		whenDue.setText(a.getWhenDue());
		progress = (TextView) getActivity().findViewById(R.id.progress);
		progress.setText(a.getProgress()+"");
		}catch(Exception e ){
			Toast.makeText(getActivity(), "Ding DIng"+e.toString(), Toast.LENGTH_LONG)
					.show();
			Log.d("DEBUG", "value = "+value+"="+e.toString());
		}
	}
}
