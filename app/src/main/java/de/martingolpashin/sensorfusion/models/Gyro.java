package de.martingolpashin.sensorfusion.models;

import java.util.ArrayList;

/**
 * Created by martin on 16.10.16.
 */
public class Gyro implements Sensor {

    private ArrayList<GyroData> data;
    private long inverval;
    private boolean isActive;

    public Gyro(long interval) {
        init();
        this.isActive = false;
        this.inverval = interval;
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

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean getIsActive() {
        return this.isActive;
    }
}
