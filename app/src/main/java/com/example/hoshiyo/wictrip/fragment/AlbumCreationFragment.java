package com.example.hoshiyo.wictrip.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoshiyo.wictrip.DatabaseHelper;
import com.example.hoshiyo.wictrip.R;
import com.example.hoshiyo.wictrip.dao.AlbumDao;
import com.example.hoshiyo.wictrip.dao.PictureDao;
import com.example.hoshiyo.wictrip.dao.PlaceDao;
import com.example.hoshiyo.wictrip.entity.Album;
import com.example.hoshiyo.wictrip.entity.Picture;
import com.example.hoshiyo.wictrip.entity.Place;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public class AlbumCreationFragment extends Fragment {

    // Result code for datepicker dialog
    private static final int DATE_DIALOG_FROM = 1;
    private static final int DATE_DIALOG_TO = 2;

    private static final String TAG = "Album Creation Fragment";

    TextView mName = null;
    Spinner mPlaceSpinner = null;
    Calendar mCalendarFrom = null;
    Calendar mCalendarTo = null;
    TextView mTextViewFrom = null;
    TextView mTextViewTo = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_album_creation, null, false);

        mName = (TextView) v.findViewById(R.id.album_name);

        //TODO Supprimer
//        PlaceDao.getInstance().create(new Place(-1, "Thailand", "TH", null, null, new LatLng(15.275766, 101.006706)));
//        PlaceDao.getInstance().create(new Place(-1, "Thailand", "TH", "Bangkok", "10150", new LatLng(15.275766, 101.006706)));
//        PlaceDao.getInstance().create(new Place(-1, "France", "FR", "Charenton-le-pont", "94220", new LatLng(48.821656, 2.413280)));

        DatabaseHelper.printDB();

        mPlaceSpinner = (Spinner) v.findViewById(R.id.places_spinner);
        Collection<Object> placesObj = PlaceDao.getInstance().getAll();
        ArrayList<Place> places = new ArrayList<Place>();
        for (Object obj : placesObj) {
            places.add((Place) obj);
        }
        ArrayAdapter<Place> placeAdapter = new ArrayAdapter<Place>(getActivity(),
                android.R.layout.simple_list_item_1, places);
        mPlaceSpinner.setAdapter(placeAdapter);

        mTextViewFrom = (TextView) v.findViewById(R.id.date_picker_from);
        mTextViewFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dp = new DatePickerFragment();
                dp.setTargetFragment(AlbumCreationFragment.this, DATE_DIALOG_FROM);

                Bundle bundle = new Bundle();
                bundle.putSerializable(DatePickerFragment.KEY_DATE, mCalendarFrom);
                dp.setArguments(bundle);

                dp.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
        mTextViewTo = (TextView) v.findViewById(R.id.date_picker_to);
        mTextViewTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dp = new DatePickerFragment();
                dp.setTargetFragment(AlbumCreationFragment.this, DATE_DIALOG_TO);

                Bundle bundle = new Bundle();
                bundle.putSerializable(DatePickerFragment.KEY_DATE, mCalendarTo);
                dp.setArguments(bundle);

                dp.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        Button btn = (Button) v.findViewById(R.id.create_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if album name is not empty
                String albumName = mName.getText().toString();
                if (albumName.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.warning_album_name_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                long timeBegin;
                long timeEnd;
                if (mCalendarFrom == null) {
                    timeBegin = 0;
                } else {
                    timeBegin = mCalendarFrom.getTimeInMillis();
                }
                if (mCalendarTo == null) {
                    timeEnd = Calendar.getInstance().getTimeInMillis();
                } else {
                    timeEnd = mCalendarTo.getTimeInMillis();
                }

                Place albumPlace = (Place) mPlaceSpinner.getSelectedItem();
                Collection<Picture> albumPictures = getFilteredPictures(albumPlace, timeBegin, timeEnd);

                if (albumPictures != null) {
                    Log.d(TAG, albumPictures.size() + " picture(s) into the new Album");
                    for (Picture picture : albumPictures)
                        Log.d(TAG, "Id: " + picture.getId());
                }

                AlbumDao.getInstance().create(new Album(-1, albumName, albumPictures, timeBegin, timeEnd, albumPlace));
            }
        });

        return v;
    }

    /**
     * @param albumPlace Place where the pictures was taken
     * @param timeBegin  Time in ms which represent the begin date of the album
     * @param timeEnd    Calendar with the ending date
     * @return List of pictures corresponding to the Place, beginDate and endDate
     */
    private Collection<Picture> getFilteredPictures(Place albumPlace, long timeBegin, long timeEnd) {

        String targetCountryCode = albumPlace.getCountryCode();
        String targetPostalCode = albumPlace.getPostalCode();

        Collection<Object> pictures = PictureDao.getInstance().getAll();
        Collection<Picture> picturesSelected = new ArrayList<Picture>();
        Picture picture;
        long dateTaken;
        String pictureCountryCode, picturePostalCode;
        for (Object obj : pictures) {
            picture = (Picture) obj;
            dateTaken = picture.getDateTaken();
            pictureCountryCode = picture.getCountryCode();
            picturePostalCode = picture.getPostalCode();
            // Picture is between the begin and ending date
            if (dateTaken > timeBegin && dateTaken < timeEnd) {
                // Picture has the same country
                if (pictureCountryCode != null && pictureCountryCode.equals(targetCountryCode)) {
                    if (targetPostalCode == null || picturePostalCode != null && picturePostalCode.equals(targetPostalCode))
                        picturesSelected.add(picture);
                }
            }
        }


        return picturesSelected;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView textView = null;

        if (requestCode == DATE_DIALOG_FROM) {
            mCalendarFrom = (Calendar) data.getSerializableExtra(DatePickerFragment.KEY_DATE);
            if (mCalendarFrom != null) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                mTextViewFrom.setText(format.format(mCalendarFrom.getTime()));
            }
        } else if (requestCode == DATE_DIALOG_TO) {
            mCalendarTo = (Calendar) data.getSerializableExtra(DatePickerFragment.KEY_DATE);
            if (mCalendarTo != null) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                mTextViewTo.setText(format.format(mCalendarTo.getTime()));
            }
        }
    }
}