package de.martingolpashin.sensorrecord.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.EActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.martingolpashin.sensorrecord.R;
import de.martingolpashin.sensorrecord.adapter.FileAdapter;
import de.martingolpashin.sensorrecord.models.Sensor;
import de.martingolpashin.sensorrecord.utils.FileHandler;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity{
    private List<File> files;
    public ArrayList<Sensor> sensors;

    RecyclerView.LayoutManager layoutManager;
    FileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.files = new ArrayList<>(Arrays.asList(FileHandler.getWritableStorageDir(this).listFiles()));
        Collections.sort(this.files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName()) * -1;
            }
        });
        this.adapter = new FileAdapter(this.files);
    }

    //TODO Martin setMinInterval

    public void writeCSVs(String fileName) {
        for(Sensor s : sensors) {
            if(s.isRecording()) {
                File file = s.writeToCSV(fileName);
                this.adapter.add(file);
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

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public List<File> getFiles() {
        return files;
    }

    public int compare(File o1, File o2) {
        String name1 = o1.getName();
        String name2 = o2.getName();

        int res = String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
        if (res == 0) {
            res = name1.compareTo(name2);
        }
        return res;
    }
}