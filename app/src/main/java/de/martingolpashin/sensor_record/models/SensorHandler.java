package de.martingolpashin.sensor_record.models;

import android.content.Context;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import de.martingolpashin.sensor_record.models.sensors.accelerometer.Accelerometer;
import de.martingolpashin.sensor_record.models.sensors.pressure.Pressure;
import de.martingolpashin.sensor_record.models.sensors.ambient_temperature.AmbientTemperature;
import de.martingolpashin.sensor_record.models.sensors.compass.Compass;
import de.martingolpashin.sensor_record.models.sensors.gps.GPS;
import de.martingolpashin.sensor_record.models.sensors.gravity.Gravity;
import de.martingolpashin.sensor_record.models.sensors.gyroscope.Gyroscope;
import de.martingolpashin.sensor_record.models.sensors.light.Light;
import de.martingolpashin.sensor_record.models.sensors.linear_accelerometer.LinearAccelerometer;
import de.martingolpashin.sensor_record.models.sensors.rotation_vector.RotationVector;

/**
 * Created by martin on 16.03.17.
 */

@EBean
public class SensorHandler {

    private List<Sensor> sensors;

    public SensorHandler(Context context) {
        this.sensors = new ArrayList<>();
        this.sensors.add(new GPS(context));
        this.sensors.add(new Accelerometer(context));
        this.sensors.add(new LinearAccelerometer(context));
        this.sensors.add(new Gyroscope(context));
        this.sensors.add(new Compass(context));
        this.sensors.add(new Pressure(context));
        this.sensors.add(new Light(context));
        this.sensors.add(new AmbientTemperature(context));
        this.sensors.add(new Gravity(context));
        this.sensors.add(new RotationVector(context));
    }

    public boolean hasActiveSensor() {
        for(Sensor s : sensors) {
            if(s.getCheckBox().isChecked()) {
                return true;
            }
        }
        return false;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }
}
