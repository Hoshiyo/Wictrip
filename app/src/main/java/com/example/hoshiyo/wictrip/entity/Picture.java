package com.example.hoshiyo.wictrip.entity;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 30/10/2014.
 */
public class Picture  implements Serializable {
    int id = -1;
    private String uri = null;
    private String countryCode = null;
    private String postalCode = null;
    private double lat;
    private double lng;
    private long dateTaken = -1;

    public Picture(int id, String uri, String countryCode, String postalCode, double lat, double lng, long dateTaken) {
        this.id = id;
        this.uri = uri;
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.lat = lat;
        this.lng = lng;
        this.dateTaken = dateTaken;
    }

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public long getDateTaken() {
        return dateTaken;
    }
}
