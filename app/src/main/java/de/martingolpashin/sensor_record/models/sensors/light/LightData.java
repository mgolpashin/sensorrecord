package de.martingolpashin.sensor_record.models.sensors.light;

import de.martingolpashin.sensor_record.models.SensorData;

/**
 * Created by martin on 14.10.16.
 */
public class LightData extends SensorData {
    private long millis;
    private float illuminance;

    public LightData(long milliseconds, float illuminance) {
        this.millis = milliseconds;
        this.illuminance = illuminance;
    }

    public long getMillis() {
        return millis;
    }

    public float getIlluminance() {
        return illuminance;
    }

    @Override
    public String toString() {
        return this.getMillis() + getSeperator() + this.getIlluminance() + System.getProperty("line.separator");
    }
}
