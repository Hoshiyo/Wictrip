package com.example.hoshiyo.wictrip.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hoshiyo.wictrip.DatabaseHelper;
import com.example.hoshiyo.wictrip.entity.Album;
import com.example.hoshiyo.wictrip.entity.Picture;
import com.example.hoshiyo.wictrip.entity.Place;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public class AlbumDao implements IDao {
    private static final String TAG = "Album Dao";
    private static final String URI_SEPARATOR = ";";

    public static final String TABLE_NAME = "album";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PICTURES = "pictures";
    public static final String COLUMN_DATE_BEGIN = "date_begin";
    public static final String COLUMN_DATE_END = "date_end";
    public static final String COLUMN_PLACE = "place";
    public static final String[] allColumns = {COLUMN_ID, COLUMN_NAME,
            COLUMN_PICTURES, COLUMN_DATE_BEGIN, COLUMN_DATE_END, COLUMN_PLACE};
    private static final String TABLE_CREATION = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_PLACE + " integer, "
            + COLUMN_DATE_BEGIN + " integer, "
            + COLUMN_DATE_END + " integer, "
            + COLUMN_PICTURES + " text, "
            + "foreign key (" + COLUMN_PLACE + ") "
            + "references " + PlaceDao.TABLE_NAME + " (" + PlaceDao.COLUMN_ID + "))";

    private static AlbumDao ourInstance = new AlbumDao();

    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;
    private Collection<Object> albums = null;

    public static AlbumDao getInstance() {
        return ourInstance;
    }

    /**
     * @return SQLite String to create the bdd table
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATION);
    }

    public static void onUpgrade(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    private Collection<Album> mAlbums = null;

    private AlbumDao() {
        //TODO initiate mAlbums with a query to DB
    }

    public void init(Context context)  throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public Object create(Object obj) {
        Album album = (Album) obj;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, album.getName());
        contentValues.put(COLUMN_PICTURES, PicturesToString(album));
        contentValues.put(COLUMN_DATE_BEGIN, album.getDateBegin().getTimeInMillis());
        contentValues.put(COLUMN_DATE_END, album.getDateEnd().getTimeInMillis());
        contentValues.put(COLUMN_PLACE, album.getPlace().getId());

        long insertId = db.insert(TABLE_NAME, null, contentValues);
        Cursor cursor = db.query(TABLE_NAME, allColumns, COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Album newAlbum = (Album) cursorToAlbum(cursor);
        cursor.close();

        return newAlbum;
    }

    private void fetchAll() {
        Log.d(TAG, "FETCH ALL");
        albums = new ArrayList<Object>();

        Cursor cursor = db.query(TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        Log.d(TAG, "Count album: " + cursor.getCount());

        while (!cursor.isAfterLast()) {
            albums.add(cursorToAlbum(cursor));
            cursor.moveToNext();
        }
        cursor.close();
    }

    @Override
    public Collection<Object> getAll() {
        Log.d(TAG, "GET ALL");
        if(albums == null) {
            fetchAll();
        }

        return albums;
    }

    @Override
    public Object getItemById(int id) {
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

    private Album cursorToAlbum(Cursor cursor) {
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        Collection<Picture> pictures = stringToPictures(cursor.getString(2));

        long timeBegin = cursor.getLong(3);
        long timeEnd = cursor.getLong(4);

        Place place = (Place) PlaceDao.getInstance().getItemById(cursor.getInt(5));

        cursor.close();

        return new Album(id, name, pictures, timeBegin, timeEnd, place);
    }

    public String PicturesToString(Album album) {
        Collection<Picture> pictures = album.getPictures();
        StringBuffer formattedPicturesUri = new StringBuffer();
        int len = pictures.size();
        for (Picture picture : pictures) {
            formattedPicturesUri.append(picture.getUri().toString());
            formattedPicturesUri.append(URI_SEPARATOR);
        }
        formattedPicturesUri.deleteCharAt(formattedPicturesUri.length() - 1);

        Log.d(TAG, "Picture URI: " + formattedPicturesUri);
        return formattedPicturesUri.toString();
    }

    public Collection<Picture> stringToPictures(String picturesUri) {
        PictureDao pictureDao = PictureDao.getInstance();
        Collection<Picture> pictures = new ArrayList<Picture>();
        Picture picture = null;
        String[] uriTab = picturesUri.split(URI_SEPARATOR);

        for(String uri : uriTab) {
            picture = pictureDao.getPictureByUri(uri);
            if(picture == null) {
                // TODO delete Uri ?
                continue;
            }

            pictures.add(picture);
        }

        return pictures;
    }
}
