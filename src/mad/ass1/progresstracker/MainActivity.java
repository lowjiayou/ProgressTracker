package mad.ass1.progresstracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private MyDetailFragment detailFragment = null;
	private MyListFragment listFragment = null;
	private FragmentManager manager;
	private int selectedItemIndex = -1;
	public static int mainPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// initialize databases
		MyDBHelper.DBHelper = new MyDBHelper(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		manager = getSupportFragmentManager();
		detailFragment = (MyDetailFragment) manager
				.findFragmentById(R.id.detail_fragment);
		listFragment = (MyListFragment) manager
				.findFragmentById(R.id.list_fragment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int orientation = getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getMenuInflater().inflate(R.menu.activity_main_land, menu);
			return true;
		}

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	private  int getPositionID(int newPosition){
		Assignment a = MyListFragment.assignments.get(newPosition);
		return a.get_ID();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_add_assignment:
			showAddForm();
			return true;
		case R.id.menu_update_assignment:try {
			showEditForm(getPositionID(mainPosition));} catch (Exception e) {
				Log.d("DEBUG", ""+e.toString());
			}
			return true;
		case R.id.menu_delete_assignment:
				confirmationBeforeDelete(mainPosition);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showEditForm(int id) {
		Intent intent = new Intent(this, AddAssignmentActivity.class);
		intent.putExtra("AID", id);
		startActivity(intent);
	}

	public void confirmationBeforeDelete(int id) {
		mainPosition = id;
		AlertDialog dgbox = makeShowDialogBox();
		dgbox.show();
	}

	private AlertDialog makeShowDialogBox() {
		AlertDialog dgbox = new AlertDialog.Builder(this)
				// set message and title
				.setTitle("Confirm?")
				.setMessage("Are you sure that you want to delete?")

				// set three option buttons
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// delete
								MyDBHelper.DBHelper
										.deleteAssignment(mainPosition);
								// and show message
								Toast.makeText(
										MainActivity.this,
										"Success delete assignment ",
										Toast.LENGTH_LONG).show();
								// return to main page after deletion
								MainActivity.this.finish();
							}
						})// setPositiveButton
				.setNeutralButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						})// setNeutralButton

				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				})// setNegativeButton

				.create();

		return dgbox;
	}

	private void showAddForm() {
		Intent intent = new Intent(this, AddAssignmentActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		int orientation = getResources().getConfiguration().orientation;

		if (orientation == Configuration.ORIENTATION_LANDSCAPE)
			updateDetails(selectedItemIndex);

	}

	public void updateDetails(int selectedItem) {
		Log.d("FRAGMENT", "Selected item " + selectedItem);
		selectedItemIndex = selectedItem;

		int orientation = getResources().getConfiguration().orientation;

		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

			if (detailFragment != null) {
				// update entry
				detailFragment.updateDetails(selectedItem);
			}
		} else {
			// show DetailsActivity
			Intent intent = new Intent(this, DetailsActivity.class);
			intent.putExtra("POSITION", selectedItem);
			startActivity(intent);
		}
	}
}
