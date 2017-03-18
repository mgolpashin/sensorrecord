package de.martingolpashin.sensor_record.models.sensors.ambient_temperature;

import de.martingolpashin.sensor_record.models.SensorData;

/**
 * Created by martin on 14.10.16.
 */
public class AmbientTemperatureData extends SensorData{
    private long millis;
    private float degree;

    public AmbientTemperatureData(long milliseconds, float degree) {
        this.millis = milliseconds;
        this.degree = degree;
    }

    public long getMillis() {
        return millis;
    }

    public float getDegree() {
        return degree;
    }

    @Override
    public String toString() {
        return this.getMillis() + getSeperator() + this.getDegree() + System.getProperty("line.separator");
    }
}
