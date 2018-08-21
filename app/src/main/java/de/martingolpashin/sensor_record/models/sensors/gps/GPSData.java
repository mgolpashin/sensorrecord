package de.martingolpashin.sensor_record.models.sensors.gps;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.martingolpashin.sensor_record.models.SensorData;

public class GPSData extends SensorData{
    private Date timestamp;
    private long millis;
    private Location location;

    public GPSData(Date timestamp, long milliseconds, Location location) {
        this.timestamp = timestamp;
        this.millis = milliseconds;
        this.location = location;
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.timestamp);
    }

    public Location getLocation() {
        return location;
    }

    public long getMillis() {
        return millis;
    }

    public static String[] getHeadline() {
        return new String[]{"Timestamp", "Milliseconds", "Latitude", "Longitude", "Altitude"};
    }

    @Override
    public String toString() {
        return this.getTimestamp() + getSeperator() + this.getMillis() + getSeperator() + this.getLocation().getLatitude() + getSeperator() + this.getLocation().getLongitude() + getSeperator() + this.getLocation().getAltitude() + System.getProperty("line.separator");
    }

}
