package de.martingolpashin.sensor_record.models.sensors.pressure;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.androidannotations.annotations.EBean;

import java.util.Date;
import java.util.TimerTask;

import de.martingolpashin.sensor_record.models.Sensor;

@EBean
public class Pressure extends Sensor implements SensorEventListener {
    private SensorManager sensorManager;
    android.hardware.Sensor pressureSensor;

    private float millibars;

    public Pressure(Context context) {
        super(context, "Pressure", 100, PressureData.getHeadline());
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.pressureSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_PRESSURE);
    }

    @Override
    public void scheduleRecording() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRecording) {
                    Date now = new Date();
                    data.add(new PressureData(now, now.getTime() - startDate, millibars));
                }
            }
        }, 0, interval);
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
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
