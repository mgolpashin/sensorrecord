package de.martingolpashin.sensor_record.models.sensors.proximity;

/**
 * Created by martin on 14.10.16.
 */
public class ProximityData {
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
        return this.getMillis() + ";" + this.getCentimeters() + ";" + System.getProperty("line.separator");
    }
}
