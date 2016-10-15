package de.martingolpashin.sensorfusion.models;

import android.location.Location;

/**
 * Created by martin on 14.10.16.
 */
public class GPSData extends Sensor {
    private Location location;

    public GPSData(long milliseconds, Location location) {
        super(milliseconds);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
