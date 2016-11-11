package de.martingolpashin.sensorrecord.models;

/**
 * Created by martin on 14.10.16.
 */
public class GyroData {
    private long millis;

    public GyroData(long milliseconds) {
        this.millis = milliseconds;
    }

    public long getMillis() {
        return millis;
    }

    @Override
    public String toString() {
        return this.getMillis() + ";" /* TODO Martin write data */ + System.getProperty("line.separator");
    }
}
