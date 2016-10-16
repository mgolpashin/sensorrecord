package de.martingolpashin.sensorfusion.models;

/**
 * Created by martin on 16.10.16.
 */
public interface Sensor {
    void record();
    void writeToCSV();
    void init();
}
