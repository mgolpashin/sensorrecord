package de.martingolpashin.sensor_record.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import de.martingolpashin.sensor_record.fragments.FileFragment;
import de.martingolpashin.sensor_record.fragments.SensorFragment;

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
