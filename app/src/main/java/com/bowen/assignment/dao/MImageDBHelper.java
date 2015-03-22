package com.bowen.assignment.dao;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Created by gen on 2015-03-21.
 */
public class MImageDBHelper extends SQLiteOpenHelper {
    private static final String TAG="MImageDBHelper";

    public static final String TABLE_IMAGE= "image";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GPS_X= "gps_x";
    public static final String COLUMN_GPS_Y= "gps_y";
    public static final String COLUMN_IMAGE_PATH="image_path";
    public static final String COLUMN_SEND_STAT="send_flag";
    public static final String COLUMN_IMAGE_NAME="image_name";

    private static final String DATABASE_NAME = "sqlite.db";
    private static final int DATABASE_VERSION = 1;
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_IMAGE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_GPS_X
            + " text, "+COLUMN_GPS_Y+" text, "+ COLUMN_IMAGE_PATH
            + " text, "+COLUMN_SEND_STAT+" text, "+COLUMN_IMAGE_NAME
            + " text);";

    public MImageDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        onCreate(db);
    }
}
