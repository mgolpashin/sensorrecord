package de.martingolpashin.sensor_record.models.sensors.proximity;

import de.martingolpashin.sensor_record.models.SensorData;

/**
 * Created by martin on 14.10.16.
 */
public class ProximityData extends SensorData{
    private long millis;
    private float centimeters;

    public ProximityData(long milliseconds, float centimeters) {
        this.millis = milliseconds;
        this.centimeters = centimeters;
    }

    public long getMillis() {
        return millis;
    }

    public float getCentimeters() {
        return centimeters;
    }

    @Override
    public String toString() {
        return this.getMillis() + getSeperator() + this.getCentimeters() + System.getProperty("line.separator");
    }
}
