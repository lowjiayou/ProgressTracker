package mad.ass1.progresstracker;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
	public static MyDBHelper DBHelper;
	private SQLiteDatabase db;
	private static final String TABLENAME = "progresstracker";

	public MyDBHelper(Context context) {
		super(context, "progresstracker.db", null, 1);
	}

	public void addAssignment(Assignment a){
		db = DBHelper.getWritableDatabase();
		String cmd = new String("INSERT INTO " + TABLENAME 
				+ " VALUES (null, "
				+ "'"+a.getModuleCode() + "', "
				+ "'"+a.getAssignmentName() + "', "
				+ "'"+a.getMarksProportion()+ "', "
				+ "'"+a.getWhenDue() + "', "
				+ "'"+a.getProgress()  + "'"
				+ ");");
		db.execSQL(cmd);
		closeDB();
	}

	private void closeDB() {
		if (db.isOpen())
			db.close();
		db = null;
	}

	public void deleteAssignment(int id) {
		db = DBHelper.getWritableDatabase();
		String cmd = new String("DELETE FROM " + TABLENAME + " WHERE _id=" + id
				+ ";");
		db.execSQL(cmd);
		closeDB();
	}

	public String updateAssignment(int id , Assignment a){
		db = DBHelper.getWritableDatabase();
		String cmd = new String("UPDATE "+TABLENAME+" SET " 
				+ "module_code = '"+a.getModuleCode() + "', "
				+ "assignment_name = '"+a.getAssignmentName() + "', "
				+ "marks_proportion = "+a.getMarksProportion()+ ", "
				+ "when_due = '"+a.getWhenDue() + "', "
				+ "progress = "+a.getProgress() + ""
				+" WHERE _id="+id
				+";");
		db.execSQL(cmd);
		closeDB();
		return cmd; 
	}
	
	public ArrayList<Assignment> getAllAssignment(){
		db = DBHelper.getReadableDatabase();
		Cursor csr= db.rawQuery("SELECT * FROM "+TABLENAME+ " ORDER BY when_due ASC", null);
		
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		while (csr.moveToNext()) {
			Assignment a = new Assignment();
			a.set_ID(csr.getInt(0));
			a.setModuleCode(csr.getString(1));
			a.setAssignmentName(csr.getString(2));
			a.setMarksProportion(csr.getInt(3));
			a.setWhenDue(csr.getString(4));
			a.setProgress(csr.getInt(5));
			assignments.add(a);
		}
		closeDB();
		if (!csr.isClosed())
			csr.close();

		return (assignments.size() == 0) ? new ArrayList<Assignment>()
				: assignments;
	}

	public Assignment getAssignmentByID(int id){
		db = DBHelper.getReadableDatabase();
		Cursor csr= db.rawQuery("SELECT * FROM "+TABLENAME+" WHERE _id="+id, null);
		
		csr.moveToNext();
		Assignment a = new Assignment();
		a.set_ID(csr.getInt(0));
		a.setModuleCode(csr.getString(1));
		a.setAssignmentName(csr.getString(2));
		a.setMarksProportion(csr.getInt(3));
		a.setWhenDue(csr.getString(4));
		a.setProgress(csr.getInt(5));
	
		closeDB();
		if (!csr.isClosed())
			csr.close();
		
		return a;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLENAME
				+ " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "module_code TEXT, " + "assignment_name TEXT, "
				+ "marks_proportion INTEGER, " + "when_due TEXT, "
				+ "progress INTEGER);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
