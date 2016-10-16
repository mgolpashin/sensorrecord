package de.martingolpashin.sensorfusion.models;

import android.location.Location;

/**
 * Created by martin on 14.10.16.
 */
public class CompassData {
    private long millis;
    private String baseDir;

    public CompassData(long milliseconds, Location location) {
        this.millis = milliseconds;
        this.baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    }

}
