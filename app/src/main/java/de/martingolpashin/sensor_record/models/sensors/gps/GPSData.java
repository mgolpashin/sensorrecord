package de.martingolpashin.sensor_record.models.sensors.gps;

import android.location.Location;

import de.martingolpashin.sensor_record.models.SensorData;

public class GPSData extends SensorData{
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
        return this.getMillis() + getSeperator() + this.getLocation().getLatitude() + getSeperator() + this.getLocation().getLongitude() + getSeperator() + this.getLocation().getAltitude() + System.getProperty("line.separator");
    }

}
