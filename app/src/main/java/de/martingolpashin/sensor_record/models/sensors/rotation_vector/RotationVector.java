package de.martingolpashin.sensor_record.models.sensors.rotation_vector;

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
public class RotationVector extends Sensor implements SensorEventListener {
    private SensorManager sensorManager;
    private android.hardware.Sensor rotationVectorSensor;
    private float x;
    private float y;
    private float z;
    private float cos;
    private float headingAccuracy;
    private String name = "RotationVector";

    public RotationVector(Context context) {
        super(context, "RotationVector", 100, new String[]{"Milliseconds", "X", "Y", "Z", "cos", "headingAccuracy"});
        this.sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        this.rotationVectorSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    public void scheduleRecording() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRecording) {
                    data.add(new RotationVectorData(new Date().getTime() - startDate, x, y, z, cos, headingAccuracy));
                }
            }
        }, 0, interval);
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
        if(isActive) {
            this.sensorManager.registerListener(this, this.rotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            this.sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        android.hardware.Sensor sensor = event.sensor;

        if (sensor.getType() == android.hardware.Sensor.TYPE_ROTATION_VECTOR) {
            this.x = event.values[0];
            this.y = event.values[1];
            this.z = event.values[2];
            this.cos = event.values[3];
            this.headingAccuracy = event.values[4];
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }
}
