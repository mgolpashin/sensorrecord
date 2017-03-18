package de.martingolpashin.sensor_record.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import de.martingolpashin.sensor_record.R;

/**
 * Created by martin on 16.10.16.
 */
public class Sensor {
    public Context context;
    public boolean active;
    public int interval;
    public boolean isRecording;
    public long startDate;
    public Timer timer;
    public ArrayList data;
    private String name;
    private int defaultValue;

    private CheckBox checkBox;
    private EditText editText;

    private String[] columns;
    private String seperator = ",";
    private LinearLayout view;

    public Sensor(Context context, String name, int defaultValue, String[] columns) {
        this.context = context;

        this.name = name;
        this.defaultValue = defaultValue;
        this.columns = columns;

        this.data = new ArrayList<>();
        this.isRecording = false;

        this.view = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.single_sensor, null);
        this.checkBox = (CheckBox) this.view.findViewById(R.id.check_sensor);
        this.checkBox.setText(name);
        this.editText = (EditText) this.view.findViewById(R.id.edit_sensor);
        editText.setText("" + defaultValue);
        this.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editText.setEnabled(isChecked);
                setActive(isChecked);
            }
        });
    }

    final public void record() {
        this.isRecording = true;
        this.startDate = new Date().getTime();
        this.timer = new Timer();
        scheduleRecording();
    }

    public void scheduleRecording() {}

    public final void reset() {
        this.data = new ArrayList<>();
        this.isRecording = false;
    }

    public File writeToCSV(String fileName, File dir, boolean includeDateTime) {
        this.timer.cancel();
        fileName = includeDateTime ? fileName + "_" + name : name;
        fileName += ".csv";
        File file = new File(dir, fileName);

        try {
            FileWriter fw = new FileWriter(file);
            String head = "";
            for(String column : this.columns) {
                head += column + seperator;
            }
            fw.write(head + System.getProperty("line.separator"));

            for(Object obj: data) {
                fw.write(obj.toString());
            }

            fw.flush();
            fw.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LinearLayout getView() {
        return view;
    }

    final public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    final public void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    final public boolean isRecording() {
        return isRecording;
    }

    final public void setInterval(int interval) {
        this.interval = interval;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public EditText getEditText() {
        return editText;
    }
}
