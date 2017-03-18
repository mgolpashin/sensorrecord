package de.martingolpashin.sensor_record.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by martin on 18.03.17.
 */

public class Compress {
    private static final int BUFFER = 2048;

    public static void zip(File[] files, File zipFile) {
        try  {
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for (File file : files) {
                Log.v("Compress", "Adding: " + file);
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(file.getName().substring(file.getName().lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
