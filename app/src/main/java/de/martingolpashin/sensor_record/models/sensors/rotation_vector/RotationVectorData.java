package de.martingolpashin.sensor_record.models.sensors.rotation_vector;

/**
 * Created by martin on 14.10.16.
 */
public class RotationVectorData {
    private long millis;
    private float x;
    private float y;
    private float z;
    private float cos;
    private float headingAccuracy;

    public RotationVectorData(long milliseconds, float x, float y, float z, float cos, float headingAccuracy) {
        this.millis = milliseconds;
        this.x = x;
        this.y = y;
        this.z = z;
        this.cos = cos;
        this.headingAccuracy = headingAccuracy;
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

    public float getCos() {
        return cos;
    }

    public float getHeadingAccuracy() {
        return headingAccuracy;
    }

    @Override
    public String toString() {
        return this.getMillis() + ";" + this.getX() + ";" + this.getY() + ";" + this.getZ() + ";" + this.getCos() + ";" + this.getHeadingAccuracy() + ";" + System.getProperty("line.separator");
    }
}
