package de.martingolpashin.sensor_record.models.sensors.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.androidannotations.annotations.EBean;

import java.util.Date;
import java.util.TimerTask;

import de.martingolpashin.sensor_record.SensorRecordApplication;
import de.martingolpashin.sensor_record.models.Sensor;

@EBean
public class GPS extends Sensor {
    private Location lastKnownLocation;

    public GPS(final Context context) {
        super(context, "GPS", 100, GPSData.getHeadline());

        final CheckBox checkBox = getCheckBox();
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = ((SensorRecordApplication) context.getApplicationContext()).getCurrentActivity();
                if (checkBox.isChecked() && ContextCompat.checkSelfPermission(currentActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    checkBox.setChecked(false);
                    ActivityCompat.requestPermissions(currentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        });

    }

    @Override
    public void scheduleRecording() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRecording) {
                    Date now = new Date();
                    data.add(new GPSData(now, now.getTime() - startDate, lastKnownLocation));
                }
            }
        }, 0, interval);
    }

    @Override
    public void setActive(boolean isActiveNew) {
        active = isActiveNew;
        if (active) {
            connect();
        }
    }

    private void connect() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(this.interval);
        locationRequest.setFastestInterval(this.interval);

        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                lastKnownLocation = result.getLastLocation();
            }
        };

        Activity currentActivity = ((SensorRecordApplication) context.getApplicationContext()).getCurrentActivity();

        if(ContextCompat.checkSelfPermission(currentActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
            client.requestLocationUpdates(locationRequest, callback, null);
        } else {
            getCheckBox().setChecked(false);
            ActivityCompat.requestPermissions(currentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
}
