package de.martingolpashin.sensor_record.activities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.martingolpashin.sensor_record.R;
import de.martingolpashin.sensor_record.SensorRecordApplication;
import de.martingolpashin.sensor_record.adapter.FileAdapter;
import de.martingolpashin.sensor_record.adapter.PagerAdapter;
import de.martingolpashin.sensor_record.fragments.FileFragment;
import de.martingolpashin.sensor_record.fragments.FileFragment_;
import de.martingolpashin.sensor_record.fragments.SensorFragment;
import de.martingolpashin.sensor_record.fragments.SensorFragment_;
import de.martingolpashin.sensor_record.models.Sensor;
import de.martingolpashin.sensor_record.models.SensorHandler;
import de.martingolpashin.sensor_record.utils.FileHandler;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity{
    public final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    public final int PERMISSION_WRITE_EXTERNAL_STORAGE = 2;

    SensorRecordApplication app;

    @ViewById
    RelativeLayout main_layout;

    @ViewById
    TabLayout tablayout;

    @ViewById
    ViewPager pager;

    SensorHandler sensorHandler;

    SensorFragment sensorFragment;
    FileFragment fileFragment;

    FileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (SensorRecordApplication) getApplicationContext();

        setContentView(R.layout.activity_main);

        this.sensorHandler = new SensorHandler(this);
        this.sensorFragment = new SensorFragment_();
        this.fileFragment = new FileFragment();

        List<File> files = new ArrayList<>(Arrays.asList(FileHandler.getWritableStorageDir(this).listFiles()));
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName()) * -1;
            }
        });
        this.adapter = new FileAdapter(files);
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = app.getCurrentActivity();
        if (this.equals(currActivity))
            app.setCurrentActivity(null);
    }

    @AfterViews
    void init() {
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), sensorFragment, fileFragment));
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void writeCSVs(String fileName) {
        File dir = new File(FileHandler.getWritableStorageDir(this) + File.separator + fileName);
        if(!dir.exists()) {
            dir.mkdir();
        }

        for(Sensor s : sensorHandler.getSensors()) {
            if(s.isRecording()) {
                s.writeToCSV(fileName, dir, false);
                s.setRecording(false);
            }
        }

        Snackbar.make(this.main_layout, dir.getName() + " saved", Snackbar.LENGTH_LONG).show();
        this.adapter.add(dir);
    }

    public void resetSensors() {
        for(Sensor s : sensorHandler.getSensors()) {
            s.reset();
        }
    }

    public void record() {
        for(Sensor s : sensorHandler.getSensors()) {
            if(s.isActive()) {
                s.record();
            }
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:

                /*
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TODO FIX this.sensorFragment.check_gps.performClick();
                } else {
                    Toast.makeText(this, R.string.permissions_grant_gps, Toast.LENGTH_SHORT).show();
                } */
                Toast.makeText(this, R.string.permissions_grant_gps, Toast.LENGTH_SHORT).show();
                break;
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.sensorFragment.btn_record_stop.performClick();
                } else {
                    Toast.makeText(this, R.string.permissions_grant_external_storage, Toast.LENGTH_LONG).show();
                }
        }
    }

    public SensorHandler getSensorHandler() {
        return sensorHandler;
    }
}