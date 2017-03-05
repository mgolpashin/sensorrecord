package de.martingolpashin.sensor_record.models;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by martin on 16.10.16.
 */
@EBean
public class GPS extends BaseSensor implements Sensor, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private Location lastKnownLocation;
    GoogleApiClient googleApiClient;

    public GPS(Context context) {
        super(context);

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
    public File writeToCSV(String fileName, File dir, boolean includeDateTime) {
        this.timer.cancel();
        fileName = includeDateTime ? fileName + "_GPS.csv" : "GPS.csv";
        File file = new File(dir, fileName);

        try {
            FileWriter fw = new FileWriter(file);
            fw.write("Milliseconds;Latitude;Longitude;Altitude" + System.getProperty("line.separator"));
            for(Object obj : data) {
                GPSData entry = (GPSData) obj;
                fw.write(entry.getMillis() + ";" +
                        entry.getLocation().getLatitude() + ";" +
                        entry.getLocation().getLongitude() + ";" +
                        entry.getLocation().getAltitude() + ";" + //TODO Altitude is 0
                        System.getProperty("line.separator"));
            }

            fw.flush();
            fw.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Fehler beim Speichern der Datei", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void setActive(boolean isActiveNew) {
        isActive = isActiveNew;
        if (isActive) {
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
