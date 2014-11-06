package com.example.hoshiyo.wictrip.entity;

import com.example.hoshiyo.GlobalVariable;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public class Place implements Serializable, Comparable<Place> {
    int id = -1;
    private String countryName;
    private String countryCode;
    private String locality;
    private String postalCode;
    private double lat;
    private double lng;


    public Place(int id, String countryName, String countryCode, String locality, String postalCode, double lat, double lng) {
        this.id = id;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.locality = locality;
        this.postalCode = postalCode;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getLocality() {
        return locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        if (postalCode == null) {
            return countryName + " (" + countryCode + ")";
        } else {
            return locality + " (" + postalCode + ")";
        }
    }

    @Override
    public int compareTo(Place otherPlace) {
        String otherCountryCode = otherPlace.getCountryCode();
        String otherPostalCode = otherPlace.getPostalCode();

        int countryComparison = countryCode.compareTo(otherCountryCode);
        int postalComparison = postalCode.compareTo(otherPostalCode);

        return (countryComparison * GlobalVariable.SORT_COUNTRY_WEIGHT) + postalComparison;
    }
}