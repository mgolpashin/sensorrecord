package de.martingolpashin.sensor_record.models.sensors.pressure;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.martingolpashin.sensor_record.models.SensorData;

public class PressureData extends SensorData{
    private Date timestamp;
    private long millis;
    private float millibars;

    public PressureData(Date timestamp, long milliseconds, float millibars) {
        this.timestamp = timestamp;
        this.millis = milliseconds;
        this.millibars = millibars;
    }

    public long getMillis() {
        return millis;
    }

    public float getMillibars() {
        return millibars;
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.timestamp);
    }

    public static String[] getHeadline() {
        return new String[]{"Timestamp", "Milliseconds", "Millibars"};
    }

    @Override
    public String toString() {
        return this.getTimestamp() + getSeperator() + this.getMillis() + getSeperator() + this.getMillibars() + System.getProperty("line.separator");
    }
}
