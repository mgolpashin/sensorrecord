package de.martingolpashin.sensorrecord.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;

import de.martingolpashin.sensorrecord.R;
import de.martingolpashin.sensorrecord.models.Sensor;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity{

    public ArrayList<Sensor> sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //TODO Martin save state when rotating device
    //TODO Martin fix bug when recording multiple times
    //TODO Martin setMinInterval

    public void writeCSVs(String fileName) {
        for(Sensor s : sensors) {
            if(s.isRecording()) {
                s.writeToCSV(fileName);
                s.setRecording(false);
            }
        }
    }

    public void resetSensors() {
        for(Sensor s : this.sensors) {
            s.reset();
        }
    }

    public void record() {
        for(Sensor s : sensors) {
            if(s.isActive()) {
                s.record();
            }
        }
    }
}