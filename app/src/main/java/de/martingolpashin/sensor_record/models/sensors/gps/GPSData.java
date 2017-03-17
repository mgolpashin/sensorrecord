package de.martingolpashin.sensor_record.models.sensors.gps;

import android.location.Location;

/**
 * Created by martin on 14.10.16.
 */
public class GPSData {
    private long millis;
    private Location location;

    public GPSData(long milliseconds, Location location) {
        this.millis = milliseconds;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public long getMillis() {
        return millis;
    }

    @Override
    public String toString() {
        return this.getMillis() + ";" + this.getLocation().getLatitude() + ";" + this.getLocation().getLongitude() + ";" + this.getLocation().getAltitude() + ";" + System.getProperty("line.separator");
    }

}
