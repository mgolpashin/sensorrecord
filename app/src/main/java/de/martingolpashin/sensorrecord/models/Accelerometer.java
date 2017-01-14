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

/**
 * Created by martin on 16.10.16.
 */
@EBean
public class Accelerometer extends BaseSensor implements Sensor, SensorEventListener {
    private float x;
    private float y;
    private float z;

    private SensorManager sensorManager;
    android.hardware.Sensor accelerometer;

    public Accelerometer(Context context) {
        super(context);
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void scheduleRecording() {
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
    public File writeToCSV(String fileName, File dir, boolean includeDateTime) {
        this.timer.cancel();
        fileName = includeDateTime ? fileName + "_Accelerometer.csv" : "Accelerometer.csv";
        File file = new File(dir, fileName);

        try {
            FileWriter fw = new FileWriter(file);
            fw.write("Milliseconds;X;Y;Z;" + System.getProperty("line.separator"));
            for(Object obj: data) {
                AccelerometerData entry = (AccelerometerData) obj;
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
            this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            this.sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        android.hardware.Sensor sensor = event.sensor;

        if (sensor.getType() == android.hardware.Sensor.TYPE_ACCELEROMETER) {
            this.x = event.values[0];
            this.y = event.values[1];
            this.z = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }
}
