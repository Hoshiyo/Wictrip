package com.example.hoshiyo.wictrip.fragment;


import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoshiyo.adapter.PictureGridViewAdapter;
import com.example.hoshiyo.wictrip.R;
import com.example.hoshiyo.wictrip.entity.Album;
import com.example.hoshiyo.wictrip.entity.Picture;

import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumPictures.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumPictures#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumPictures extends Fragment {
    private static final String TAG = "Album pictures";

    // the fragment initialization parameters album
    public static final String ARG_ALBUM = "album";

    private Album mAlbum;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param album The album use to display
     * @return A new instance of fragment AlbumPictures.
     */
    public static AlbumPictures newInstance(String album) {
        AlbumPictures fragment = new AlbumPictures();
        Bundle args = new Bundle();
        args.putString(ARG_ALBUM, album);
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumPictures() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlbum = (Album) getArguments().getSerializable(ARG_ALBUM);
        }

        // Create custom menu
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        View menu = getActivity().getLayoutInflater().inflate(R.layout.menu_album_pictures, null);

        TextView textView = (TextView) menu.findViewById(R.id.title);
        textView.setText(mAlbum.getName());

        ImageView imageView = (ImageView) menu.findViewById(R.id.item_add);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        imageView = (ImageView) menu.findViewById(R.id.item_delete);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        actionBar.setCustomView(menu);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_album_pictures, container, false);

        GridView gridView = (GridView) v.findViewById(R.id.gridView);
        Collection<Picture> pictures = mAlbum.getPictures();
        PictureGridViewAdapter adapter = new PictureGridViewAdapter(getActivity(), (java.util.ArrayList<Picture>) pictures);
        gridView.setAdapter(adapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
