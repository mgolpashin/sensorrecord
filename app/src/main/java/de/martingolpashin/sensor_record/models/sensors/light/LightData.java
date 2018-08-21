package de.martingolpashin.sensor_record.models.sensors.light;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.martingolpashin.sensor_record.models.SensorData;

public class LightData extends SensorData {
    private Date timestamp;
    private long millis;
    private float illuminance;

    public LightData(Date timestamp, long milliseconds, float illuminance) {
        this.timestamp = timestamp;
        this.millis = milliseconds;
        this.illuminance = illuminance;
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.timestamp);
    }

    public long getMillis() {
        return millis;
    }

    public float getIlluminance() {
        return illuminance;
    }

    public static String[] getHeadline() {
        return new String[]{"Timestamp", "Milliseconds", "Illuminance"};
    }

    @Override
    public String toString() {
        return this.getTimestamp() + getSeperator() + this.getMillis() + getSeperator() + this.getIlluminance() + System.getProperty("line.separator");
    }
}
