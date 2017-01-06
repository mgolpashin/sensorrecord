package de.martingolpashin.sensorrecord.models;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.martingolpashin.sensorrecord.utils.FileHandler;

/**
 * Created by martin on 16.10.16.
 */
@EBean
public class Compass implements Sensor, SensorEventListener {

    private ArrayList<CompassData> data;
    private Timer timer;
    private boolean isRecording;
    private boolean isActive;
    private int interval;

    private SensorManager sensorManager;
    android.hardware.Sensor compass;

    private Context context;

    public Compass(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        this.isRecording = false;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.compass = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void record() {
        this.isRecording = true;
        this.timer = new Timer();
        final long startDate = new Date().getTime();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRecording) {
                    data.add(new CompassData(new Date().getTime() - startDate));
                }
            }
        }, 0, interval);
    }

    @Override
    public File writeToCSV(String fileName) {
        this.timer.cancel();
        File dir = FileHandler.getWritableStorageDir(this.context);

        File file = new File(dir, fileName + "_Compass.csv");

        try {
            FileWriter fw = new FileWriter(file);
            fw.write("Milliseconds;X;Y;Z;" + System.getProperty("line.separator"));
            for(CompassData entry : data) {
                fw.write(entry.toString());
            }

            fw.flush();
            fw.close();
            //Toast.makeText(context, file.getAbsolutePath() + " created", Toast.LENGTH_LONG).show();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
        if(isActive) {
            this.sensorManager.registerListener(this, this.compass, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            this.sensorManager.unregisterListener(this);
        }
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    @Override
    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isRecording() {
        return isRecording;
    }

    @Override
    public void reset() {
        this.data = new ArrayList<>();
        this.isRecording = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        android.hardware.Sensor sensor = event.sensor;

        if (sensor.getType() == android.hardware.Sensor.TYPE_MAGNETIC_FIELD) {
            //TODO Martin store values
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }
}
