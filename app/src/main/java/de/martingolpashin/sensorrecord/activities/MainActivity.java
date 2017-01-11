package de.martingolpashin.sensorrecord.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.androidannotations.annotations.EActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.martingolpashin.sensorrecord.R;
import de.martingolpashin.sensorrecord.adapter.FileAdapter;
import de.martingolpashin.sensorrecord.fragments.MainFragment;
import de.martingolpashin.sensorrecord.fragments.MainFragment_;
import de.martingolpashin.sensorrecord.models.Sensor;
import de.martingolpashin.sensorrecord.utils.FileHandler;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity{
    public final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    public final int PERMISSION_WRITE_EXTERNAL_STORAGE = 2;

    private List<File> files;
    public ArrayList<Sensor> sensors;

    MainFragment mainFragment;

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
        this.mainFragment = new MainFragment_();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_main, this.mainFragment)
                .commit();
    }

    public void writeCSVs(String fileName) {
        File dir = FileHandler.getWritableStorageDir(this);
        boolean saveDir = multipleSensorsRecording();
        if(saveDir) {
            dir = new File(dir + File.separator + fileName);
            if(!dir.exists()) {
                dir.mkdir();
            }
        }

        for(Sensor s : sensors) {
            if(s.isRecording()) {
                File file = s.writeToCSV(fileName, dir);
                if(file != null && !saveDir) {
                    this.adapter.add(file);
                }
                s.setRecording(false);
            }
        }

        if(saveDir) {
            this.adapter.add(dir);
        }
    }

    private boolean multipleSensorsRecording() {
        int count = 0;
        for(Sensor s : sensors) {
            if(s.isRecording()) {
                count ++;
            }
        }

        return count > 1;
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

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.mainFragment.check_gps.performClick();
                } else {
                    Toast.makeText(this, "Please grant permission to use the gps sensor", Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.mainFragment.btn_record.performClick();
                } else {
                    Toast.makeText(this, "Please grant permission to write to external storage", Toast.LENGTH_LONG).show();
                }
        }
    }
}