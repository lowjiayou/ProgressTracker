package mad.ass1.progresstracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MyListFragment extends ListFragment {
	private MainActivity parent;
	public static ArrayList<Assignment> assignments;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list_layout, null);
		parent = (MainActivity) getActivity();
		
		ArrayList<Map<String, String>> list = buildData();
		String[] from = { "name", "purpose" };
		int[] to = { android.R.id.text1, android.R.id.text2 };

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
				android.R.layout.simple_list_item_2, from, to) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView tv = (TextView) view.findViewById(android.R.id.text1);
				tv.setTextColor(Color.parseColor("#FFFFFF"));
				tv = (TextView) view.findViewById(android.R.id.text2);
				tv.setTextColor(Color.parseColor("#C0C0C0"));
				return view;
			};
		};
		setListAdapter(adapter);
		Log.d("FRAGMENT", "Adapter created");
		return v;
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		parent.updateDetails(getPositionID(position));
		parent.mainPosition = position;
	}
	private  int getPositionID(int newPosition){
		Assignment a = assignments.get(newPosition);
		return a.get_ID();
	}

	private ArrayList<Map<String, String>> buildData() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// initialize the variable
		assignments = new ArrayList<Assignment>();
		// get all the assignment
		assignments = MyDBHelper.DBHelper.getAllAssignment();
		if (assignments.size() > 0) // to string the content
			for (Assignment a : assignments)
				list.add(putData(a.getModuleCode(),
						a.getWhenDue() + " " + a.getProgress() + "%"));
		return list;
	}

	private HashMap<String, String> putData(String name, String purpose) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put("name", name);
		item.put("purpose", purpose);
		return item;
	}
}
