package com.example.hoshiyo.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.hoshiyo.BitmapDecoder;
import com.example.hoshiyo.wictrip.entity.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 03/11/2014.
 */
public class PictureGridViewAdapter extends BaseAdapter {

    private Activity mContext;
    private List<Picture> mPictures;
    private LayoutInflater mLayoutInflater = null;

    public PictureGridViewAdapter(Activity context, ArrayList<Picture> pictures) {
        mContext = context;
        mPictures = new ArrayList<Picture>(pictures);
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mPictures.size();
    }

    @Override
    public Object getItem(int position) {
        return mPictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mPictures.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Bitmap bm;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            //TODO change dim to res file
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        BitmapDecoder.loadBitmap(mPictures.get(position).getUri().toString(), imageView);

        return imageView;
    }
}
