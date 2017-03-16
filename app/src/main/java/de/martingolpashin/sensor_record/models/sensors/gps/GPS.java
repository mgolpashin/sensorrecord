package de.martingolpashin.sensor_record.models.sensors.gps;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.androidannotations.annotations.EBean;

import java.util.Date;
import java.util.TimerTask;

import de.martingolpashin.sensor_record.models.Sensor;

/**
 * Created by martin on 16.10.16.
 */
@EBean
public class GPS extends Sensor implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private Location lastKnownLocation;
    GoogleApiClient googleApiClient;

    public GPS(Context context) {
        super(context, "GPS", 100, new String[]{"Milliseconds", "Latitude", "Longitude", "Altitude"});

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
                    data.add(new GPSData(new Date().getTime() - startDate, lastKnownLocation));
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
        //TODO add permission check
        LocationServices.FusedLocationApi.requestLocationUpdates(this.googleApiClient, locationRequest, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        this.lastKnownLocation = location;
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
}
