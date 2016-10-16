package de.martingolpashin.sensorfusion.models;

import java.util.ArrayList;

/**
 * Created by martin on 16.10.16.
 */
public class Accelerometer implements Sensor{
    private ArrayList<AccelerometerData> accelerometerData;
    private long interval;

    public Accelerometer(long interval) {
        init();
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
}
