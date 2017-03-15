package de.martingolpashin.sensor_record.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.martingolpashin.sensor_record.fragments.FileFragment_;
import de.martingolpashin.sensor_record.fragments.SensorFragment_;

/**
 * Created by martin on 15.03.17.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new SensorFragment_();
            case 1: return new FileFragment_();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
