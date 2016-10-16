package de.martingolpashin.sensorfusion.models;

import java.util.ArrayList;

/**
 * Created by martin on 16.10.16.
 */
public class GPS implements Sensor {

    private ArrayList<GPSData> data;
    private long interval;

    public GPS(long interval) {
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
        this.data = new ArrayList<>();
    }
}
