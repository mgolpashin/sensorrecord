package de.martingolpashin.sensor_record.models.sensors.rotation_vector;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.martingolpashin.sensor_record.models.SensorData;

public class RotationVectorData extends SensorData{
    private Date timestamp;
    private long millis;
    private float x;
    private float y;
    private float z;
    private float cos;
    private float headingAccuracy;

    public RotationVectorData(Date timestamp, long milliseconds, float x, float y, float z, float cos, float headingAccuracy) {
        this.timestamp = timestamp;
        this.millis = milliseconds;
        this.x = x;
        this.y = y;
        this.z = z;
        this.cos = cos;
        this.headingAccuracy = headingAccuracy;
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

    public float getCos() {
        return cos;
    }

    public float getHeadingAccuracy() {
        return headingAccuracy;
    }

    public static String[] getHeadline() {
        return new String[]{"Timestamp", "Milliseconds", "X", "Y", "Z", "cos", "headingAccuracy"};
    }

    @Override
    public String toString() {
        return this.getTimestamp() + getSeperator() + this.getMillis() + getSeperator() + this.getX() + getSeperator() + this.getY() + getSeperator() + this.getZ() + getSeperator() + this.getCos() + getSeperator() + this.getHeadingAccuracy() + System.getProperty("line.separator");
    }
}
