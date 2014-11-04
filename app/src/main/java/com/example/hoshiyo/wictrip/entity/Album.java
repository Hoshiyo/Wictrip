package com.example.hoshiyo.wictrip.entity;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public class Album  implements Serializable {
    private static final String TAG = "Album";
    int id = -1;
    String name = null; // Album name
    Collection<Picture> pictures = new ArrayList<Picture>();
    Calendar dateBegin = null;
    Calendar dateEnd = null;
    Place place = null;

    public Album(int id, String name, Collection<Picture> pictures, long timeBegin, long timeEnd, Place place) {
        this.id = id;
        this.name = name;
        this.pictures = pictures;
        this.dateBegin = Calendar.getInstance();
        this.dateBegin.setTimeInMillis(timeBegin);
        this.dateEnd = Calendar.getInstance();
        this.dateEnd.setTimeInMillis(timeEnd);
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Picture> getPictures() {
        return pictures;
    }

    public Calendar getDateBegin() {
        return dateBegin;
    }

    public Calendar getDateEnd() {
        return dateEnd;
    }

    public Place getPlace() {
        return place;
    }

    @Override
    public String toString() {
        return name;
    }
}