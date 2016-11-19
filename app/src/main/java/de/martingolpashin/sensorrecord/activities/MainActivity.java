package de.martingolpashin.sensorrecord.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.EActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.martingolpashin.sensorrecord.R;
import de.martingolpashin.sensorrecord.models.Sensor;
import de.martingolpashin.sensorrecord.utils.FileHandler;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity{
    private List<File> files;
    public ArrayList<Sensor> sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.files = new ArrayList<>(Arrays.asList(FileHandler.getWritableStorageDir(this).listFiles()));
    }

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

    public List<File> getFiles() {
        return files;
    }
}