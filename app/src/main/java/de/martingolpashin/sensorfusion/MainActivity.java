package de.martingolpashin.sensorfusion;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.martingolpashin.sensorfusion.models.Sensor;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Date _activeRecordStart;
    private String fileFormat = "yyyy.MM.dd_HH:mm:ss";

    private ArrayList<Sensor> sensors = new ArrayList<>();

    GoogleApiClient googleApiClient;

    @ViewById
    Button btn_record;

    @ViewById
    Button btn_stop;

    @ViewById
    CheckBox check_gps;

    @ViewById
    SeekBar seek_gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _initGoogleAPIClient();

        seek_gps.setClickable(false);
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Click
    void btn_record() {
        _activeRecordStart = new Date();

        btn_record.setVisibility(View.INVISIBLE);
        btn_stop.setVisibility(View.VISIBLE);
    }

    @Click
    void btn_stop() {
        //stop recording
        SimpleDateFormat format = new SimpleDateFormat(fileFormat);

        String fileName = format.format(_activeRecordStart);
        writeCSVs(fileName);

        _activeRecordStart = null;

        btn_record.setVisibility(View.VISIBLE);
        btn_stop.setVisibility(View.INVISIBLE);
    }

    private void _initGoogleAPIClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //TODO make user control frequency to get lastLocation
        /*
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
         */
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                //gpsRows.add(new GPSData(_calcMillis(), lastLocation));
            }
        }
    }

    private void registerSensor() {
        //TODO implement
    }

    private void unregisterSensor() {
        //TODO implement
    }

    private void lockSensors() {
        //TODO implement
    }

    private void unlockSensors() {
        //TODO implement
    }

    @Override
    public void onConnectionSuspended(int i) {
        //TODO implement
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //TODO implement
    }

    private long _calcMillis() {
        return new Date().getTime() - _activeRecordStart.getTime();
    }

    private void writeCSVs(String fileName) {
        //TODO persist records to fileSystem
        for(Sensor s : sensors) {

        }
    }
}