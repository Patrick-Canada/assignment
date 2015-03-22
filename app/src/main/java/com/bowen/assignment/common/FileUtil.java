package com.bowen.assignment.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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


    public static File getImageFileDir(){
        Context context=MGlobal.getInstance().context;
        File file=context.getFilesDir();
        String path= file.getAbsolutePath();
        String myPath=path+"/image";
        File imageDir=new File(myPath);
        if (!imageDir.exists()){
            imageDir.mkdir();
        }
        return imageDir;
    }


    public static boolean saveFile(Bitmap image,String fileName){
        File  imageFile = new File(getImageFileDir(), fileName);
        FileOutputStream outputStream=null;
        try{
            outputStream=new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return true;

        }catch (IOException e){
            Log.e(TAG,e.getMessage());
            return false;
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }
    }


    public static Bitmap getFileByName(String name){
        File imageDir=getImageFileDir();
        FileInputStream fi;
        try {
            fi = new FileInputStream(new File(imageDir,name));
            Bitmap thumbnail = BitmapFactory.decodeStream(fi);
            return thumbnail;
        } catch (FileNotFoundException e) {
            Log.e(TAG,"file not found");
        }
        return null;
    }


    public static Bitmap readFileFromInternalStorage(String fileName){
        try {
            Context context=MGlobal.getInstance().context;
            File filePath = context.getFileStreamPath(fileName);
            if (filePath.exists()){
                FileInputStream fi = new FileInputStream(filePath);
                Bitmap thumbnail = BitmapFactory.decodeStream(fi);
                return thumbnail;
            }else {
                return null;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

}
