package de.martingolpashin.sensorrecord.models;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.TimerTask;

import de.martingolpashin.sensorrecord.utils.FileHandler;

/**
 * Created by martin on 16.10.16.
 */
@EBean
public class Compass extends BaseSensor implements Sensor, SensorEventListener {
    private SensorManager sensorManager;
    android.hardware.Sensor compass;

    public Compass(Context context) {
        super(context);
        this.isRecording = false;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.compass = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void scheduleRecording() {
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
            for(Object obj : data) {
                CompassData entry = (CompassData) obj;
                fw.write(entry.toString());
            }

            fw.flush();
            fw.close();
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
