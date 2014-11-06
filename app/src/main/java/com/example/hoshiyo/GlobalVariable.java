package com.example.hoshiyo;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 05/11/2014.
 */
public class GlobalVariable {
    public final static String DISK_CACHE_SUBDIR = "thumbnails";
    public final static int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    public static int PICTURE_SIZE;

    public final static int SORT_COUNTRY_WEIGHT = 100;
    public final static int SORT_LOCALITY_WEIGHT = 1;

    public final static String ARG_ALBUM = "album"; // Arg in Bundle to specify an album to a Fragment

    public final static String FRAG_GALLERY = "gallery"; // Frag id for the gallery fragment
    public final static String FRAG_ALBUM = "album"; // Frag id for the album fragment
}
