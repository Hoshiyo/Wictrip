package com.example.hoshiyo.wictrip.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.hoshiyo.wictrip.DatabaseHelper;
import com.example.hoshiyo.wictrip.entity.Picture;
import com.google.android.gms.maps.model.LatLng;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 30/10/2014.
 */
public class PictureDao implements IDao {
    private static final String TAG = "Picture Dao";

    public static final String TABLE_NAME = "picture";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_COUNTRY_CODE = "country_code";
    public static final String COLUMN_POSTAL_CODE = "postal_code";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_DATE_TAKEN = "date_taken";

    public static final String[] allColumns = {COLUMN_ID, COLUMN_URI, COLUMN_COUNTRY_CODE,
            COLUMN_POSTAL_CODE, COLUMN_LAT, COLUMN_LNG, COLUMN_DATE_TAKEN};
    private static final String TABLE_CREATION = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_URI + " text not null, "
            + COLUMN_COUNTRY_CODE + " text, "
            + COLUMN_POSTAL_CODE + " text, "
            + COLUMN_LAT + " real, "
            + COLUMN_LNG + " real, "
            + COLUMN_DATE_TAKEN + " integer)";

    private static PictureDao ourInstance = new PictureDao();

    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;
    Collection<Object> pictures = null;

    public static PictureDao getInstance() {
        return ourInstance;
    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATION);
    }

    public static void onUpgrade(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    private PictureDao() {
    }

    public void init(Context context) throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        fetchAll();
    }

    @Override
    public Picture create(Object obj) {
        Picture picture = (Picture) obj;
        if (exist(obj))
            return picture;

        // Add new picture to DB
        ContentValues values = pictureToContentValues(picture);
        long insertId = db.insert(TABLE_NAME, null, values);

        // Query the new picture created
        Cursor cursor = db.query(TABLE_NAME,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Picture newPicture = cursorToPicture(cursor);
        cursor.close();

        return newPicture;
    }

    /**
     * Fetch all pictures from DB
     */
    private void fetchAll() {
        pictures = new ArrayList<Object>();

        Cursor cursor = db.query(TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            pictures.add(cursorToPicture(cursor));
            cursor.moveToNext();
        }
        cursor.close();
    }

    /**
     * @return Get all pictures
     */
    @Override
    public Collection<Object> getAll() {
        return pictures;
    }

    @Override
    public Object getItemById(int id) {
        return null;
    }

    @Override
    public Object update(Object obj) {
        Picture picture = (Picture) obj;
        ContentValues values = pictureToContentValues(picture);
        db.update(TABLE_NAME, values, COLUMN_ID + "=" + picture.getId(), null);

        Cursor cursor = db.query(TABLE_NAME, allColumns, COLUMN_ID + "=" + picture.getId(), null, null, null, null);
        Picture updatedPicture = cursorToPicture(cursor);
        cursor.close();

        return updatedPicture;
    }

    @Override
    public Object delete(Object obj) {
        Picture picture = (Picture) obj;
        db.delete(TABLE_NAME, COLUMN_ID + "=" + picture.getId(), null);

        return obj;
    }

    @Override
    public boolean exist(Object obj) {
        Picture picture = (Picture) obj;
        String[] columns = {COLUMN_ID};
        String[] whereValues = {picture.getUri().toString()};
        Cursor cursor = db.query(TABLE_NAME, columns,
                COLUMN_URI + "=?", whereValues, null, null, null);

        int count = cursor.getCount();
        cursor.close();

        Log.d(TAG, "Picture already exist: " + count);
        if (count >= 1)
            return true;

        return false;
    }

    private Picture cursorToPicture(Cursor cursor) {
        int id = cursor.getInt(0);
        Uri uri = Uri.parse(cursor.getString(1));
        String countryCode = cursor.getString(2);
        String postalCode = cursor.getString(3);
        LatLng position = new LatLng(cursor.getDouble(4), cursor.getDouble(5));
        long dateTaken = cursor.getLong(6);

        return new Picture(id, uri, countryCode, postalCode, position, dateTaken);
    }

    /**
     * Check if the picture is already in the DB
     *
     * @param uri picture uri
     * @return Boolean if the picture is already in the DB
     */
    public boolean exist(Uri uri) {
        String[] columns = {COLUMN_ID};
        String[] whereValues = {uri.toString()};
        Cursor cursor = db.query(TABLE_NAME, columns,
                COLUMN_URI + "=?", whereValues, null, null, null);
        int nbResult = cursor.getCount();
        cursor.close();

        if (nbResult == 0)
            return false;

        return true;
    }

    public Picture getPictureByUri(String uri) {
        Picture picture = null;
        for (Object obj : pictures) {
            picture = (Picture) obj;
            if (picture.getUri().toString().equals(uri))
                return picture;
        }

        return picture;
    }

    private ContentValues pictureToContentValues(Picture picture) {
        ContentValues values = new ContentValues();
        LatLng position = picture.getPosition();

        values.put(COLUMN_URI, picture.getUri().toString());
        values.put(COLUMN_COUNTRY_CODE, picture.getCountryCode());
        values.put(COLUMN_POSTAL_CODE, picture.getPostalCode());
        values.put(COLUMN_LAT, position.latitude);
        values.put(COLUMN_LNG, position.longitude);
        values.put(COLUMN_DATE_TAKEN, picture.getDateTaken());

        return values;
    }
}
