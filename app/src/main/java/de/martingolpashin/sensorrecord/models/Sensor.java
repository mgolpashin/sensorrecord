package de.martingolpashin.sensorrecord.models;

/**
 * Created by martin on 16.10.16.
 */
public interface Sensor {
    void record();
    void writeToCSV(String fileName);
    void reset();
    void setActive(boolean isActive);
    boolean isActive();
    void setRecording(boolean isRecording);
    boolean isRecording();
}
