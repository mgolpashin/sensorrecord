package de.martingolpashin.sensor_record.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.martingolpashin.sensor_record.R;
import de.martingolpashin.sensor_record.activities.MainActivity;
import de.martingolpashin.sensor_record.models.Accelerometer;
import de.martingolpashin.sensor_record.models.Compass;
import de.martingolpashin.sensor_record.models.GPS;
import de.martingolpashin.sensor_record.models.Gyroscope;

@EFragment(R.layout.fragment_sensors)
public class SensorFragment extends Fragment {
    private boolean isRecordingEnabled = false;
    private static final int MIN_INTERVAL = 10;
    MainActivity activity;
    private Date _activeRecordStart;
    private boolean isRecording = false;

    @ViewById
    public FloatingActionButton btn_record_stop;

    @ViewById
    public CheckBox check_gps;
    @ViewById
    EditText edit_gps;

    @ViewById
    CheckBox check_accelerometer;
    @ViewById
    EditText edit_accelerometer;

    @ViewById
    CheckBox check_gyro;
    @ViewById
    EditText edit_gyro;

    @ViewById
    CheckBox check_compass;
    @ViewById
    EditText edit_compass;

    @Bean
    GPS gps;
    @Bean
    Accelerometer accelerometer;
    @Bean
    Gyroscope gyro;
    @Bean
    Compass compass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensors, container, false);
    }

    @AfterViews
    void init() {
        this.activity = (MainActivity) getActivity();

        //add sensors
        this.activity.sensors = new ArrayList<>();

        this.activity.sensors.add(gps);
        check_gps.setEnabled(true);
        this.activity.sensors.add(accelerometer);
        check_accelerometer.setEnabled(true);
        this.activity.sensors.add(gyro);
        check_gyro.setEnabled(true);
        this.activity.sensors.add(compass);
        check_compass.setEnabled(true);

        edit_gps.setText("100");
        edit_accelerometer.setText("10");
        edit_gyro.setText("10");
        edit_compass.setText("100");
    }

    @Click
    void btn_record_stop() {
        if(isRecording) {
            onStopClicked();
        } else {
            onRecordClicked();
        }
    }

    public void onRecordClicked() {
        if(!this.isRecordingEnabled) {
            Toast.makeText(this.activity, "No active Sensor", Toast.LENGTH_LONG).show();
            return;
        }

        if(!hasMinInterval()) {
            Toast.makeText(this.activity, "Minimum interval is " + MIN_INTERVAL, Toast.LENGTH_LONG).show();
            return;
        }

        if(ContextCompat.checkSelfPermission(this.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, this.activity.PERMISSION_WRITE_EXTERNAL_STORAGE);
            return;
        }

        if(edit_gps.isEnabled() && !edit_gps.getText().toString().equals("")) {
            gps.setInterval(Integer.parseInt(edit_gps.getText().toString()));
        }
        if(edit_accelerometer.isEnabled() && !edit_accelerometer.getText().toString().equals("")) {
            accelerometer.setInterval(Integer.parseInt(edit_accelerometer.getText().toString()));
        }
        if(edit_gyro.isEnabled() && !edit_gyro.getText().toString().equals("")) {
            gyro.setInterval(Integer.parseInt(edit_gyro.getText().toString()));
        }
        if(edit_compass.isEnabled() && !edit_compass.getText().toString().equals("")) {
            compass.setInterval(Integer.parseInt(edit_compass.getText().toString()));
        }

        _activeRecordStart = new Date();
        _enableControls(false);

        activity.record();
        isRecording = true;
        btn_record_stop.setImageResource(R.drawable.ic_stop_white_24dp);
    }

    private boolean hasMinInterval() {
        return !(
                    (edit_gps.isEnabled() && (edit_gps.getText().toString().equals("") || (Integer.parseInt(edit_gps.getText().toString())) < MIN_INTERVAL)) ||
                    (edit_accelerometer.isEnabled() && (edit_accelerometer.getText().toString().equals("") || (Integer.parseInt(edit_accelerometer.getText().toString())) < MIN_INTERVAL)) ||
                    (edit_gyro.isEnabled() && (edit_gyro.getText().toString().equals("") || (Integer.parseInt(edit_gyro.getText().toString())) < MIN_INTERVAL)) ||
                    (edit_compass.isEnabled() && (edit_compass.getText().toString().equals("") || (Integer.parseInt(edit_compass.getText().toString())) < MIN_INTERVAL))
                );
    }

    void onStopClicked() {
        String fileFormat = "yyyy-MM-dd_HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(fileFormat, Locale.GERMAN);
        String fileName = format.format(_activeRecordStart);
        activity.writeCSVs(fileName);
        _activeRecordStart = null;
        activity.resetSensors();
        _resumeControls();

        isRecording = false;
        btn_record_stop.setImageResource(R.drawable.ic_fiber_manual_record_white_24dp);
    }

    @Click
    public void check_gps() {
        if(check_gps.isChecked() && ContextCompat.checkSelfPermission(this.activity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            check_gps.setChecked(false);
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.activity.PERMISSION_ACCESS_FINE_LOCATION);
            return;
        }

        edit_gps.setEnabled(check_gps.isChecked());
        gps.setActive(check_gps.isChecked());
        checkRecordBtnEnabled();
    }

    @CheckedChange
    void check_accelerometer(boolean isChecked) {
        edit_accelerometer.setEnabled(isChecked);
        accelerometer.setActive(isChecked);
        checkRecordBtnEnabled();
    }

    @CheckedChange
    void check_gyro(boolean isChecked) {
        edit_gyro.setEnabled(isChecked);
        gyro.setActive(isChecked);
        checkRecordBtnEnabled();
    }

    @CheckedChange
    void check_compass(boolean isChecked) {
        edit_compass.setEnabled(isChecked);
        compass.setActive(isChecked);
        checkRecordBtnEnabled();
    }

    private void _enableControls(boolean enabled) {
        _enableCheckboxes(enabled);
        _enableIntervalControls(enabled);
    }

    private void _enableCheckboxes(boolean enabled) {
        check_gps.setEnabled(enabled);
        check_accelerometer.setEnabled(enabled);
        check_gyro.setEnabled(enabled);
        check_compass.setEnabled(enabled);
    }

    private void _enableIntervalControls(boolean enabled) {
        if (check_gps.isChecked()) {
            edit_gps.setEnabled(enabled);
        }
        if (check_accelerometer.isChecked()) {
            edit_accelerometer.setEnabled(enabled);
        }
        if(check_gyro.isChecked()) {
            edit_gyro.setEnabled(enabled);
        }
        if(check_compass.isChecked()) {
            edit_compass.setEnabled(enabled);
        }
    }

    private void checkRecordBtnEnabled() {
        if (!check_compass.isChecked() &&
            !check_accelerometer.isChecked() &&
            !check_gps.isChecked() &&
            !check_gyro.isChecked()) {

            this.isRecordingEnabled = false;
        } else {
            this.isRecordingEnabled = true;
        }
    }

    private void _resumeControls() {
        _enableCheckboxes(true);
        _enableIntervalControls(true);
    }
}
