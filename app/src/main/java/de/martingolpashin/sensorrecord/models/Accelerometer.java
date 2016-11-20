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
public class Accelerometer implements Sensor, SensorEventListener {

    private ArrayList<AccelerometerData> data;
    private Timer timer;
    private boolean isRecording;
    private boolean isActive;
    private int interval;

    private float x;
    private float y;
    private float z;

    private SensorManager sensorManager;
    android.hardware.Sensor accelerometer;

    private Context context;

    public Accelerometer(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        this.isRecording = false;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION);
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
                    data.add(new AccelerometerData(new Date().getTime() - startDate, x, y, z));
                }
            }
        }, 0, interval);
    }

    @Override
    public File writeToCSV(String fileName) {
        this.timer.cancel();
        File dir = FileHandler.getWritableStorageDir(this.context);
        File file = new File(dir, fileName + "_Accelerometer.csv");

        try {
            FileWriter fw = new FileWriter(file);
            fw.write("Date;X;Y;Z;" + System.getProperty("line.separator"));
            for(AccelerometerData entry : data) {
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
            this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
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

        if (sensor.getType() == android.hardware.Sensor.TYPE_LINEAR_ACCELERATION) {
            this.x = event.values[0];
            this.y = event.values[1];
            this.z = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }
}
