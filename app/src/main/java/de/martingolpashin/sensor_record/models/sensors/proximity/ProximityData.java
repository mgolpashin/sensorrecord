package de.martingolpashin.sensor_record.models.sensors.proximity;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.martingolpashin.sensor_record.models.SensorData;

public class ProximityData extends SensorData{
    private Date timestamp;
    private long millis;
    private float centimeters;

    public ProximityData(Date timestamp, long milliseconds, float centimeters) {
        this.timestamp = timestamp;
        this.millis = milliseconds;
        this.centimeters = centimeters;
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.timestamp);
    }

    public long getMillis() {
        return millis;
    }

    public float getCentimeters() {
        return centimeters;
    }

    public static String[] getHeadline() {
        return new String[]{"Timestamp", "Milliseconds", "centimeters"};
    }

    @Override
    public String toString() {
        return this.getTimestamp() + getSeperator() + this.getMillis() + getSeperator() + this.getCentimeters() + System.getProperty("line.separator");
    }
}
