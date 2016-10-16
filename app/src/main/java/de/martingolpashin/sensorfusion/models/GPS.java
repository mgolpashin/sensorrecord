package de.martingolpashin.sensorfusion.models;

import android.widget.CheckBox;
import android.widget.SeekBar;

import java.util.ArrayList;

/**
 * Created by martin on 16.10.16.
 */
public class GPS implements Sensor {

    private ArrayList<GPSData> data;
    private boolean isActive;

    private CheckBox checkBox;
    private SeekBar seekBar;

    public GPS(CheckBox checkBox, SeekBar seekBar) {
        init();
        this.isActive = false;

        this.checkBox = checkBox;
        this.seekBar = seekBar;
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
