package de.martingolpashin.sensor_record.models.sensors.ambient_temperature;

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
public class AmbientTemperature extends Sensor implements SensorEventListener {
    private SensorManager sensorManager;
    private android.hardware.Sensor degreeSensor;
    private float degree;
    private String name = "AmbientTemperature";

    public AmbientTemperature(Context context) {
        super(context, "AmbientTemperature", 100, new String[]{"Milliseconds", "Degree"});
        this.sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        this.degreeSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE);
    }

    @Override
    public void scheduleRecording() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRecording) {
                    data.add(new AmbientTemperatureData(new Date().getTime() - startDate, degree));
                }
            }
        }, 0, interval);
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
        if(isActive) {
            this.sensorManager.registerListener(this, this.degreeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            this.sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        android.hardware.Sensor sensor = event.sensor;

        if (sensor.getType() == android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE) {
            this.degree = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }
}
