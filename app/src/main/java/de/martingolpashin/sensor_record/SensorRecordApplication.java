package de.martingolpashin.sensor_record;

import android.app.Activity;
import android.app.Application;

/**
 * Created by martin on 17.03.17.
 */
public class SensorRecordApplication extends Application {
    private Activity mCurrentActivity = null;

    public void onCreate() {
        super.onCreate();
    }

    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }
}
