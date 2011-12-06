package com.spotdroid.mobile.android.data;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * ormlite data object for location data.
 * 
 */
@DatabaseTable(tableName = "location")
public class LocationData implements Serializable {

	public LocationData() {
		super();
		this.timestamp = new Date();  
	}

	private static final long serialVersionUID = 8532070207251592573L;

	public LocationData(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = new Date(); 
	}

	@DatabaseField(generatedId = true)
	private Integer id;

	@DatabaseField
	private double latitude;

	@DatabaseField
	private double longitude;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date timestamp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {

		String time = "";
		if (timestamp != null) 
		{
			time = timestamp.toGMTString();
		}

		return "LocationData [id=" + id + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", timestamp=" + time + "]";
	}

}
