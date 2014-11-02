package com.example.hoshiyo.wictrip.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hoshiyo.wictrip.DatabaseHelper;
import com.example.hoshiyo.wictrip.entity.Place;
import com.google.android.gms.maps.model.LatLng;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public class PlaceDao implements IDao {

    public static final String TABLE_NAME = "place";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COUNTRY_NAME = "country_name";
    public static final String COLUMN_COUNTRY_CODE = "country_code";
    public static final String COLUMN_LOCALITY = "locality";
    public static final String COLUMN_POSTAL_CODE = "postal_code";
    public static final String COLUMN_LAT = "lat"; // Latitude
    public static final String COLUMN_LNG = "lng"; // Longitude
    public static final String[] allColumns = {COLUMN_ID, COLUMN_COUNTRY_NAME,
            COLUMN_COUNTRY_CODE, COLUMN_LOCALITY, COLUMN_POSTAL_CODE,
            COLUMN_LAT, COLUMN_LNG};
    private static final String TABLE_CREATION = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_COUNTRY_NAME + " text not null, "
            + COLUMN_COUNTRY_CODE + " text not null, "
            + COLUMN_LOCALITY + " text, "
            + COLUMN_POSTAL_CODE + " text, "
            + COLUMN_LNG + " real, "
            + COLUMN_LAT + " real)";

    private static PlaceDao ourInstance = new PlaceDao();

    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;
    private Collection<Object> places = null;

    public static PlaceDao getInstance() {
        return ourInstance;
    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATION);
    }

    public static void onUpgrade(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    private PlaceDao() {
    }

    public void init(Context context) throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public Object create(Object obj) {
        Place place = (Place) obj;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COUNTRY_NAME, place.getCountryName());
        contentValues.put(COLUMN_COUNTRY_CODE, place.getCountryCode());
        contentValues.put(COLUMN_LOCALITY, place.getLocality());
        contentValues.put(COLUMN_POSTAL_CODE, place.getPostalCode());
        LatLng position = place.getPosition();
        contentValues.put(COLUMN_LAT, position.latitude);
        contentValues.put(COLUMN_LNG, position.longitude);

        long insertId = db.insert(TABLE_NAME, null, contentValues);
        Cursor cursor = db.query(TABLE_NAME, allColumns, COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Place newPlace = (Place) cursorToPlace(cursor);
        cursor.close();

        return newPlace;
    }

    /**
     * Fetch all Place form DB
     */
    private void fetchAll() {
        places = new ArrayList<Object>();

        Cursor cursor = db.query(TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            places.add(cursorToPlace(cursor));
            cursor.moveToNext();
        }
        cursor.close();
    }

    /**
     *
     * @return Get all Place
     */
    @Override
    public Collection<Object> getAll() {
        if(places == null) {
            fetchAll();
        }
        return places;
    }

    @Override
    public Object getItemById(int id) {
        if(places == null) {
            fetchAll();
        }

        Place place;
        for(Object obj : places) {
            place = (Place) obj;

            if(place.getId() == id)
                return obj;
        }

        return null;
    }

    @Override
    public Object update(Object obj) {
        return null;
    }

    @Override
    public Object delete(Object obj) {
        return null;
    }

    private Object cursorToPlace(Cursor cursor) {
        int id = cursor.getInt(0);
        String countryName = cursor.getString(1);
        String countryCode = cursor.getString(2);
        String locality = cursor.getString(3);
        String postalCode = cursor.getString(4);
        LatLng position = new LatLng(cursor.getDouble(5), cursor.getDouble(6));

        return new Place(id, countryName, countryCode, locality, postalCode, position);
    }
}
