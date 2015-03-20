package com.bowen.assignment.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by gen on 2015-03-14.
 */
public class FileUtil {

    private final static String TAG="FileUtil";

    public static boolean saveImageToInternalStorage(Bitmap image,String fileName) {

        try {
            Context context=MGlobal.getInstance().context;

            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            image.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();

            return true;

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());

            return false;
        }
    }


    public static Bitmap readFileFromInternalStorage(String fileName){
        try {
            Context context=MGlobal.getInstance().context;
            File filePath = context.getFileStreamPath(fileName);
            FileInputStream fi = new FileInputStream(filePath);
            Bitmap thumbnail = BitmapFactory.decodeStream(fi);
            return thumbnail;
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

}
