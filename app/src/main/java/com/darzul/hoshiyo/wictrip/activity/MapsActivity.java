package com.darzul.hoshiyo.wictrip.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.darzul.hoshiyo.wictrip.GlobalVariable;
import com.darzul.hoshiyo.wictrip.NavDrawerChild;
import com.darzul.hoshiyo.wictrip.NavDrawerGroup;
import com.darzul.hoshiyo.wictrip.adapter.MyAdapter;
import com.darzul.hoshiyo.wictrip.dao.AlbumDao;
import com.darzul.hoshiyo.wictrip.dao.PlaceDao;
import com.darzul.hoshiyo.wictrip.entity.Album;
import com.darzul.hoshiyo.wictrip.entity.Place;
import com.darzul.hoshiyo.wictrip.fragment.AlbumCreationFragment;
import com.darzul.hoshiyo.wictrip.fragment.AlbumPictures;
import com.darzul.hoshiyo.wictrip.fragment.Gallery;
import com.darzul.hoshiyo.wictrip.R;
import com.darzul.hoshiyo.wictrip.fragment.Setting;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements LocationListener,
        AlbumPictures.OnFragmentInteractionListener,
        Gallery.OnFragmentInteractionListener,
        AlbumCreationFragment.OnFragmentInteractionListener {

    private static final long TIME_BETWEEN_TWO_POINTS = 50000;
    private static final float DISTANCE_BETWEEN_TWO_POINTS = 3000;

    private static final String TAG = "MapActivity";


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    //private static String PROX_ALERT = "String fr.esiea.mobile.prox";

    private DrawerLayout drawerLayout = null;
    private ListView drawer_menu = null;

    EditText searchLocation = null;
    Button goLocation = null;
    Button open_close_drawer = null;
    Button addLocation = null;

    // nav drawer title
    private CharSequence mDrawerTitle = null;

    // used to store app title
    private CharSequence mTitle = null;

    // slide menu items
    private String[] navMenuHeaders = null;
    private TypedArray navMenuIcons = null;
    private String[] navMenuItemName = null;

    //private ArrayList<NavDrawerChild> navDrawerItems = null;
    private ArrayList<String> navDrawerItems = null;


    DrawerLayout mDrawerLayout;
    ExpandableListAdapter mAdapter;
    ExpandableListView mDrawerList;
    ArrayList<NavDrawerGroup> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        //load map
        setUpMapIfNeeded();

        mTitle = mDrawerTitle = getTitle();

        initButton();

        loadResources();

        //setItemData();

        initDrawer();

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return displayView(groupPosition);
            }
        });

        open_close_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        // Defining button click event listener for the find button
        Button.OnClickListener findClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location
                searchLocation = (EditText) findViewById(R.id.locationSearch);

                // Getting user input location
                String location = searchLocation.getText().toString();

                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        };

        // Setting button click event listener for the find button
        goLocation.setOnClickListener(findClickListener);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //access different radar

        //IntentFilter filter = new IntentFilter(PROX_ALERT);
        //registerReceiver(new MyLocationPromixityReceiver(this), filter);
    }

    private void setSubItemData() {
        //listDataHeader = new ArrayList<NavDrawerChild>();
        listDataHeader = new ArrayList<NavDrawerGroup>();
        List<NavDrawerChild> children = null;

        Collection<Object> places = PlaceDao.getInstance().getAll();
        Place place;

        Collection<Object> albums = AlbumDao.getInstance().getAll();
        Album album;

        // Adding child data
        listDataHeader.add(new NavDrawerGroup(navMenuItemName[0], -1, null));
        listDataHeader.add(new NavDrawerGroup(navMenuItemName[1], R.drawable.ic_menu_add, null));
        listDataHeader.add(new NavDrawerGroup(navMenuItemName[2], R.drawable.ic_menu_add, null));

        children = new ArrayList<NavDrawerChild>();
        for(Object obj : places) {
            place = (Place) obj;
            children.add(new NavDrawerChild(place.toString(), R.drawable.ic_menu_add, -1));
        }
        listDataHeader.add(new NavDrawerGroup(navMenuItemName[3], -1, children));
        listDataHeader.add(new NavDrawerGroup(navMenuItemName[4], -1, null));
        listDataHeader.add(new NavDrawerGroup(navMenuItemName[5], R.drawable.ic_menu_add, null));

        children = new ArrayList<NavDrawerChild>();
        for(Object obj : albums) {
            album = (Album) obj;
            children.add(new NavDrawerChild(album.toString(), R.drawable.ic_menu_add, -1));
        }
        listDataHeader.add(new NavDrawerGroup(navMenuItemName[6], -1, children));
        listDataHeader.add(new NavDrawerGroup(navMenuItemName[7], -1, null));
        listDataHeader.add(new NavDrawerGroup(navMenuItemName[8], R.drawable.ic_settings, null));
    }

    private void loadResources() {
        // load slide header menu items, nav drawer icons from resources and av drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        navMenuItemName = getResources().getStringArray(R.array.nav_drawer_items);
    }

    private void initButton() {
        //Recuperation items from layouts
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        open_close_drawer = (Button)findViewById(R.id.open_close_drawer);
        goLocation = (Button)findViewById(R.id.locationGo);
        addLocation = (Button) findViewById(R.id.addPlace);
    }

    private void setItemData() {
        //navDrawerItems = new ArrayList<NavDrawerChild>();

        // adding nav drawer items to array
        // Place
        /*navDrawerItems.add(new NavDrawerChild(navMenuHeaders[0]));
        // Already visited
        navDrawerItems.add(new NavDrawerChild(navMenuItemName[0], navMenuIcons.getResourceId(0, -1)));
        // To visit
        navDrawerItems.add(new NavDrawerChild(navMenuItemName[1], navMenuIcons.getResourceId(0, -1)));
        // Album
        navDrawerItems.add(new NavDrawerChild(navMenuHeaders[1]));
        // Create
        navDrawerItems.add(new NavDrawerChild(navMenuItemName[3], navMenuIcons.getResourceId(0, -1)));
        // Friends
        navDrawerItems.add(new NavDrawerChild(navMenuHeaders[2]));
        // Parameters
        navDrawerItems.add(new NavDrawerChild(navMenuHeaders[3]));*/

        // Recycle the typed array
        navMenuIcons.recycle();

    }

    private void initDrawer() {
        //Definition of menu content
        /*drawer_menu = (ListView)findViewById(R.id.slider_menu);

        // setting the nav drawer list adapter
        itemAdapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        drawer_menu.setAdapter(itemAdapter);*/


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) findViewById(R.id.lvExp);

        setSubItemData();

        mAdapter = new MyAdapter(this, listDataHeader);
        mDrawerList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events (Facebook SDK)
        AppEventsLogger.activateApp(this);

        setUpMapIfNeeded();

        //Listen location's change with criteria and say it to manager
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String theBestProvider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(
                theBestProvider,
                TIME_BETWEEN_TWO_POINTS,
                DISTANCE_BETWEEN_TWO_POINTS,
                this
        );
    }

    //Stop listener when we are on pause
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event (Facebook SDK)
        AppEventsLogger.deactivateApp(this);

        locationManager.removeUpdates(this);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getBaseContext(), "Location changed", Toast.LENGTH_SHORT).show();
        /*if(mMap != null) {
            LatLng myLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        }*/
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Toast.makeText(getBaseContext(), "Status changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(getBaseContext(), "Provider enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getBaseContext(), "Provider disabled", Toast.LENGTH_SHORT).show();
    }

    /*private void setUpProximity() {
        double latLocation = 48.81454;
        double lngLocation = 2.37888;
        float radius = 100;
        long expiration = -1; //never
        Intent intent = new Intent(PROX_ALERT); //intention a lever en cas d'entrée/sortie
        PendingIntent proxIntent = PendingIntent.getBroadcast(this, -1, intent, 0);

        locationManager.addProximityAlert(latLocation, lngLocation, radius, expiration, proxIntent); //add a position to listen
    }*/

    /**
     * Méthode appelée lors de la sélection d'un élément du menu
     *
     * @param item object in listView
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            mMap.clear();

            // Adding Markers on Google Map for each matching address
            for (int i = 0; i < addresses.size(); i++) {

                Address address = addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                mMap.addMarker(markerOptions);

                // Locate the first location
                if (i == 0)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

    /**
     * Slide menu item click listener
     * */

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            Log.d("DEBUG","Miracleeeeeeeeeeeeeeeeee");
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private boolean displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;


        switch (position) {
            case 1:
                Collection<Object> placeDaos = PlaceDao.getInstance().getAll();
                Place place;

                for(Object obj : placeDaos) {
                    place = (Place) obj;
                    mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng())).title(place.getCountryName()));
                }
                break;
            case 2:
                Toast.makeText(getApplication(),"To go",Toast.LENGTH_SHORT).show();
                break;
            case 5:
                fragment = new AlbumCreationFragment();
                break;
            case 8:
                Toast.makeText(getApplication(),"Log in",Toast.LENGTH_SHORT).show();
                fragment = new Setting();
                break;
            default:
                return false;
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_field, fragment)
                    .addToBackStack(null)
                    .commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            drawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

        return true;
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
