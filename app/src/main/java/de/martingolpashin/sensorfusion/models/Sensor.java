package de.martingolpashin.sensorfusion.models;

/**
 * Created by martin on 14.10.16.
 */
public class Sensor {
    private long milliseconds;
    private String baseDir;
    private boolean isActive;

    public Sensor(long milliseconds) {
        this.milliseconds = milliseconds;
        this.baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        this.isActive = false;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }
}
