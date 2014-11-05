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

    public void init(Context context) throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        fetchAll();
    }

    @Override
    public Album create(Object obj) {
        Album album = (Album) obj;
        if (exist(obj))
            return album;

        ContentValues values = albumToContentValues(album);
        long insertId = db.insert(TABLE_NAME, null, values);

        Cursor cursor = db.query(TABLE_NAME, allColumns, COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Album newAlbum = (Album) cursorToAlbum(cursor);
        cursor.close();

        return newAlbum;
    }

    private void fetchAll() {
        albums = new ArrayList<Object>();

        Cursor cursor = db.query(TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            albums.add(cursorToAlbum(cursor));
            cursor.moveToNext();
        }
        cursor.close();
    }

    @Override
    public Collection<Object> getAll() {
        return albums;
    }

    @Override
    public Album getItemById(int id) {
//        Cursor cursor = db.query(TABLE_NAME, allColumns, COLUMN_ID + "=" + id, null, null, null, null);
//        cursor.moveToFirst();
//        Album album = cursorToAlbum(cursor);
//        cursor.close();

        Album album = null;
        for (Object obj : albums) {
            album = (Album) obj;
            if (album.getId() == id)
                return album;
        }
        return album;
    }

    @Override
    public Object update(Object obj) {
        Album album = (Album) obj;
        ContentValues values = albumToContentValues(album);
        db.update(TABLE_NAME, values, COLUMN_ID + "=" + album.getId(), null);

        Cursor cursor = db.query(TABLE_NAME, allColumns, COLUMN_ID + "=" + album.getId(), null, null, null, null);
        cursor.moveToFirst();
        Album updatedAlbum = cursorToAlbum(cursor);
        cursor.close();

        return updatedAlbum;
    }

    @Override
    public Object delete(Object obj) {
        Album album = (Album) obj;
        db.delete(TABLE_NAME, COLUMN_ID + "=" + album.getId(), null);

        return obj;
    }

    @Override
    public boolean exist(Object obj) {
        Album album = (Album) obj;
        String[] columns = {COLUMN_ID};
        String[] selectionArgs = {album.getName(), "" + album.getPlace().getId()};
        Cursor cursor = db.query(TABLE_NAME, columns,
                COLUMN_NAME + "=?" + " and " + COLUMN_PLACE + "=?",
                selectionArgs, null, null, null);

        int count = cursor.getCount();
        cursor.close();

        Log.d(TAG, "Place already exist: " + count);
        if (count >= 1)
            return true;

        return false;
    }

    private Album cursorToAlbum(Cursor cursor) {
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        Collection<Picture> pictures = stringToPictures(cursor.getString(2));
        Log.d(TAG, name + ": " + pictures.size());

        long timeBegin = cursor.getLong(3);
        long timeEnd = cursor.getLong(4);

        Place place = (Place) PlaceDao.getInstance().getItemById(cursor.getInt(5));

        return new Album(id, name, pictures, timeBegin, timeEnd, place);
    }

    public String PicturesToString(Album album) {
        Collection<Picture> pictures = album.getPictures();
        StringBuffer formattedPicturesUri = new StringBuffer();
        int len = pictures.size();
        for (Picture picture : pictures) {
            formattedPicturesUri.append(picture.getUri());
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

        for (String uri : uriTab) {
            picture = pictureDao.getPictureByUri(uri);
            if (picture == null) {
                Log.d(TAG, "Picture does not exist !");
                // TODO delete Uri ?
                continue;
            }

            pictures.add(picture);
        }

        Log.d(TAG, "Pictures: " + pictures.size());

        return pictures;
    }

    private ContentValues albumToContentValues(Album album) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, album.getName());
        values.put(COLUMN_PICTURES, PicturesToString(album));
        values.put(COLUMN_DATE_BEGIN, album.getDateBegin().getTimeInMillis());
        values.put(COLUMN_DATE_END, album.getDateEnd().getTimeInMillis());
        values.put(COLUMN_PLACE, album.getPlace().getId());

        return values;
    }

    public void addPicture(Album album, Picture picture) {
        album.addPicture(picture);
        update(album);
    }

    public void deletePicture(Album album, Picture picture) {
        album.deletePicture(picture);
        update(album);
    }
}
