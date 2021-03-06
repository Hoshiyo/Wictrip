package com.darzul.hoshiyo.wictrip.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public class Album implements Serializable {
    private static final String TAG = "Album";
    int id = -1;
    String name = null; // Album name
    Collection<Picture> pictures = new ArrayList<Picture>();
    //Calendar dateBegin = null;
    //Calendar dateEnd = null;
    long timeBegin;
    long timeEnd;
    Place place = null;

    public Album(int id, String name, List<Picture> pictures, long timeBegin, long timeEnd, Place place) {
        this.id = id;
        this.name = name;
        this.pictures = pictures;
        this.timeBegin = timeBegin;
        //this.dateBegin = Calendar.getInstance();
        //this.dateBegin.setTimeInMillis(timeBegin);
        this.timeEnd = timeEnd;
        //this.dateEnd = Calendar.getInstance();
        //this.dateEnd.setTimeInMillis(timeEnd);
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

    public long getTimeBegin() {
        return timeBegin;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public Place getPlace() {
        return place;
    }

    @Override
    public String toString() {
        return name;
    }

    public void deletePicture(Picture picture) {
        pictures.remove(picture);
    }

    public void addPicture(Picture picture) {
        pictures.add(picture);
    }
}