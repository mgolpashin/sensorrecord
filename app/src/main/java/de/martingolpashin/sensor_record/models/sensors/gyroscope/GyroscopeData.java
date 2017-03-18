package de.martingolpashin.sensor_record.models.sensors.gyroscope;

import de.martingolpashin.sensor_record.models.SensorData;

/**
 * Created by martin on 14.10.16.
 */
public class GyroscopeData extends SensorData{
    private long millis;
    private float x;
    private float y;
    private float z;

    public GyroscopeData(long milliseconds, float x, float y, float z) {
        this.millis = milliseconds;
        this.x = x;
        this.y = y;
        this.z = z;
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

    @Override
    public String toString() {
        return this.getMillis() + getSeperator() + this.getX() + getSeperator() + this.getY() + getSeperator() + this.getZ() + System.getProperty("line.separator");
    }
}
