package com.darzul.hoshiyo.wictrip.service;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;

import com.darzul.hoshiyo.wictrip.activity.MapsActivity;
import com.darzul.hoshiyo.wictrip.dao.PictureDao;
import com.darzul.hoshiyo.wictrip.dao.PlaceDao;
import com.darzul.hoshiyo.wictrip.entity.Picture;
import com.darzul.hoshiyo.wictrip.entity.Place;

import java.io.IOException;
import java.util.List;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 30/10/2014.
 */
public class PictureReverseGeocoding {

    private static final String TAG = "Picture service";
    private static final long GEOCODER_LIMIT_TIME_PER_SEC = 200;

    private Geocoder mGeocoder = null;
    private MapsActivity mActivity;

    public PictureReverseGeocoding(MapsActivity activity) {
        mGeocoder = new Geocoder(activity);
        mActivity = activity;
    }

    public void start() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (computeUserPictures()) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.refreshData();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Reverse geocoding with all user pictures to get country, locality
     * Store the picture info into local db (date taken, uri and position)
     *
     * @return boolean to notify if Dao have to fetch data or not
     */
    public boolean computeUserPictures() throws IOException {

        // State to notify if there is new datas
        boolean haveToRefresh = false;

        // Get user pictures
        Cursor cursors[] = new Cursor[2];

        // from SD card
        cursors[0] = mActivity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        // and from intern memory
        cursors[1] = mActivity.getContentResolver().query(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI, null, null, null,
                null);

        // merge all picture from these two storage
        Cursor mergeCursor = new MergeCursor(cursors);

        int dataColumnId = mergeCursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int dateTakenColumnId = mergeCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
        int lngColumnId = mergeCursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
        int latColumnId = mergeCursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);

        PlaceDao placeDao = PlaceDao.getInstance();
        PictureDao pictureDao = PictureDao.getInstance();
        Picture picture;
        String uri;
        String countryName, countryCode = null, locality, postalCode = null;
        double lng, lat;
        long dateTaken;
        float dist;
        List<Address> addresses;

        Location currentLocation = new Location("");
        Location previousLocation = new Location("");
        previousLocation.setLatitude(0);
        previousLocation.setLongitude(0);

        mergeCursor.moveToFirst();
        while (!mergeCursor.isAfterLast()) {
            uri = mergeCursor.getString(dataColumnId);

            // Picture is not in the DB
            if (!pictureDao.uriExist(uri)) {
                haveToRefresh = true;

                dateTaken = mergeCursor.getLong(dateTakenColumnId);

                // Get lat and lng in the picture meta data
                lat = mergeCursor.getDouble(latColumnId);
                lng = mergeCursor.getDouble(lngColumnId);

                // Geo tag available
                if (lng != 0 && lat != 0) {
                    currentLocation.setLatitude(lat);
                    currentLocation.setLongitude(lng);
                    dist = currentLocation.distanceTo(previousLocation);

                    if (dist > 1000) {
                        addresses = mGeocoder.getFromLocation(lat, lng, 10);
                        countryName = null;
                        countryCode = null;
                        locality = null;
                        postalCode = null;
                        // There is at least 1 address
                        if (addresses.size() >= 1) {
                            // Check addresses to get CountryCode and PostalCode
                            for (Address address : addresses) {
                                if (countryName == null)
                                    countryName = address.getCountryName();
                                if (countryCode == null)
                                    countryCode = address.getCountryCode();
                                if (locality == null)
                                    locality = address.getLocality();
                                if (postalCode == null)
                                    postalCode = address.getPostalCode();

                                if (countryName != null && countryCode != null && locality != null && postalCode != null)
                                    break;
                            }

                            // Create country place if possible
                            if (countryCode != null && countryName != null) {
                                placeDao.create(new Place(-1, countryName, countryCode, null, null, lat, lng, true));
                                // Create locality if we get locality and postal code
                                if (postalCode != null && locality != null)
                                    placeDao.create(new Place(-1, countryName, countryCode, locality, postalCode, lat, lng, true));
                            }

                            Log.d(TAG, "Geolocalisable - country: " + countryCode + " Postal code: " + postalCode);
                        }
                        // Request to reverse geocoding has a limit time per sec
                        SystemClock.sleep(GEOCODER_LIMIT_TIME_PER_SEC);

                        previousLocation.setLatitude(lat);
                        previousLocation.setLongitude(lng);
                    }

                    picture = new Picture(-1, uri, countryCode, postalCode, lat, lng, dateTaken);
                }
                // No data to geo tag in the picture
                else {
                    picture = new Picture(-1, uri, null, null, lat, lng, dateTaken);
                }

                Log.d(TAG, "New picture: " + uri + " (" + lat + ", " + lng + ") " + dateTaken);

                // Add the picture object to DB
                pictureDao.create(picture);
            } else {
                Log.d(TAG, "Picture already in the DB");
            }

            mergeCursor.moveToNext();
        }

        cursors[0].close();
        cursors[1].close();
        mergeCursor.close();

        return haveToRefresh;
    }
}