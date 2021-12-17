package de.martingolpashin.sensor_record.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.martingolpashin.sensor_record.R;
import de.martingolpashin.sensor_record.activities.MainActivity;

@EFragment(R.layout.fragment_files)
public class FileFragment extends Fragment {

    MainActivity activity;

    @ViewById
    RecyclerView file_list;

    public FileFragment() {}

    @AfterViews
    void init() {
        this.activity = (MainActivity) getActivity();
        file_list.setHasFixedSize(true);
        file_list.setLayoutManager(new LinearLayoutManager(this.activity));
        file_list.setAdapter(this.activity.getAdapter());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_files, container, false);
    }


}
