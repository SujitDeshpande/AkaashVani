package com.locastio.akaashvani;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.locastio.akaashvani.data.Group;
import com.locastio.akaashvani.data.UserGroup;
import com.locastio.akaashvani.services.LocationAPI;
import com.locastio.akaashvani.services.LocationTrackerAPI;
import com.locastio.akaashvani.services.UserGroupAPI;
import com.locastio.akaashvani.util.GPSTracker;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapActivity extends BaseActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, LocationAPI.Callback, GPSTracker.Callback, UserGroupAPI.Callback, LocationTrackerAPI.Callback {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 5 * 1; // 1 minute

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private GoogleMap googleMap;
    protected LocationManager locationManager;
    private android.location.LocationListener locationListener;

    List<Markers> markers = new ArrayList<Markers>();


    GPSTracker gpsTracker;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    //Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000; //1000 * 5 * 1; // 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        String strGroupObjId = getIntent().getStringExtra("groupObjId");

        UserGroupAPI userGroupAPI = new UserGroupAPI(this);
        userGroupAPI.getUserGroupsOfGroupId(strGroupObjId);
        Log.d("k10:", strGroupObjId);


        gpsTracker = new GPSTracker(this, this);
//        gpsTracker.getLocation();

        markers = getData();
        //setContentView(R.layout.activity_maps);
        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        /*if (!isGooglePlayServicesAvailable()) {
            finish();
        }*/
        createLocationRequest();
        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();*/


        setContentView(R.layout.activity_map);


        getLocation();
 /*       MapFragment fm;
        fm = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.activity_map, fm);
        fragmentTransaction.commit();*/
//        MapFragment fm = new MapFragment();
        /*MapFragment fm = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);*/
//        googleMap = fm.getMap();
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        googleMap.setMyLocationEnabled(true);
//        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                getLocation();
//                addMarker();
//                return true;
//            }
//        });
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public static List<Markers> getData (){
        List<Markers> data = new ArrayList<>();
        double[] lats = {12.9715011, 12.968064};
        double[] longs = {77.5959849, 77.5950677};
        for (int i=0; (i < lats.length); i++){
            Markers current = new Markers();
            current.latitude = lats [i];
            current.longitude = longs [i];
            data.add(current);
        }
        return data;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        //mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        //mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: ");
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    protected void startLocationUpdates() {
/*        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);*/
        getLocation();
        Log.d(TAG, "Location update started ..............: ");
    }

    private void addMarkers() {
        googleMap.clear();
        for (int i = 0; i < markers.size(); i++) {
            Markers currentMarker = markers.get(i);

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(currentMarker.latitude, currentMarker.longitude));

            googleMap.addMarker(markerOptions);
        }
    }

    private void addMarker() {
        MarkerOptions options = new MarkerOptions();

        // following four lines requires 'Google Maps Android API Utility Library'
        // https://developers.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info
        /*IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());*/

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        options.position(currentLatLng);

        //mMarker = googleMap.addMarker(options);
        Log.d(TAG, "Marker added.............................");
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                17));
        Log.d(TAG, "Zoom done.............................");
        addMarkers();
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationListener = new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mCurrentLocation = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (mCurrentLocation == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            mCurrentLocation = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (mCurrentLocation != null) {
                                latitude = mCurrentLocation.getLatitude();
                                longitude = mCurrentLocation.getLongitude();
                            }
                            Toast.makeText(
                                    getApplicationContext(),
                                    "GPS Location is - \nLat: " + latitude
                                            + "\nLong: " + longitude,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "GPS Not Enabled",
                            Toast.LENGTH_LONG).show();
                    if (isNetworkEnabled) {

                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            mCurrentLocation = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (mCurrentLocation != null) {
                                latitude = mCurrentLocation.getLatitude();
                                longitude = mCurrentLocation.getLongitude();
                            }

                            Toast.makeText(
                                    getApplicationContext(),
                                    "Network Location is - \nLat: " + latitude
                                            + "\nLong: " + longitude,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }

                addMarker();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
/*        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);*/
        Log.d(TAG, "Location update stopped .......................");
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        //}
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: ");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        addMarker();

        ParseUser currentUser = ParseUser.getCurrentUser();
        LocationAPI locationAPI = new LocationAPI(this);
        locationAPI.updateLocationOfUser(currentUser, location);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }


    @Override
    public void didUpdateLocation(Location location) {
        LocationAPI locationAPI = new LocationAPI(null);
        ParseUser currentUser = ParseUser.getCurrentUser();
        locationAPI.updateLocationOfUser(currentUser, location);
    }

    @Override
    public void didDataChanged(DataSnapshot dataSnapshot) {
        System.out.println("There are " + dataSnapshot.getChildrenCount() + " blog posts");
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            Location location = postSnapshot.getValue(Location.class);
            System.out.println(location.getLatitude() + " - " + location.getLongitude());
        }
    }

    @Override
    public void didCancelled() {

    }

    //UserGroupAPI.Callback methods
    @Override
    public void didRetrievedMyGroups(List<ParseObject> groupList) {

    }
    @Override
    public void didRetrievedUserGroups(List<ParseObject> userGroupObjList, Group group) {

        this.trackLocationOfGroup(userGroupObjList);

    }
    @Override
    public void didRetrieveGrpFailed(String s) {

    }
    @Override
    public void didRetrieveUserGroupFailed(String s) {

    }

    List<LocationTrackerAPI> locationTrackerAPIList = new ArrayList<LocationTrackerAPI>();
    // Location Tracking methods
    void trackLocationOfGroup(List<ParseObject> userGroupList) {
        for (ParseObject parseObject: userGroupList) {
            UserGroup userGroup = (UserGroup) parseObject;
            LocationTrackerAPI locationTrackerAPI = new LocationTrackerAPI(this);
            locationTrackerAPI.trackUserLocation(userGroup.getUser());
            locationTrackerAPIList.add(locationTrackerAPI);
        }
    }

    void stopTrackingGroup() {
        for (LocationTrackerAPI locationTrackerAPI: locationTrackerAPIList) {
            locationTrackerAPI.removeTrackingForUser();
            locationTrackerAPIList.add(locationTrackerAPI);
        }
    }

    //LocationTrackerAPI.Callback methods
    @Override
    public void didDataChanged(DataSnapshot dataSnapshot, ParseUser parseUser) {
        System.out.println("There are " + dataSnapshot.getChildrenCount() + " blog posts");

        double latitude = 0;
        if (dataSnapshot.hasChild("latitude") ) {
            latitude = (double)dataSnapshot.child("latitude").getValue();
            System.out.println(latitude);
//            Toast.makeText(this, "User " + parseUser.get("fullname") + location.getLongitude() + ":" + location.getLongitude(), Toast.LENGTH_SHORT).show();
        }
        double longitude = 0;
        if (dataSnapshot.hasChild("longitude") ) {
            longitude = (double)dataSnapshot.child("longitude").getValue();
            System.out.println(longitude);
        }

        if (latitude !=0 && longitude != 0) {
            Toast.makeText(this, "User " + parseUser.get("fullname") + ":" + latitude + ":" + longitude, Toast.LENGTH_SHORT).show();
        }

//        for (DataSnapshot locationSnapshot: dataSnapshot.getChildren()) {
//            System.out.println(locationSnapshot);
//
//            if (locationSnapshot.hasChild("latitude") ) {
//                double latitude = (double)locationSnapshot.child("latitude").getValue();
//            }
//            if (locationSnapshot.hasChild("longitude") ) {
//                double longitude = (double)locationSnapshot.child("longitude").getValue();
//            }
////            Location location = locationSnapshot.getValue(Location.class);
//
////            Toast.makeText(this, "User " + parseUser.get("fullname") + location.getLongitude() + ":" + location.getLongitude(), Toast.LENGTH_SHORT).show();
////            System.out.println(post.getAuthor() + " - " + post.getTitle());
//        }
    }

    @Override
    public void didCancelledLocationTrackingOfUser(ParseUser parseUser) {

    }
}
