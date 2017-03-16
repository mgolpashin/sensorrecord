package de.martingolpashin.sensor_record.models;

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
public class AirPressure extends BaseSensor implements Sensor, SensorEventListener {
    private SensorManager sensorManager;
    android.hardware.Sensor pressureSensor;

    private float millibars;

    public AirPressure(Context context) {
        super(context);
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.pressureSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_PRESSURE);
    }

    @Override
    public void scheduleRecording() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRecording) {
                    data.add(new PressureData(new Date().getTime() - startDate, millibars));
                }
            }
        }, 0, interval);
    }

    @Override
    public File writeToCSV(String fileName, File dir, boolean includeDateTime) {
        this.timer.cancel();
        fileName = includeDateTime ? fileName + "_Pressure.csv" : "Pressure.csv";
        File file = new File(dir, fileName);

        try {
            FileWriter fw = new FileWriter(file);
            fw.write("Milliseconds;millibars;" + System.getProperty("line.separator"));
            for(Object obj: data) {
                PressureData entry = (PressureData) obj;
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
            this.sensorManager.registerListener(this, this.pressureSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            this.sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        android.hardware.Sensor sensor = event.sensor;

        if (sensor.getType() == android.hardware.Sensor.TYPE_PRESSURE) {
            this.millibars = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }
}
