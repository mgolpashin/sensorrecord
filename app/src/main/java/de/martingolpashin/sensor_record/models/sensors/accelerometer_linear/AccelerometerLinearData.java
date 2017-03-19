package de.martingolpashin.sensor_record.models.sensors.accelerometer_linear;

import de.martingolpashin.sensor_record.models.SensorData;

public class AccelerometerLinearData extends SensorData{
    private long millis;
    private float x;
    private float y;
    private float z;

    public AccelerometerLinearData(long milliseconds, float x, float y, float z) {
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
