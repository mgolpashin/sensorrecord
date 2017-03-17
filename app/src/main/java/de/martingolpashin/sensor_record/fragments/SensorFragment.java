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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.martingolpashin.sensor_record.R;
import de.martingolpashin.sensor_record.activities.MainActivity;
import de.martingolpashin.sensor_record.models.Sensor;

@EFragment(R.layout.fragment_sensors)
public class SensorFragment extends Fragment {
    private static final int MIN_INTERVAL = 10;
    MainActivity activity;
    private Date _activeRecordStart;
    private boolean isRecording = false;

    @ViewById
    LinearLayout layout_sensors;

    @ViewById
    public FloatingActionButton btn_record_stop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensors, container, false);
    }

    @AfterViews
    void init() {
        this.activity = (MainActivity) getActivity();
        for(Sensor s : activity.getSensorHandler().getSensors()) {
            layout_sensors.addView(s.getView());
        }
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
        if(!activity.getSensorHandler().hasActiveSensor()) {
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

        for(Sensor s : activity.getSensorHandler().getSensors()) {
            if(s.getEditText().isEnabled() && !s.getEditText().getText().toString().equals("")) {
                s.setInterval(Integer.parseInt(s.getEditText().getText().toString()));
            }
        }

        _activeRecordStart = new Date();
        _enableControls(false);

        activity.record();
        isRecording = true;
        btn_record_stop.setImageResource(R.drawable.ic_stop_white_24dp);
    }

    private boolean hasMinInterval() {
        boolean hasMinInterval = true;
        for(Sensor s : activity.getSensorHandler().getSensors()) {
            EditText editText = s.getEditText();
            hasMinInterval = hasMinInterval && !(editText.isEnabled() && editText.getText().toString().equals("") || (Integer.parseInt(editText.getText().toString()) < MIN_INTERVAL));
        }
        return hasMinInterval;
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

    private void _enableControls(boolean enabled) {
        _enableCheckboxes(enabled);
        _enableIntervalControls(enabled);
    }

    private void _enableCheckboxes(boolean enabled) {
        for(Sensor s : activity.getSensorHandler().getSensors()) {
            s.getCheckBox().setEnabled(enabled);
        }
    }

    private void _enableIntervalControls(boolean enabled) {
        for(Sensor s : activity.getSensorHandler().getSensors()) {
            if(s.getCheckBox().isChecked()) {
                s.getEditText().setEnabled(enabled);
            }
        }
    }

    private void _resumeControls() {
        _enableCheckboxes(true);
        _enableIntervalControls(true);
    }
}
