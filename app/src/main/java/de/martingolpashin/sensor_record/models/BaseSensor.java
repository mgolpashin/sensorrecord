package de.martingolpashin.sensor_record.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

/**
 * Created by martin on 16.10.16.
 */
public class BaseSensor {
    public Context context;
    public boolean isActive;
    public int interval;
    public boolean isRecording;
    public long startDate;
    public Timer timer;
    public ArrayList data;

    public BaseSensor(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        this.isRecording = false;
    }

    final public void record() {
        this.isRecording = true;
        this.startDate = new Date().getTime();
        this.timer = new Timer();
        scheduleRecording();
    }

    void scheduleRecording() {}

    public final void reset() {
        this.data = new ArrayList<>();
        this.isRecording = false;
    }

    final public boolean isActive() {
        return this.isActive;
    }

    final public void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    final public boolean isRecording() {
        return isRecording;
    }

    final public void setInterval(int interval) {
        this.interval = interval;
    }

    final public int getInterval() {
        return interval;
    }
}
