package de.martingolpashin.sensor_record.models.sensors.gyroscope;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.androidannotations.annotations.EBean;

import java.util.Date;
import java.util.TimerTask;

import de.martingolpashin.sensor_record.models.Sensor;

@EBean
public class Gyroscope extends Sensor implements SensorEventListener {
    private float x;
    private float y;
    private float z;

    private SensorManager sensorManager;
    private android.hardware.Sensor gyroscope;

    public Gyroscope(Context context) {
        super(context, "Gyroscope", 10, GyroscopeData.getHeadline());
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.gyroscope = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void scheduleRecording() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRecording) {
                    Date now = new Date();
                    data.add(new GyroscopeData(now, now.getTime() - startDate, x, y, z));
                }
            }
        }, 0, interval);
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
        if(isActive) {
            this.sensorManager.registerListener(this, this.gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            this.sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        android.hardware.Sensor sensor = event.sensor;

        if (sensor.getType() == android.hardware.Sensor.TYPE_GYROSCOPE) {
            this.x = event.values[0];
            this.y = event.values[1];
            this.z = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }
}
