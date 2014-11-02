package com.example.hoshiyo.wictrip.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public final static String KEY_DATE = "date";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int year, month, day;

        Bundle bundle = getArguments();
        if (bundle != null) {
            Calendar calendar = (Calendar) bundle.getSerializable(KEY_DATE);
            if (calendar != null)
                c = calendar;
        }

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Intent intent = new Intent();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        intent.putExtra(KEY_DATE, c);

        getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
    }
}