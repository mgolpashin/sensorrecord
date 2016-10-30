package de.martingolpashin.sensorrecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.martingolpashin.sensorrecord.models.GPS;
import de.martingolpashin.sensorrecord.models.Sensor;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private Date _activeRecordStart;
    private String fileFormat = "yyyy.MM.dd_HH:mm:ss";

    private ArrayList<Sensor> sensors;

    @ViewById
    Button btn_record;
    @ViewById
    Button btn_stop;

    @ViewById
    CheckBox check_gps;
    @ViewById
    EditText edit_gps;

    /*
    @ViewById
    CheckBox check_compass;
    @ViewById
    SeekBar seek_compass;
    @ViewById
    TextView interval_compass;
    @ViewById
    CheckBox check_gyro;
    @ViewById
    SeekBar seek_gyro;
    @ViewById
    TextView interval_gyro;
    @ViewById
    CheckBox check_accelerometer;
    @ViewById
    SeekBar seek_accelerometer;
    @ViewById
    TextView interval_accelerometer;
    */

    @Bean
    GPS gps;
    /*
    @Bean
    Gyro gyro;
    @Bean
    Compass compass;
    @Bean
    Accelerometer accelerometer;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void _initSensors() {
        this.sensors = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.sensors.add(gps);
            check_gps.setEnabled(true);
        }

        /*
        this.sensors.add(gyro);
        this.sensors.add(compass);
        this.sensors.add(accelerometer);
        */
    }


    @Override
    protected void onStart() {
        _initSensors();
        super.onStart();
    }

    @Click
    void btn_record() {
        if(check_gps.isChecked()) {
            gps.setInterval(Integer.parseInt(edit_gps.getText().toString()));
        }
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

    private void writeCSVs(String fileName) {
        for(Sensor s : sensors) {
            if(s.isRecording()) {
                s.writeToCSV(fileName);
                s.setRecording(false);
            }
        }
    }

    private void _resetSensors() {
        for(Sensor s : this.sensors) {
            s.reset();
        }
    }

    void _record() {
        for(Sensor s : sensors) {
            if(s.isActive()) {
                s.record();
            }
        }
    }

    @CheckedChange
    void check_gps(CheckBox check_gps, boolean isChecked) {
        edit_gps.setEnabled(isChecked);
        gps.setActive(isChecked);
        _checkRecordBtnEnabled();
    }

    /*
    @CheckedChange
    void check_compass(CheckBox check_compass, bo434olean isChecked) {
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
    */

    private void _checkRecordBtnEnabled() {
        if (/*!check_accelerometer.isChecked() &&
                !check_gyro.isChecked() &&
                !check_compass.isChecked() && */
                !check_gps.isChecked()) {

            btn_record.setEnabled(false);
        } else {
            btn_record.setEnabled(true);
        }
    }

    private void _enableControls(boolean enabled) {
        _enableCheckboxes(enabled);
        _enableIntervalControls(enabled);
    }

    private void _resumeControls() {
        _enableCheckboxes(true);
        _enableIntervalControls(true);
        /*
        if(check_compass.isChecked()) {
            seek_compass.setEnabled(true);
        }
        if(check_accelerometer.isChecked()) {
            seek_accelerometer.setEnabled(true);
        }
        if(check_gyro.isChecked()) {
            seek_gyro.setEnabled(true);
        }
        */
    }

    private void _enableCheckboxes(boolean enabled) {
        check_gps.setEnabled(enabled);
        /*
        check_accelerometer.setEnabled(enabled);
        check_compass.setEnabled(enabled);
        check_gyro.setEnabled(enabled);
        */
    }

    private void _enableIntervalControls(boolean enabled) {
        edit_gps.setEnabled(enabled);
        /*
        seek_accelerometer.setEnabled(enabled);
        seek_compass.setEnabled(enabled);
        seek_gyro.setEnabled(enabled);
        */
    }
}