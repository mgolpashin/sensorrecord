package de.martingolpashin.sensor_record.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.File;

public class FileHandler {

    public static File getWritableStorageDir(Context context) {
        return isExternalStorageWritable() && ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ? getExternalStorageDir() : getInternalStorageDir(context);

    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static File getExternalStorageDir() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "SensorRecord" + File.separator);
        if(!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }

    private static File getInternalStorageDir(Context context) {
        File dir = new File(context.getFilesDir() + File.separator + "SensorRecord" + File.separator);
        if(!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }

    public static File convertToZip(File dir) {
        File zip = new File(dir, dir.getName() + ".zip");
        Compress.zip(dir.listFiles(), zip);
        return zip;
    }
}