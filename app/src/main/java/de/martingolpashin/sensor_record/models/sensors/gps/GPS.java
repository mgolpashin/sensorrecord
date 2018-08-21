package de.martingolpashin.sensor_record.models.sensors.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.androidannotations.annotations.EBean;

import java.util.Date;
import java.util.TimerTask;

import de.martingolpashin.sensor_record.SensorRecordApplication;
import de.martingolpashin.sensor_record.models.Sensor;

@EBean
public class GPS extends Sensor implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private Location lastKnownLocation;
    GoogleApiClient googleApiClient;

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


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
        }
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
            googleApiClient.connect();
        } else {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(this.interval);
        Activity currentActivity = ((SensorRecordApplication) context.getApplicationContext()).getCurrentActivity();
        if(ContextCompat.checkSelfPermission(currentActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(this.googleApiClient, locationRequest, this);
        } else {
            getCheckBox().setChecked(false);
            ActivityCompat.requestPermissions(currentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        this.lastKnownLocation = location;
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}
