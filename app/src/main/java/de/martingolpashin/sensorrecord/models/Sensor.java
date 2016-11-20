package de.martingolpashin.sensorrecord.models;

import java.io.File;

/**
 * Created by martin on 16.10.16.
 */
public interface Sensor {
    void record();
    File writeToCSV(String fileName);
    void reset();
    void setActive(boolean isActive);
    boolean isActive();
    void setRecording(boolean isRecording);
    boolean isRecording();
}
