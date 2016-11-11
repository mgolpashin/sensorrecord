package de.martingolpashin.sensorrecord.models;

/**
 * Created by martin on 14.10.16.
 */
public class CompassData {
    private long millis;
    private String baseDir;

    public CompassData(long milliseconds) {
        this.millis = milliseconds;
        this.baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    }

}
