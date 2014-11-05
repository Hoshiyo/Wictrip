package com.example.hoshiyo.wictrip;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.hoshiyo.BitmapDecoder;
import com.example.hoshiyo.wictrip.dao.AlbumDao;
import com.example.hoshiyo.wictrip.dao.PictureDao;
import com.example.hoshiyo.wictrip.dao.PlaceDao;
import com.example.hoshiyo.wictrip.service.PictureService;

import java.sql.SQLException;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 01/11/2014.
 */
public class Wictrip extends Application {
    private static final String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            PictureDao.getInstance().init(getApplicationContext());
            PlaceDao.getInstance().init(getApplicationContext());
            AlbumDao.getInstance().init(getApplicationContext());
            Log.d(TAG, "initialized");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Get screen width and height
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d(TAG, "W: " + width + " H: " + height);

        // Set picture size for gallery (2 picture per column)
        BitmapDecoder.init(width/2);

        // Start picture service to process user pictures
        startService(new Intent(this, PictureService.class));
    }
}