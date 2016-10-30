package de.martingolpashin.sensorrecord.models;

import android.location.Location;

/**
 * Created by martin on 14.10.16.
 */
public class GPSData {
    private long millis;
    private Location location;
    private String baseDir;

    public GPSData(long milliseconds, Location location) {
        this.millis = milliseconds;
        this.location = location;
        this.baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public Location getLocation() {
        return location;
    }

    public long getMillis() {
        return millis;
    }
}
