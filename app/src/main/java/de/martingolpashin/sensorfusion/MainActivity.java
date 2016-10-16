package de.martingolpashin.sensorfusion;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.martingolpashin.sensorfusion.models.Accelerometer;
import de.martingolpashin.sensorfusion.models.Compass;
import de.martingolpashin.sensorfusion.models.GPS;
import de.martingolpashin.sensorfusion.models.Gyro;
import de.martingolpashin.sensorfusion.models.Sensor;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Date _activeRecordStart;
    private String fileFormat = "yyyy.MM.dd_HH:mm:ss";
    private long DEFAULT_INTERVAL = 100;

    private ArrayList<Sensor> sensors;

    GoogleApiClient googleApiClient;

    @ViewById
    Button btn_record;

    @ViewById
    Button btn_stop;

    @ViewById
    CheckBox check_gps;

    @ViewById
    SeekBar seek_gps;

    @ViewById
    CheckBox check_compass;

    @ViewById
    SeekBar seek_compass;

    @ViewById
    CheckBox check_gyro;

    @ViewById
    SeekBar seek_gyro;

    @ViewById
    CheckBox check_accelerometer;

    @ViewById
    SeekBar seek_accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _initGoogleAPIClient();
        _initSensors();
    }

    private void _initSensors() {
        this.sensors = new ArrayList<>();
        this.sensors.add(new Accelerometer(DEFAULT_INTERVAL));
        this.sensors.add(new GPS(DEFAULT_INTERVAL));
        this.sensors.add(new Gyro(DEFAULT_INTERVAL));
        this.sensors.add(new Compass(DEFAULT_INTERVAL));
    }


    @Override
    protected void onStart() {
        _enableSeekBars(false);
        super.onStart();
    }

    @Click
    void btn_record() {
        _activeRecordStart = new Date();

        _enableControls(false);

        btn_record.setVisibility(View.GONE);
        btn_stop.setVisibility(View.VISIBLE);

        _record();
    }

    @Click
    void btn_stop() {
        //stop recording
        SimpleDateFormat format = new SimpleDateFormat(fileFormat);
        String fileName = format.format(_activeRecordStart);

        writeCSVs(fileName);

        _activeRecordStart = null;

        _resetSensors();

        _resumeControls();

        btn_record.setVisibility(View.VISIBLE);
        btn_stop.setVisibility(View.GONE);
    }

    private void _resetSensors() {
        for(Sensor s : this.sensors) {
            s.init();
        }
    }

    void _record() {
        for(Sensor s : sensors) {
            if(s.getIsActive()) {
                s.record();
            }
        }
    }

    @CheckedChange
    void check_gps(CheckBox check_gps, boolean isChecked) {
        if(isChecked) {
            googleApiClient.connect();
        } else {
            googleApiClient.disconnect();
        }

        seek_gps.setEnabled(isChecked);
        _checkRecordBtnEnabled();
    }

    @CheckedChange
    void check_compass(CheckBox check_compass, boolean isChecked) {
        //TODO set isProperty of the corresponding sensor from list of sensors;
        seek_compass.setEnabled(isChecked);
        _checkRecordBtnEnabled();
    }

    @CheckedChange
    void check_gyro(CheckBox check_gyro, boolean isChecked) {
        seek_gyro.setEnabled(isChecked);
        _checkRecordBtnEnabled();
    }

    @CheckedChange
    void check_accelerometer(CheckBox check_accelerometer, boolean isChecked) {
        seek_accelerometer.setEnabled(isChecked);
        _checkRecordBtnEnabled();
    }

    private void _checkRecordBtnEnabled() {
        if (!check_accelerometer.isChecked() &&
                !check_gyro.isChecked() &&
                !check_compass.isChecked() &&
                !check_gps.isChecked()) {

            btn_record.setEnabled(false);
        } else {
            btn_record.setEnabled(true);
        }
    }

    private void _enableControls(boolean enabled) {
        _enableCheckboxes(enabled);
        _enableSeekBars(enabled);
    }

    private void _resumeControls() {
        _enableCheckboxes(true);
        if(check_gps.isChecked()) {
            seek_gps.setEnabled(true);
        }
        if(check_compass.isChecked()) {
            seek_compass.setEnabled(true);
        }
        if(check_accelerometer.isChecked()) {
            seek_accelerometer.setEnabled(true);
        }
        if(check_gyro.isChecked()) {
            seek_gyro.setEnabled(true);
        }
    }

    private void _enableCheckboxes(boolean enabled) {
        check_gps.setEnabled(enabled);
        check_accelerometer.setEnabled(enabled);
        check_compass.setEnabled(enabled);
        check_gyro.setEnabled(enabled);
    }

    private void _enableSeekBars(boolean enabled) {
        seek_accelerometer.setEnabled(enabled);
        seek_compass.setEnabled(enabled);
        seek_gps.setEnabled(enabled);
        seek_gyro.setEnabled(enabled);
    }

    private void writeCSVs(String fileName) {
        //TODO Martin persist to filesystem
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
        Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();

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
}