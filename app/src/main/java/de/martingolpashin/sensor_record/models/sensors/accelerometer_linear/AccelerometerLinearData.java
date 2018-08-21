package de.martingolpashin.sensor_record.models.sensors.accelerometer_linear;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.martingolpashin.sensor_record.models.SensorData;

public class AccelerometerLinearData extends SensorData{
    private Date timestamp;
    private long millis;
    private float x;
    private float y;
    private float z;

    public AccelerometerLinearData(Date timestamp, long milliseconds, float x, float y, float z) {
        this.timestamp = timestamp;
        this.millis = milliseconds;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.timestamp);
    }

    public long getMillis() {
        return millis;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public static String[] getHeadline() {
        return new String[]{"Timestamp", "Milliseconds", "X", "Y", "Z"};
    }

    @Override
    public String toString() {
        return this.getTimestamp() + getSeperator() + this.getMillis() + getSeperator() + this.getX() + getSeperator() + this.getY() + getSeperator() + this.getZ() + System.getProperty("line.separator");
    }
}
