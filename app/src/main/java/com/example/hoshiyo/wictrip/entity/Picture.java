package com.example.hoshiyo.wictrip.entity;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 30/10/2014.
 */
public class Picture {
    int id = -1;
    private Uri uri = null;
    private String countryCode = null;
    private String postalCode = null;
    private LatLng position = null;
    private long dateTaken = -1;

    public Picture(int id, Uri uri, String countryCode, String postalCode, LatLng position, long dateTaken) {
        this.id = id;
        this.uri = uri;
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.position = position;
        this.dateTaken = dateTaken;
    }

    public int getId() {
        return id;
    }

    public Uri getUri() {
        return uri;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public LatLng getPosition() {
        return position;
    }

    public long getDateTaken() {
        return dateTaken;
    }
}
