package de.martingolpashin.sensor_record.models.sensors.light;

/**
 * Created by martin on 14.10.16.
 */
public class LightData {
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
        return this.getMillis() + ";" + this.getIlluminance() + ";" + System.getProperty("line.separator");
    }
}
