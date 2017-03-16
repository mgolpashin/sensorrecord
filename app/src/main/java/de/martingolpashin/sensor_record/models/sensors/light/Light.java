package de.martingolpashin.sensor_record.models.sensors.light;

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
public class Light extends Sensor implements SensorEventListener {
    private SensorManager sensorManager;
    private android.hardware.Sensor lightSensor;
    private float illuminance;
    private String name = "Light";

    public Light(Context context) {
        super(context, "Light", 100, new String[]{"Milliseconds", "Illuminance"});
        this.sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        this.lightSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LIGHT);
    }

    @Override
    public void scheduleRecording() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRecording) {
                    data.add(new LightData(new Date().getTime() - startDate, illuminance));
                }
            }
        }, 0, interval);
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
        if(isActive) {
            this.sensorManager.registerListener(this, this.lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            this.sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        android.hardware.Sensor sensor = event.sensor;

        if (sensor.getType() == android.hardware.Sensor.TYPE_PRESSURE) {
            this.illuminance = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }
}
