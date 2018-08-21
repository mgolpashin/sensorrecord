package de.martingolpashin.sensor_record.models.sensors.accelerometer;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.androidannotations.annotations.EBean;

import java.util.Date;
import java.util.TimerTask;

import de.martingolpashin.sensor_record.models.Sensor;

/**
 * Created by martin on 16.10.16.
 */
@EBean
public class Accelerometer extends Sensor implements SensorEventListener {
    private float x;
    private float y;
    private float z;

    private SensorManager sensorManager;
    android.hardware.Sensor accelerometer;

    public Accelerometer(Context context) {
        super(context, "Accelerometer", 10, AccelerometerData.getHeadline());
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void scheduleRecording() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRecording) {
                    Date now = new Date();
                    data.add(new AccelerometerData(now, now.getTime() - startDate, x, y, z));
                }
            }
        }, 0, interval);
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
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
