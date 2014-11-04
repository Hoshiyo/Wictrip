package com.example.hoshiyo.wictrip;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

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

        startService(new Intent(this, PictureService.class));
    }
}