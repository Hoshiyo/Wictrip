package com.example.hoshiyo.wictrip.fragment;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoshiyo.GlobalVariable;
import com.example.hoshiyo.adapter.PictureGridViewAdapter;
import com.example.hoshiyo.wictrip.R;
import com.example.hoshiyo.wictrip.dao.AlbumDao;
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

    private PictureGridViewAdapter mAdapter = null;
    private Album mAlbum;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param album The album use to display
     * @return A new instance of fragment AlbumPictures.
     */
    public static AlbumPictures newInstance(Album album) {
        AlbumPictures fragment = new AlbumPictures();
        Bundle args = new Bundle();
        args.putSerializable(GlobalVariable.ARG_ALBUM, album);
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
            mAlbum = (Album) getArguments().getSerializable(GlobalVariable.ARG_ALBUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_album_pictures, container, false);

        GridView gridView = (GridView) v.findViewById(R.id.gridView);
        Collection<Picture> pictures = mAlbum.getPictures();
        mAdapter = new PictureGridViewAdapter(getActivity(), (java.util.ArrayList<Picture>) pictures);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.isSelected(position)) {
                    mAdapter.deselect((ImageView) view, position);
                } else {
                    mAdapter.select((ImageView) view, position);
                }
            }
        });

        createMenu();

        return v;
    }


    /**
     * Create action bar menu
     */
    private void createMenu() {
        // Create custom menu
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        View menu = getActivity().getLayoutInflater().inflate(R.layout.menu_album_pictures, null);
        actionBar.setCustomView(menu);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // Set album name in action bar
        TextView textView = (TextView) menu.findViewById(R.id.title);
        textView.setText(mAlbum.getName());

        ImageView imageView = (ImageView) menu.findViewById(R.id.item_add);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickAddPicture(mAlbum);
                }
            }
        });

        imageView = (ImageView) menu.findViewById(R.id.item_delete);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nbPicturesSelected = mAdapter.getPictureSelectedSize();
                if (mAdapter.getPictureSelectedSize() > 0) {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.dialog_title_delete)
                            .setMessage(getActivity().getResources().getQuantityString(R.plurals.dialog_really_delete,
                                    nbPicturesSelected))
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAdapter.deleteSelection(mAlbum);
                                }

                            })
                            .setNegativeButton(R.string.no, null)
                            .show();
                }
            }
        });
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
        public void onClickAddPicture(Album album);
    }

    public void refreshAlbum() {
        createMenu();
        mAlbum = AlbumDao.getInstance().getItemById(mAlbum.getId());
        mAdapter.setNewPictureList((java.util.ArrayList<Picture>) mAlbum.getPictures());
        mAdapter.notifyDataSetChanged();
    }
}
