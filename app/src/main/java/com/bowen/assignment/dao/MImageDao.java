package com.bowen.assignment.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bowen.assignment.entity.ImageEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gen on 2015-03-21.
 */
public class MImageDao {

    private SQLiteDatabase database;
    private MImageDBHelper dbHelper;
    private String[] allColumns = { MImageDBHelper.COLUMN_ID,
            MImageDBHelper.COLUMN_GPS_X,MImageDBHelper.COLUMN_GPS_Y,
            MImageDBHelper.COLUMN_IMAGE_PATH,MImageDBHelper.COLUMN_SEND_STAT
            ,MImageDBHelper.COLUMN_IMAGE_NAME };

    public MImageDao(Context context) {
        dbHelper = new MImageDBHelper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ImageEntity createImageEntity(ImageEntity imageEntity) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(MImageDBHelper.COLUMN_GPS_X, imageEntity.getX());
        values.put(MImageDBHelper.COLUMN_GPS_Y,imageEntity.getY());
        values.put(MImageDBHelper.COLUMN_IMAGE_PATH,imageEntity.getUri());
        values.put(MImageDBHelper.COLUMN_SEND_STAT,imageEntity.getSendState());
        values.put(MImageDBHelper.COLUMN_IMAGE_NAME,imageEntity.getName());
        long insertId = database.insertOrThrow(MImageDBHelper.TABLE_IMAGE, null,
                values);
        Cursor cursor = database.query(MImageDBHelper.TABLE_IMAGE,
                allColumns, MImageDBHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ImageEntity image =cursorToImage(cursor);
        cursor.close();
        this.close();
        return image;
    }

    private ImageEntity cursorToImage(Cursor cursor) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(cursor.getLong(0));
        imageEntity.setX(cursor.getDouble(1));
        imageEntity.setY(cursor.getDouble(2));
        imageEntity.setUri(cursor.getString(3));
        imageEntity.setSendState(cursor.getInt(4));
        imageEntity.setName(cursor.getString(5));
        return imageEntity;
    }

    public List<ImageEntity> getAllImages() {
        this.open();
        List<ImageEntity> imageEntities = new ArrayList<>();

        Cursor cursor = database.query(MImageDBHelper.TABLE_IMAGE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ImageEntity imageEntity = cursorToImage(cursor);
            imageEntities.add(imageEntity);
            cursor.moveToNext();
        }
        cursor.close();
        this.close();
        return imageEntities;
    }
}
