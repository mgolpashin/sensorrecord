package de.martingolpashin.sensorrecord.models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
        this.timer = new Timer();
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.compass = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void record() {
        this.isRecording = true;
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
    public void writeToCSV(String fileName) {
        this.timer.cancel();
        File dir = isExternalStorageWritable() && ContextCompat.checkSelfPermission(this.context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ? getExternalStorageDir() : getInternalStorageDir();

        File file = new File(dir, fileName + "_Compass.csv");

        try {
            FileWriter fw = new FileWriter(file);
            fw.write("Date;X;Y;Z;" + System.getProperty("line.separator"));
            for(CompassData entry : data) {
                fw.write(entry.toString());
            }

            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
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

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private File getExternalStorageDir() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "SensorRecord" + File.separator);
        if(!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }

    private File getInternalStorageDir() {
        File dir = new File(context.getFilesDir() + File.separator + "SensorRecord" + File.separator);
        if(!dir.exists()) {
            dir.mkdir();
        }

        return dir;
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
