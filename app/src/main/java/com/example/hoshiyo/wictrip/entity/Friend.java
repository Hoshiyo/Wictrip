package com.example.hoshiyo.wictrip.entity;

import android.graphics.Bitmap;

import java.util.Collection;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public class Friend {
    String fName = null;
    String lName = null;
    String id = null;
    Bitmap picture = null;

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getId() {
        return id;
    }

    public Bitmap getPicture() {
        return picture;
    }
}