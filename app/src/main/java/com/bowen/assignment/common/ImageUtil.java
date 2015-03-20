package com.bowen.assignment.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.text.InputType;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by gen on 2015-03-14.
 */
public class ImageUtil {

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static BitmapFactory.Options optionsBitmapFromInputStream(InputStream is,
                                                                     int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,null,options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return options;
    }


    public static Bitmap getBitmapFromUriByOptions(Context context,Uri uri,
                                                   BitmapFactory.Options options){
        try {
            InputStream inputStream=context.getContentResolver().
                    openInputStream(uri);
            Bitmap img = BitmapFactory.decodeStream(inputStream, null, options);
            return img;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


}
