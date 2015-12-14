package mad.ass1.progresstracker;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.Toast;

public class Assignment {
	private int _id;
	private String moduleCode;
	private String assignmentName;
	private int marksProportion;
	private Date whenDue;
	private int progress;

	@Override
	public String toString() {
		return "Assignment [moduleCode=" + moduleCode + ", assignmentName="
				+ assignmentName + ", marksProportion=" + marksProportion
				+ ", whenDue=" + whenDue + ", progress=" + progress + "]";
	}

	public String toListViewString() {
		return moduleCode + " " + dateToString(whenDue) + " " + progress + " ";
	}

	public Assignment() {
		this._id = -1;
		this.moduleCode = null;
		this.assignmentName = null;
		this.marksProportion = -1;
		this.whenDue = null;
		this.progress = 0;
	}

	public Assignment(String moduleCode, String assignmentName,
			int marksProportion, String whenDue, int progress) {
		this.moduleCode = moduleCode;
		this.assignmentName = assignmentName;
		this.marksProportion = marksProportion;
		this.whenDue = strToDate(whenDue);
		this.progress = progress;
	}

	public int get_ID() {
		return _id;
	}

	public void set_ID(int _id) {
		this._id = _id;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode.toUpperCase();
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public int getMarksProportion() {
		return marksProportion;
	}

	public void setMarksProportion(int marksProportion) {
		this.marksProportion = marksProportion;
	}

	public String getWhenDue() {
		return dateToString(whenDue);
	}

	public void setWhenDue(String strWhenDue) {
		Date whenDue = strToDate(strWhenDue);
		this.whenDue = whenDue;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public Date strToDate(String val) {
		// using the format of year/month date
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		// trying to catch when error occur
		try {
			date = format1.parse(val);
		} catch (ParseException e) {
			Toast.makeText(null, "Ting dong! error occur: strToDate",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return date;
	}

	public String dateToString(Date date) {
		Format formatter = new SimpleDateFormat("yyyy/MM/dd");
		String s = formatter.format(date);
		return s;
	}

}
