package com.darzul.hoshiyo.wictrip.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.darzul.hoshiyo.wictrip.GlobalVariable;
import com.example.hoshiyo.wictrip.R;
import com.darzul.hoshiyo.wictrip.dao.AlbumDao;
import com.darzul.hoshiyo.wictrip.entity.Album;
import com.darzul.hoshiyo.wictrip.fragment.AlbumCreationFragment;
import com.darzul.hoshiyo.wictrip.fragment.AlbumPictures;
import com.darzul.hoshiyo.wictrip.fragment.Gallery;

public class AlbumActivity extends FragmentActivity
        implements AlbumPictures.OnFragmentInteractionListener,
        Gallery.OnFragmentInteractionListener,
        AlbumCreationFragment.OnFragmentInteractionListener {

    private static final String TAG = "Album Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        if (savedInstanceState == null) {
            Fragment fragment = AlbumPictures.newInstance(AlbumDao.getInstance().getItemById(2));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, AlbumCreationFragment.newInstance(), GlobalVariable.FRAG_ALBUM)
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
    public void onClickAddPicture(Album album) {
        Fragment fragment = Gallery.newInstance(album);
        getSupportFragmentManager()
                .beginTransaction().addToBackStack(null)
                .add(R.id.container, fragment, GlobalVariable.FRAG_GALLERY)
                .commit();
    }

    @Override
    public void onGalleryValidated() {
        onBackPressed();
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(GlobalVariable.FRAG_ALBUM);
        if(fragment != null) {
            if(fragment instanceof AlbumPictures) {
                AlbumPictures albumPictures = (AlbumPictures) fragment;
                albumPictures.refreshAlbum();
            }
        }
    }

    @Override
    public void onCreateAlbum(Album album) {
        onBackPressed();
        AlbumDao.getInstance().create(album);
        //TODO refresh album list
    }
}
