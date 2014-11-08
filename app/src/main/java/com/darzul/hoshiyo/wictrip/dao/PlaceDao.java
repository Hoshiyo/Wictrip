package com.darzul.hoshiyo.wictrip.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.darzul.hoshiyo.wictrip.GlobalVariable;
import com.darzul.hoshiyo.wictrip.DatabaseHelper;
import com.darzul.hoshiyo.wictrip.entity.Place;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public class PlaceDao implements IDao {

    private static final String TAG = "Place Dao";
    public static final String TABLE_NAME = "place";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COUNTRY_NAME = "country_name";
    public static final String COLUMN_COUNTRY_CODE = "country_code";
    public static final String COLUMN_LOCALITY = "locality";
    public static final String COLUMN_POSTAL_CODE = "postal_code";
    public static final String COLUMN_LAT = "lat"; // Latitude
    public static final String COLUMN_LNG = "lng"; // Longitude
    private static final String COLUMN_VISITED = "visited";

    public static final String[] allColumns = {COLUMN_ID, COLUMN_COUNTRY_NAME,
            COLUMN_COUNTRY_CODE, COLUMN_LOCALITY, COLUMN_POSTAL_CODE,
            COLUMN_LAT, COLUMN_LNG, COLUMN_VISITED};
    private static final String TABLE_CREATION = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_COUNTRY_NAME + " text not null, "
            + COLUMN_COUNTRY_CODE + " text not null, "
            + COLUMN_LOCALITY + " text, "
            + COLUMN_POSTAL_CODE + " text, "
            + COLUMN_LNG + " real, "
            + COLUMN_LAT + " real,"
            + COLUMN_VISITED + " integer)";

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
        fetchAll();
    }

    @Override
    public Place create(Object obj) {
        Place place = (Place) obj;
        if (exist(obj))
            return place;

        ContentValues values = placeToContentValues(place);
        long insertId = db.insert(TABLE_NAME, null, values);
        Cursor cursor = db.query(TABLE_NAME, allColumns, COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Place newPlace = cursorToPlace(cursor);
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

        Collections.sort((java.util.List<Object>) places, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                Place leftPlace = (Place) lhs;
                String leftCountryCode = leftPlace.getCountryCode();
                String leftPostalCode = leftPlace.getPostalCode();

                Place rightPlace = (Place) rhs;
                String rightCountryCode = rightPlace.getCountryCode();
                String rightPostalCode = rightPlace.getPostalCode();

                int countryComparison = leftCountryCode.compareTo(rightCountryCode);
                int postalComparison = 0;
                if (leftPostalCode != null) {
                    if (rightPostalCode != null) {
                        postalComparison = leftPostalCode.compareTo(rightPostalCode);
                    } else
                        postalComparison = 1;
                } else {
                    if (rightPostalCode != null) {
                        postalComparison = -1;
                    }
                }

//                Log.d(TAG, leftCountryCode + " compareTo " + rightCountryCode + " -> " + countryComparison);
//                Log.d(TAG, leftPostalCode + " compareTo " + rightPostalCode + " -> " + postalComparison);
//                int result = (countryComparison * GlobalVariable.SORT_COUNTRY_WEIGHT)
//                        + (postalComparison * GlobalVariable.SORT_LOCALITY_WEIGHT);
//                Log.d(TAG, "Result: " + result);

                return (countryComparison * GlobalVariable.SORT_COUNTRY_WEIGHT)
                        + (postalComparison * GlobalVariable.SORT_LOCALITY_WEIGHT);
            }
        });
    }

    /**
     * @return Get all Place
     */
    @Override
    public Collection<Object> getAll() {
        return places;
    }

    @Override
    public Place getItemById(int id) {
        Place place;
        for (Object obj : places) {
            place = (Place) obj;

            if (place.getId() == id)
                return place;
        }

        return null;
    }

    @Override
    public Place update(Object obj) {
        Place place = (Place) obj;
        ContentValues values = placeToContentValues(place);
        db.update(TABLE_NAME, values, COLUMN_ID + "=" + place.getId(), null);

        Cursor cursor = db.query(TABLE_NAME, allColumns, COLUMN_ID + "=" + place.getId(), null, null, null, null);
        Place updatedPlace = cursorToPlace(cursor);
        cursor.close();

        return updatedPlace;
    }

    @Override
    public Object delete(Object obj) {
        Place place = (Place) obj;
        db.delete(TABLE_NAME, COLUMN_ID + "=" + place.getId(), null);

        return obj;
    }

    @Override
    public boolean exist(Object obj) {
        Place place = (Place) obj;
        String postalCode = place.getPostalCode();
        String query;
        String[] argsValue;

        if (postalCode == null) {
            argsValue = new String[1];
            argsValue[0] = place.getCountryCode();
            query = COLUMN_COUNTRY_CODE + "=?" + " and " + COLUMN_POSTAL_CODE + " is null";
            Log.d(TAG, "1 -> " + argsValue[0]);
        } else {
            argsValue = new String[2];
            argsValue[0] = place.getCountryCode();
            argsValue[1] = postalCode;
            query = COLUMN_COUNTRY_CODE + "=?" + " and " + COLUMN_POSTAL_CODE + "=?";
            Log.d(TAG, "1 -> " + argsValue[0] + " 2 -> " + argsValue[1]);
        }

        String[] columns = {COLUMN_ID};
        Cursor cursor = db.query(TABLE_NAME, columns, query, argsValue, null, null, null);

        int count = cursor.getCount();
        cursor.close();

        Log.d(TAG, "Place already exist: " + count);
        if (count >= 1)
            return true;

        return false;
    }

    @Override
    public void refreshData() {
        fetchAll();
    }

    private Place cursorToPlace(Cursor cursor) {
        int id = cursor.getInt(0);
        String countryName = cursor.getString(1);
        String countryCode = cursor.getString(2);
        String locality = cursor.getString(3);
        String postalCode = cursor.getString(4);
        double lat = cursor.getDouble(5);
        double lng = cursor.getDouble(6);
        boolean visited = (cursor.getInt(7) == 1) ? true:false;

        return new Place(id, countryName, countryCode, locality, postalCode, lat, lng, visited);
    }

    private ContentValues placeToContentValues(Place place) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_COUNTRY_NAME, place.getCountryName());
        values.put(COLUMN_COUNTRY_CODE, place.getCountryCode());
        values.put(COLUMN_LOCALITY, place.getLocality());
        values.put(COLUMN_POSTAL_CODE, place.getPostalCode());
        values.put(COLUMN_LAT, place.getLat());
        values.put(COLUMN_LNG, place.getLng());
        values.put(COLUMN_VISITED, (place.isVisited()) ? 1:0);

        return values;
    }
}