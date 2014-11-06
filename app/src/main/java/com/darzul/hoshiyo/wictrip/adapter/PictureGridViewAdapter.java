package com.darzul.hoshiyo.wictrip.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.darzul.hoshiyo.wictrip.GlobalVariable;
import com.darzul.hoshiyo.wictrip.dao.AlbumDao;
import com.darzul.hoshiyo.wictrip.entity.Album;
import com.darzul.hoshiyo.wictrip.entity.Picture;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 03/11/2014.
 */
public class PictureGridViewAdapter extends BaseAdapter {

    private static final String TAG = "Picture adapter";
    private static final int PICTURE_SIZE = GlobalVariable.PICTURE_SIZE;
    private Activity mContext;
    private List<Picture> mPictures;
    private List<Picture> mPicturesSelected;
    Picasso picasso;

    public PictureGridViewAdapter(Activity context, ArrayList<Picture> pictures) {
        mContext = context;
        mPictures = new ArrayList<Picture>(pictures);
        mPicturesSelected = new ArrayList<Picture>();
        picasso = Picasso.with(mContext);
        picasso.setIndicatorsEnabled(true);
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
            imageView.setLayoutParams(new GridView.LayoutParams(PICTURE_SIZE, PICTURE_SIZE));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        if(mPicturesSelected.contains(mPictures.get(position))) {
            imageView.setAlpha(0x55);
        }
        else {
            imageView.setAlpha(0xFF);
        }

        File f = new File(mPictures.get(position).getUri());
        picasso.load(f)
                .resize(PICTURE_SIZE, PICTURE_SIZE)
                .centerCrop()
                .into(imageView);

        return imageView;
    }

    public void setNewPictureList(ArrayList<Picture> pictures) {
        mPictures = new ArrayList<Picture>(pictures);
        mPicturesSelected.clear();
        notifyDataSetChanged();
    }

    public void select(ImageView imageView, int position) {
        imageView.setAlpha(0x55);
        mPicturesSelected.add(mPictures.get(position));
    }

    public void deselect(ImageView imageView, int position) {
        imageView.setAlpha(0xFF);
        mPicturesSelected.remove(mPictures.get(position));
    }

    public void addSelection(Album album) {
        for(Picture picture : mPicturesSelected) {
            album.addPicture(picture);
        }

        AlbumDao.getInstance().update(album);
    }

    public void deleteSelection(Album album) {
        if(mPicturesSelected.size() == 0)
            return;

        AlbumDao albumDao = AlbumDao.getInstance();
        for(Picture picture : mPicturesSelected) {
            albumDao.deletePicture(album, picture);
            mPictures.remove(picture);
        }
        notifyDataSetChanged();
        mPicturesSelected.clear();
    }

    public void addPicture(Picture picture) {
        mPictures.add(picture);
        notifyDataSetChanged();
    }

    public int getPictureSelectedSize() {
        return mPicturesSelected.size();
    }

    public boolean isSelected(int position) {
        return mPicturesSelected.contains(mPictures.get(position));
    }
}
