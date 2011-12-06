package com.spotdroid.mobile.android.gui;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.spotdroid.mobile.android.R;
import com.spotdroid.mobile.android.data.DatabaseHelper;
import com.spotdroid.mobile.android.data.LocationData;

/**
 * Used to show the activity index in table layout. 
 * The data will be loaded from the internal database. 
 */
public class ActivityIndexActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	/** Tag for log tracking. */
	private static final String TAG = ActivityIndexActivity.class.getSimpleName();

	private TableLayout table;

	/** initialize database helper class */
	static {
		OpenHelperManager.setOpenHelperClass(DatabaseHelper.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set layout
		setContentView(R.layout.activity_index);
		// set table
		table = (TableLayout) findViewById(R.id.layout_activity_index);

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// load data
		loadIndex();
	}

	/**
	 * create dynamic table layout and fill it with data of activity index.
	 */
	private void loadIndex() {

		// get dao
		Dao<LocationData, Integer> locationDao = null;

		int index = 1;
		int left = 3, top = 3, right = 3, bottom = 3;

		// first clean the table except first row
		table.removeViews(index, (table.getChildCount()-1));

		// query for all of the data objects in the database
		List<LocationData> list;
		try {
			locationDao = getHelper().getLocationDao();
			list = locationDao.queryForAll();
			for (LocationData data : list) {

				TableRow row = new TableRow(this);

				TextView viewID = new TextView(this);
				viewID.setPadding(left, top, right, bottom);
				viewID.setText(data.getId().toString());
				row.addView(viewID);

				TextView viewLongitude = new TextView(this);
				viewLongitude.setPadding(left, top, right, bottom);
				viewLongitude.setText(String.valueOf(data.getLongitude()));
				row.addView(viewLongitude);

				TextView viewLatitude = new TextView(this);
				viewLatitude.setPadding(left, top, right, bottom);
				viewLatitude.setText(String.valueOf(data.getLatitude()));
				row.addView(viewLatitude);

				TextView viewTimestamp = new TextView(this);
				viewTimestamp.setPadding(left, top, right, bottom);
				viewTimestamp.setText(data.getTimestamp().toLocaleString());
				row.addView(viewTimestamp);

				table.addView(row, index);
				index++;

			}
		} catch (SQLException e) {
			Log.e(TAG, "Could not read data");
			e.printStackTrace();
		} finally {

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_index_optionmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.opt_refresh_index:
			//refresh the view
			onResume();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
