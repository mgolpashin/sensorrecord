package de.martingolpashin.sensor_record.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.martingolpashin.sensor_record.fragments.FileFragment;
import de.martingolpashin.sensor_record.fragments.SensorFragment;

/**
 * Created by martin on 15.03.17.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private SensorFragment sensorFragment;
    private FileFragment fileFragment;

    public PagerAdapter(FragmentManager fm, SensorFragment sensorFragment, FileFragment fileFragment) {
        super(fm);
        this.sensorFragment = sensorFragment;
        this.fileFragment = fileFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return sensorFragment;
            case 1: return fileFragment;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
