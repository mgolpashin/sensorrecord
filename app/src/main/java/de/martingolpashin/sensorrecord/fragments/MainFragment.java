package de.martingolpashin.sensorrecord.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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

import de.martingolpashin.sensorrecord.R;
import de.martingolpashin.sensorrecord.activities.MainActivity;
import de.martingolpashin.sensorrecord.models.Accelerometer;
import de.martingolpashin.sensorrecord.models.Compass;
import de.martingolpashin.sensorrecord.models.GPS;
import de.martingolpashin.sensorrecord.models.Gyroscope;

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {

    private static final int MIN_INTERVAL = 10;
    MainActivity activity;
    private Date _activeRecordStart;

    @ViewById
    ImageButton btn_record;
    @ViewById
    ImageButton btn_stop;

    @ViewById
    CheckBox check_gps;
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

    @ViewById
    RecyclerView file_list;

    @Bean
    GPS gps;
    @Bean
    Accelerometer accelerometer;
    @Bean
    Gyroscope gyro;
    @Bean
    Compass compass;

    //TODO add interval validation
    //use design library?

    //TODO add new files

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @AfterViews
    void init() {
        checkRecordBtnEnabled();
        this.activity = (MainActivity) getActivity();

        file_list.setHasFixedSize(true);
        file_list.setLayoutManager(new LinearLayoutManager(this.activity));
        file_list.setAdapter(this.activity.getAdapter());

        //add sensors
        this.activity.sensors = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(this.activity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.activity.sensors.add(gps);
            check_gps.setEnabled(true);
        }

        this.activity.sensors.add(accelerometer);
        check_accelerometer.setEnabled(true);
        this.activity.sensors.add(gyro);
        check_gyro.setEnabled(true);
        this.activity.sensors.add(compass);
        //TODO activate when implemented
        //check_compass.setEnabled(true);
    }

    @Click
    void btn_record() {
        if(!hasMinInterval()) {
            Toast.makeText(this.activity, "Minimum interval is " + MIN_INTERVAL, Toast.LENGTH_LONG).show();
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

        btn_record.setVisibility(View.GONE);
        btn_stop.setVisibility(View.VISIBLE);

        activity.record();
    }

    private boolean hasMinInterval() {
        return !(
                    (edit_gps.isEnabled() && (edit_gps.getText().toString().equals("") || (Integer.parseInt(edit_gps.getText().toString())) < MIN_INTERVAL)) ||
                    (edit_accelerometer.isEnabled() && (edit_accelerometer.getText().toString().equals("") || (Integer.parseInt(edit_accelerometer.getText().toString())) < MIN_INTERVAL)) ||
                    (edit_gyro.isEnabled() && (edit_gyro.getText().toString().equals("") || (Integer.parseInt(edit_gyro.getText().toString())) < MIN_INTERVAL)) ||
                    (edit_compass.isEnabled() && (edit_compass.getText().toString().equals("") || (Integer.parseInt(edit_compass.getText().toString())) < MIN_INTERVAL))
                );
    }

    @Click
    void btn_stop() {
        String fileFormat = "yyyy-MM-dd_HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(fileFormat, Locale.GERMAN);
        String fileName = format.format(_activeRecordStart);
        activity.writeCSVs(fileName);
        _activeRecordStart = null;
        activity.resetSensors();
        _resumeControls();

        btn_record.setVisibility(View.VISIBLE);
        btn_stop.setVisibility(View.GONE);
    }

    @CheckedChange
    void check_gps(boolean isChecked) {
        edit_gps.setEnabled(isChecked);
        gps.setActive(isChecked);
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

            btn_record.setEnabled(false);
        } else {
            btn_record.setEnabled(true);
        }
    }


    private void _resumeControls() {
        _enableCheckboxes(true);
        _enableIntervalControls(true);
    }
}
