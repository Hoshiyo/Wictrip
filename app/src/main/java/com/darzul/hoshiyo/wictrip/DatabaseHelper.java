package com.darzul.hoshiyo.wictrip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.darzul.hoshiyo.wictrip.dao.AlbumDao;
import com.darzul.hoshiyo.wictrip.dao.PictureDao;
import com.darzul.hoshiyo.wictrip.dao.PlaceDao;
import com.darzul.hoshiyo.wictrip.entity.Album;
import com.darzul.hoshiyo.wictrip.entity.Picture;
import com.darzul.hoshiyo.wictrip.entity.Place;

import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 30/10/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DB helper";

    private static final String DATABASE_NAME = "wicktrip.db";
    private static final int DATABASE_VERSION = 11;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PictureDao.onCreate(db);
        PlaceDao.onCreate(db);
        AlbumDao.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Destroy and recreate the db
        PictureDao.onUpgrade(db);
        PlaceDao.onUpgrade(db);
        AlbumDao.onUpgrade(db);

        db.setVersion(1);
    }

    public static void printDB() {
        Log.d(TAG, "Pictures");
        Collection<Object> pictures = PictureDao.getInstance().getAll();
        Picture picture;
        for (Object obj : pictures) {
            picture = (Picture) obj;
            Log.d(TAG, "Id: " + picture.getId() + " Uri: " + picture.getUri()
                    + " CountryCode: " + picture.getCountryCode()
                    + " PostalCode: " + picture.getPostalCode() + " Lat: "
                    + picture.getLat() + " Lng: "
                    + picture.getLng()
                    + " DateTaken: " + picture.getDateTaken());
        }

        Log.d(TAG, "Places");
        Collection<Object> places = PlaceDao.getInstance().getAll();
        Place place;
        for (Object obj : places) {
            place = (Place) obj;
            Log.d(TAG, "Id: " + place.getId() + " CountryName: " + place.getCountryName()
                    + " CountryCode: " + place.getCountryCode() + " Locality: "
                    + place.getLocality() + " PostalCode: " + place.getPostalCode()
                    + " Lat: " + place.getLat() + " Lng: "
                    + place.getLng());
        }

        Log.d(TAG, "Albums");
        Collection<Object> albums = AlbumDao.getInstance().getAll();
        Album album;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        for (Object obj : albums) {
            album = (Album) obj;
            Log.d(TAG, "Id: " + album.getId() + " Name: " + album.getName()
                    + " dateBegin: " + format.format(album.getTimeBegin())
                    + " dateEnd: " + format.format(album.getTimeEnd())
                    + " Place: " + album.getPlace().getId());
        }
    }
}
