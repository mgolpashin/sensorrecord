package de.martingolpashin.sensorrecord.models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.martingolpashin.sensorrecord.utils.FileHandler;

/**
 * Created by martin on 16.10.16.
 */
@EBean
public class GPS implements Sensor, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private ArrayList<GPSData> data;
    private Location lastKnownLocation;
    private Timer timer;
    private boolean isRecording;
    private boolean isActive;
    private int interval;

    private Context context;

    GoogleApiClient googleApiClient;

    public GPS(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        this.isRecording = false;

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this.context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
        }
    }

    @Override
    public void record() {
        this.isRecording = true;
        this.timer = new Timer();
        final long startDate = new Date().getTime();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isRecording) {
                    data.add(new GPSData(new Date().getTime() - startDate, lastKnownLocation));
                }
            }
        }, 0, interval);
    }

    @Override
    public File writeToCSV(String fileName) {
        this.timer.cancel();
        File dir = FileHandler.getWritableStorageDir(this.context);
        File file = new File(dir, fileName + "_GPS.csv");

        try {
            FileWriter fw = new FileWriter(file);
            fw.write("Date;Latitude;Longitude;Altitude" + System.getProperty("line.separator"));
            for(GPSData entry : data) {
                fw.write(entry.getMillis() + ";" +
                        entry.getLocation().getLatitude() + ";" +
                        entry.getLocation().getLongitude() + ";" +
                        entry.getLocation().getAltitude() + ";" +
                        System.getProperty("line.separator"));
            }

            fw.flush();
            fw.close();
            //Toast.makeText(context, file.getAbsolutePath() + " created", Toast.LENGTH_LONG).show();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
        if (isActive) {
            googleApiClient.connect();
        } else {
            googleApiClient.disconnect();
        }
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(this.interval);
        //TODO check
        LocationServices.FusedLocationApi.requestLocationUpdates(this.googleApiClient, locationRequest, this);
    }

    public boolean isRecording() {
        return isRecording;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.lastKnownLocation = location;
    }

    @Override
    public void reset() {
        this.data = new ArrayList<>();
        this.isRecording = false;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
