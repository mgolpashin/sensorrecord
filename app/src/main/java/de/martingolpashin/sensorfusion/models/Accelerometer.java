package de.martingolpashin.sensorfusion.models;

import java.util.ArrayList;

/**
 * Created by martin on 16.10.16.
 */
public class Accelerometer implements Sensor{
    private ArrayList<AccelerometerData> accelerometerData;
    private long interval;
    private boolean isActive;

    public Accelerometer(long interval) {
        init();
        this.isActive = false;
        this.interval = interval;
    }

    @Override
    public void record() {

    }

    @Override
    public void writeToCSV() {

    }

    @Override
    public void init() {
        this.accelerometerData = new ArrayList<>();
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean getIsActive() {
        return this.isActive;
    }
}
