package de.martingolpashin.sensorrecord.models;

import java.io.File;

/**
 * Created by martin on 07.01.17.
 */
public interface Sensor {
    File writeToCSV(String fileName, File dir);
    void scheduleRecording();
    void reset();
    void record();

    void setActive(boolean isActive);
    boolean isActive();
    void setRecording(boolean isRecording);
    boolean isRecording();
    int getInterval();
}
