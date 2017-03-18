package de.martingolpashin.sensor_record.models.sensors.pressure;

import de.martingolpashin.sensor_record.models.SensorData;

/**
 * Created by martin on 14.10.16.
 */
public class PressureData extends SensorData{
    private long millis;
    private float millibars;

    public PressureData(long milliseconds, float millibars) {
        this.millis = milliseconds;
        this.millibars = millibars;
    }

    public long getMillis() {
        return millis;
    }

    public float getMillibars() {
        return millibars;
    }

    @Override
    public String toString() {
        return this.getMillis() + getSeperator() + this.getMillibars() + System.getProperty("line.separator");
    }
}
