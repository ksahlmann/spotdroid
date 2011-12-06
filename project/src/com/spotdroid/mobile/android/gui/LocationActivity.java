package com.spotdroid.mobile.android.gui;

import java.sql.SQLException;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.spotdroid.mobile.android.R;
import com.spotdroid.mobile.android.data.DatabaseHelper;
import com.spotdroid.mobile.android.data.LocationData;

/**
 * Used to show the GPS Location and Tracking.
 * 
 */
public class LocationActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	/** Tag for log tracking. */
	private static final String TAG = LocationActivity.class.getSimpleName();

	/** Instance of LocationManager. */
	private LocationManager locationManager;

	/** Listener for GPS provider. */
	private MyLocationListener myLocationListener;

	/** Indicates the GPS tracking is started. */
	private boolean gpsTrackingStarted = false;

	/** Request code for GPS preference activity call. */
	private static final int REQUEST_CODE = 2;

	/**
	 * The minimum time interval for notifications, in milliseconds. This field
	 * is only used as a hint to conserve power, and actual time between
	 * location updates may be greater or lesser than this value. Will be
	 * replaced by user settings.
	 */
	private static final int MIN_TIME = 1000;

	/**
	 * The minimum distance interval for notifications, in meters. Will be
	 * replaced by user settings.
	 */
	private static final int MIN_DISTANCE = 100;

	/** The osmdroid mapview to show the map. */
	protected MapView mapView;

	/** The osmdroid overlay to show the location. */
	protected MyLocationOverlay myLocationOverlay;

	/** initialize database helper class*/
	static 
	{
		OpenHelperManager.setOpenHelperClass(DatabaseHelper.class); 
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set layout
		setContentView(R.layout.location);
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);
		// set zoom to default
		mapView.getController().setZoom(5);
		mapView.getController().setCenter(new GeoPoint(51.00, 10.20));
		mapView.setTileSource(TileSourceFactory.MAPNIK);
		myLocationOverlay = new MyLocationOverlay(this.getApplicationContext(),
				this.mapView);
		mapView.getOverlays().add(myLocationOverlay);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		myLocationOverlay.disableMyLocation();
		super.onPause();
		// TODO: maybe to stop the gps tracking if the app is off?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// enable location
		myLocationOverlay.enableMyLocation();

		// reset the button and texts.
		if (!isGPSEnabled()) {
			gpsTrackingStarted = false;
			((Button) findViewById(R.id.btn_start_gps))
					.setText(R.string.key_btn_start_gps);
			cleanUpCoordinates();
		}

		super.onResume();
	}

	/**
	 * This method is called from layout button, id=btn_start_gps. Check if GPS
	 * tracking was started and change the button state and text.
	 * 
	 * @param view
	 */
	public void onClickGPS(View view) {
		if (gpsTrackingStarted) {
			// stop tracking
			gpsTrackingStarted = false;
			((Button) findViewById(R.id.btn_start_gps))
					.setText(R.string.key_btn_start_gps);
			cleanUpCoordinates();
			locationManager.removeUpdates(myLocationListener);
		} else {
			if (!isGPSEnabled()) {
				// show dialog
				showGPSDialog();
			} else {
				// start tracking
				startGPSTracking();
			}
		}
	}

	/**
	 * Starts the GPS Tracking and change the button state.
	 */
	private void startGPSTracking() {
		Log.d(TAG, "GPS Location Started");

		gpsTrackingStarted = true;
		((Button) findViewById(R.id.btn_start_gps))
				.setText(R.string.key_btn_stop_gps);

		myLocationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, myLocationListener);
	}

	/**
	 * show dialog to activate GPS.
	 */
	private void showGPSDialog() {
		// show dialog
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		ActivateGPSDialogListener listener = new ActivateGPSDialogListener();
		alertBuilder.setPositiveButton(R.string.key_dlg_btn_yes, listener);
		alertBuilder.setNegativeButton(R.string.key_dlg_btn_no, listener);
		alertBuilder.setMessage(R.string.key_dlg_msg_gps_off);
		final AlertDialog alert = alertBuilder.create();
		alert.show();
	}

	/**
	 * Dialog Listener to activate GPS.
	 */
	private class ActivateGPSDialogListener implements OnClickListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.DialogInterface.OnClickListener#onClick(android.content
		 * .DialogInterface, int)
		 */
		@Override
		public void onClick(DialogInterface dialog, int key) {
			switch (key) {
			case AlertDialog.BUTTON_POSITIVE:
				dialog.cancel();
				// go to GPS preferences.
				Intent intent = new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(intent, REQUEST_CODE);
				break;
			case AlertDialog.BUTTON_NEGATIVE:
				dialog.cancel();
				break;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "RequestCode: " + requestCode + " ResultCode: " + resultCode);

		if (requestCode == REQUEST_CODE && resultCode == RESULT_CANCELED) {
			if (isGPSEnabled()) {
				// start tracking
				startGPSTracking();
			} else {
				// GPS is off
				// TODO : maybe show the message, the gps is off ? 
			}
		}
	}

	/**
	 * Check if GPS is enabled.
	 * 
	 * @return true, if GPS is on.
	 */
	private boolean isGPSEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * Utility method to clean up the text fields with GPS coordinates.
	 */
	private void cleanUpCoordinates() {
		((TextView) findViewById(R.id.txt_latitude)).setText("");
		((TextView) findViewById(R.id.txt_longitude)).setText("");
	}

	/**
	 * Locations Listener for GPS provider, updates the fields on changes.
	 */
	public class MyLocationListener implements LocationListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onStatusChanged(java.lang.String,
		 * int, android.os.Bundle)
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d(TAG, "onStatusChanged: " + provider + ", Status: " + status);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onProviderEnabled(java.lang.String)
		 */
		@Override
		public void onProviderEnabled(String provider) {
			Log.d(TAG, "onProviderEnabled: " + provider);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onProviderDisabled(java.lang.String
		 * )
		 */
		@Override
		public void onProviderDisabled(String provider) {
			Log.d(TAG, "onProviderDisabled: " + provider);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onLocationChanged(android.location
		 * .Location)
		 */
		@Override
		public void onLocationChanged(Location location) {
			Log.d(TAG, "Location changed: " + location.getLatitude() + " / "
					+ location.getLongitude());

			((TextView) findViewById(R.id.txt_latitude)).setText(String
					.valueOf(location.getLatitude()));
			((TextView) findViewById(R.id.txt_longitude)).setText(String
					.valueOf(location.getLongitude()));

			GeoPoint point = new GeoPoint(location);
			// move to location
			// animation doesn't set the center properly -> bug
			// mapView.getController().animateTo(point,
			// MapController.AnimationType.LINEAR);
			mapView.getController().setZoom(15);
			mapView.getController().setCenter(point);

			// save location in database
			LocationData locationData = new LocationData(location.getLatitude(), location.getLongitude());
			saveLocation(locationData);

		}
	}

	/**
	 * save the location in database.
	 * 
	 * @param location
	 * @throws SQLException 
	 */
	private void saveLocation(LocationData locationData) 
	{
		// get dao
		Dao<LocationData, Integer> locationDao = null;
		try 
		{
			locationDao = getHelper().getLocationDao();
			locationDao.create(locationData);
			Log.d(TAG, "Saved in Database: " + locationData.toString());
		} 
		catch (SQLException e) 
		{
			Log.e(TAG, "Could not save");
			e.printStackTrace();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.location_optionmenu, menu);
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
		case R.id.opt_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}