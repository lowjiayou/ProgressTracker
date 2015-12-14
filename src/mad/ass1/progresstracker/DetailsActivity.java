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

public class DetailsActivity extends FragmentActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		Intent intent = getIntent();

		int orientation = getResources().getConfiguration().orientation;

		// destroy this activity and go back to the "base" view if we are in
		// landscape mode
		// this activity is only for portrait mode
		if (orientation == Configuration.ORIENTATION_LANDSCAPE)
			finish();

		if (intent != null) {
			FragmentManager manager = getSupportFragmentManager();
			MyDetailFragment detailFragment = (MyDetailFragment) manager
					.findFragmentById(R.id.detail_fragment);
			if (detailFragment != null) {
				int position = intent.getIntExtra("POSITION", -1);
				if (position > -1) {
					detailFragment.updateDetails(position);
				}
			}
		} else {
			Log.d("FRAGMENT", "DetailsActivity not started by intent");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.update_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = getIntent();
		int position = i.getIntExtra("POSITION", -1);
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_update_assignment:
			showEditForm(position);
			return true;
		case R.id.menu_delete_assignment:
			confirmationBeforeDelete(position);
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
		AlertDialog dgbox = makeShowDialogBox(id);
		dgbox.show();
	}

	private AlertDialog makeShowDialogBox(final int id) {
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
								MyDBHelper.DBHelper.deleteAssignment(id);
								// and show message 
								Toast.makeText(DetailsActivity.this,
										"Success delete assignment",
										Toast.LENGTH_LONG).show();
								finish();
								// return to main page after deletion
								Intent intent = new Intent(
										DetailsActivity.this,
										MainActivity.class);
								startActivity(intent);
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

}
