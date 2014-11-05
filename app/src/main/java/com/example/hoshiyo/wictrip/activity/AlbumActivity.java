package com.example.hoshiyo.wictrip.activity;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.example.hoshiyo.wictrip.R;
import com.example.hoshiyo.wictrip.dao.AlbumDao;
import com.example.hoshiyo.wictrip.dao.PictureDao;
import com.example.hoshiyo.wictrip.dao.PlaceDao;
import com.example.hoshiyo.wictrip.entity.Album;
import com.example.hoshiyo.wictrip.fragment.AlbumCreationFragment;
import com.example.hoshiyo.wictrip.fragment.AlbumPictures;

public class AlbumActivity extends FragmentActivity implements AlbumPictures.OnFragmentInteractionListener {

    private static final String TAG = "Album Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        if (savedInstanceState == null) {
            Fragment fragment = new AlbumPictures();
            Bundle bundle = new Bundle();
            Album album = AlbumDao.getInstance().getItemById(2);
            bundle.putSerializable(AlbumPictures.ARG_ALBUM, album);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, null)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO
    }
}
