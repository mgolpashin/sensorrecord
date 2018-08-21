package de.martingolpashin.sensor_record.models.sensors.ambient_temperature;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.martingolpashin.sensor_record.models.SensorData;

public class AmbientTemperatureData extends SensorData{
    private Date timestamp;
    private long millis;
    private float degree;

    public AmbientTemperatureData(Date timestamp, long milliseconds, float degree) {
        this.timestamp = timestamp;
        this.millis = milliseconds;
        this.degree = degree;
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.timestamp);
    }

    public long getMillis() {
        return millis;
    }

    public float getDegree() {
        return degree;
    }

    public static String[] getHeadline() {
        return new String[]{"Timestamp", "Milliseconds", "Degrees"};
    }

    @Override
    public String toString() {
        return this.getTimestamp() + getSeperator() + this.getMillis() + getSeperator() + this.getDegree() + System.getProperty("line.separator");
    }
}
