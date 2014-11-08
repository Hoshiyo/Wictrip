package com.darzul.hoshiyo.wictrip.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.darzul.hoshiyo.wictrip.GlobalVariable;
import com.darzul.hoshiyo.wictrip.adapter.PictureGridViewAdapter;
import com.darzul.hoshiyo.wictrip.R;
import com.darzul.hoshiyo.wictrip.dao.PictureDao;
import com.darzul.hoshiyo.wictrip.entity.Album;
import com.darzul.hoshiyo.wictrip.entity.Picture;


import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 05/11/2014.
 */
public class Gallery extends Fragment {

    private static final String TAG = "Gallery";

    private Album mAlbum;
    private PictureGridViewAdapter mAdapter;
    private TextView mIndicator;
    private OnFragmentInteractionListener mListener;

    public static Gallery newInstance(Album album) {
        Gallery fragment = new Gallery();
        Bundle args = new Bundle();
        args.putSerializable(GlobalVariable.ARG_ALBUM, album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) {
            mAlbum = (Album) getArguments().getSerializable(GlobalVariable.ARG_ALBUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_gallery, container, false);

        GridView gridView = (GridView) v.findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.isSelected(position)) {
                    mAdapter.deselect((ImageView) view, position);
                }
                else {
                    mAdapter.select((ImageView) view, position);
                }

                refreshIndicator();
            }
        });

        // Get all pictures
        Collection<Object> picturesObj = PictureDao.getInstance().getAll();
        ArrayList<Picture> pictures = new ArrayList<Picture>();
        for (Object obj : picturesObj) {
            pictures.add((Picture) obj);
        }
        mAdapter = new PictureGridViewAdapter(getActivity(), pictures);

        gridView.setAdapter(mAdapter);

        // Inflate menu
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        View menu = getActivity().getLayoutInflater().inflate(R.layout.menu_gallery, container, false);
        actionBar.setCustomView(menu);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mIndicator = (TextView) menu.findViewById(R.id.select_indicator);
        refreshIndicator();
        menu.findViewById(R.id.validate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addSelection(mAlbum);
                onValidatePressed();
            }
        });

        actionBar.show();

        return v;
    }

    private void refreshIndicator() {
        int size = mAdapter.getPictureSelectedSize();
        mIndicator.setText(size + " " + getResources().getQuantityString(R.plurals.picture_selected, size));
    }

    public void onValidatePressed() {
        if (mListener != null) {
            mListener.onGalleryValidated();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener.onGalleryDestroyed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onGalleryValidated();
        public void onGalleryDestroyed();
    }
}
