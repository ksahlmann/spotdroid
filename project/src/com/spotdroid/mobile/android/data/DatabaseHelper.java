package com.spotdroid.mobile.android.data;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of the
 * database. This class provides the DAOs used by the other classes.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	/** Tag for log tracking. */
	private static final String TAG = DatabaseHelper.class.getSimpleName();

	/** name of the database file */
	private static final String DATABASE_NAME = "spotdroid.db";
	/** database version will be increased by changes */
	private static final int DATABASE_VERSION = 1;

	/** the DAO object for access the Location table */
	private Dao<LocationData, Integer> locationDao = null;

	/**
	 * constructor.
	 */
	public DatabaseHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, LocationData.class);
		} catch (SQLException e) {
			Log.e(TAG, "Can't create database", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			// drop old database
			TableUtils.dropTable(connectionSource, LocationData.class, true);
			// create new database
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * returns the DAO for LocationData.
	 * 
	 * @return Dao<LocationData, Integer>
	 * @throws SQLException
	 */
	public Dao<LocationData, Integer> getLocationDao() throws SQLException {
		if (locationDao == null) {
			locationDao = getDao(LocationData.class);
		}
		return locationDao;
	}

}
