package mad.ass1.progresstracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class AddAssignmentActivity extends Activity {
	private int position;
	EditText et_ModuleCode, et_AssignmentName, et_MarksProportion, et_WhenDue,
			et_Progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_layout); 
		Intent intent = getIntent();
		position = intent.getIntExtra("AID", -1);
		findETView();
		setEditTextEvent();
		if (intent != null && position > -1) {
			//Assignment a = MyListFragment.assignments.get(position);
			Assignment a = MyDBHelper.DBHelper.getAssignmentByID(position);
			loadAssignment(a);
		} else {
			Log.d("ADD/Update", "AddAssignmentActivity not started by intent");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_save_assignment:
			saveToDB();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean isValid() {
		boolean[] isValid = new boolean[5];
		// et_ModuleCode.getError() == null &&
		isValid[0] = (moduleCodeValidation()) ? true : false;
		// boolean a = (isValid[0]) ? et_ModuleCode.requestFocus() : false;
		isValid[1] = (assignmentNameValidation()) ? true : false;
		// a = (isValid[1]) ? et_ModuleCode.requestFocus() : false;
		isValid[2] = (marksProportionValidation()) ? true : false;
		// a = (isValid[2]) ? et_ModuleCode.requestFocus() : false;
		isValid[3] = (progressValidation()) ? true : false;
		// a = (isValid[3]) ? et_ModuleCode.requestFocus() : false;
		isValid[4] = (whenDueValidation()) ? true : false;
		// a = (isValid[4]) ? et_ModuleCode.requestFocus() : false;
		for (boolean b : isValid)
			if (b == false)
				return false;
		return true;
	}

	// find edit text view from ui
	private void findETView() {
		et_ModuleCode = (EditText) findViewById(R.id.edittxt_module_code);
		et_AssignmentName = (EditText) findViewById(R.id.edittxt_assignment_name);
		et_MarksProportion = (EditText) findViewById(R.id.edittxt_marks_proportion);
		et_Progress = (EditText) findViewById(R.id.edittxt_progress);
		et_WhenDue = (EditText) findViewById(R.id.edittxt_when_due);
	}

	// check is text empty
	private static boolean checkIsEmpty(String val, EditText et) {
		// if is empty , then set error message
		if (val.length() <= 0) {
			et.setError("Required field");
			return true;
		} else {
			et.setError(null);
			return false;
		}
	}

	// check is value within range
	private static boolean checkRange(String val, EditText et, int lb, int ub) {
		// lb is lower boundary, up is ub boundary
		try {
			// try to convert to integer to validate input
			int i = Integer.parseInt(val);
			// show error if value not within range
			if (!(i >= lb && i <= ub)) {
				et.setError("Accept number range from " + et.getHint());
				return false;
			} else {
				et.setError(null);
				return true;
			}
		} catch (NumberFormatException nfe) {
			et.setError("Accept numbers only");
			return false;
		}
	}

	// check is it valid date

	private static boolean checkDate(String val, EditText et) {
		String regex = "((19|20)\\d\\d)/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])";
		if (!(val.matches(regex))) {
			et.setError("Please enter valid date");
			return false;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		try {
			date = dateFormat.parse(val);// "05/01/1999"
		} catch (ParseException e) {
			et.setError("Accept only " + et.getHint() + "format");
			return false;
		}
		if (date.before(new Date())) {
			et.setError("Accept due date before today");
			return false;
		} else {
			et.setError(null);
			return true;
		}

	}

	// check is the code match format
	private static boolean checkCode(String val, EditText et) {
		// regular expression accepting 2 alphabet and 4 number only
		String regex = "([a-zA-Z]{2}[0-9]{4})";
		if (!(val.matches(regex))) {
			et.setError("Format error. " + et.getHint());
			return false;
		}
		// try to check is module number 0000
		int code = Integer.parseInt(val.substring(2));
		if (code <= 0) {
			et.setError("Cannot enter 0000 as code");
			return false;
		} else {
			et.setError(null);
			return true;
		}
	}

	private boolean moduleCodeValidation() {
		String val = et_ModuleCode.getText().toString();
		et_ModuleCode.setText(val.toUpperCase());
		if (!checkIsEmpty(val, et_ModuleCode))
			return checkCode(val, et_ModuleCode);
		return false;
	}

	private boolean assignmentNameValidation() {
		// get the value in edit text
		String val = et_AssignmentName.getText().toString();
		return !(checkIsEmpty(val.trim(), et_AssignmentName));
	}

	private boolean marksProportionValidation() {
		// get the value in edit text
		String val = et_MarksProportion.getText().toString();
		if (!checkIsEmpty(val, et_MarksProportion))
			return checkRange(val, et_MarksProportion, 1, 100);
		return false;
	}

	private boolean progressValidation() {
		// get the value in edit text
		String val = et_Progress.getText().toString();
		if (!checkIsEmpty(val, et_Progress))
			return checkRange(val, et_Progress, 0, 100);
		return false;
	}

	private boolean whenDueValidation() {
		// get the value in edit text
		String val = et_WhenDue.getText().toString();
		if (!checkIsEmpty(val, et_WhenDue))
			return checkDate(val, et_WhenDue);
		return false;

	}

	private void setEditTextEvent() {
		et_ModuleCode.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				// when Edittext lose focus
				if (!hasFocus) {// get the value in edit text
					moduleCodeValidation();
				}
			}
		});
		et_AssignmentName.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				// when Edit text lose focus
				if (!hasFocus) {
					assignmentNameValidation();
				}
			}
		});
		et_MarksProportion
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					public void onFocusChange(View v, boolean hasFocus) {
						// when Edit text lose focus
						if (!hasFocus) {
							marksProportionValidation();

						}
					}
				});
		et_WhenDue.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				// when Edit text lose focus
				if (!hasFocus) {
					whenDueValidation();
				}
			}
		});
		et_Progress.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				// when Edit text lose focus
				if (!hasFocus) {
					progressValidation();
				}
			}
		});
	}

	// load assignment to edit text
	private void loadAssignment(Assignment a) {
		Log.d("DEBUG", a.toString());
		try {
			et_ModuleCode.setText(a.getModuleCode());
			et_AssignmentName.setText(a.getAssignmentName());
			et_MarksProportion.setText(a.getMarksProportion() + "");
			et_WhenDue.setText(a.getWhenDue());
			et_Progress.setText(a.getProgress() + "");
		} catch (Exception e) {
			Toast.makeText(this, "Ding dong" + e.toString(), Toast.LENGTH_LONG)
					.show();
		}
	}

	// get edit text value and save to database
	private void saveToDB() {
		if (!isValid()) {
			Toast.makeText(this, "Complete form before save",
					Toast.LENGTH_SHORT).show();
			return;
		}

		// boolean[] o= isValid();
		// int c = 0;
		// for (boolean b : o) {
		// Log.d("DEBUG", (c++) + ": " + b);
		// }
		//
		// return;
		// Toast.makeText(this, stk, Toast.LENGTH_LONG).show();
		 
		Assignment a = new Assignment();
		String val;
		a.setModuleCode(et_ModuleCode.getText().toString());
		val = et_AssignmentName.getText().toString();
		a.setAssignmentName(val.toUpperCase());
		val = et_MarksProportion.getText().toString();
		a.setMarksProportion(Integer.parseInt(val));
		val = et_WhenDue.getText().toString();
		a.setWhenDue(val);
		val = et_Progress.getText().toString();
		a.setProgress(Integer.parseInt(val));
		String smth = "";
		if (position > -1) {
			smth = MyDBHelper.DBHelper.updateAssignment(position, a);
		} else {
			MyDBHelper.DBHelper.addAssignment(a);
		}
		Toast.makeText(this, "Save successful", Toast.LENGTH_LONG).show();	
		finish();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
