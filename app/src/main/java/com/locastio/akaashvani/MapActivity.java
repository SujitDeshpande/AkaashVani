package com.locastio.akaashvani;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.locastio.akaashvani.data.Group;
import com.locastio.akaashvani.data.UserGroup;
import com.locastio.akaashvani.services.LocationAPI;
import com.locastio.akaashvani.services.LocationTrackerAPI;
import com.locastio.akaashvani.services.UserGroupAPI;
import com.locastio.akaashvani.util.GPSTracker;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends FragmentActivity implements LocationAPI.Callback, GPSTracker.Callback, UserGroupAPI.Callback, LocationTrackerAPI.Callback {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 5 * 1; // 1 minute
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private GoogleMap mMap;
    private android.location.LocationListener locationListener;

    HashMap<String, Markers> hashMapMarkerOptions = new HashMap<>();

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
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();

//        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
        String strGroupObjId = getIntent().getStringExtra("groupObjId");
//
        UserGroupAPI userGroupAPI = new UserGroupAPI(this);
        userGroupAPI.getUserGroupsOfGroupId(strGroupObjId);
        Log.d("k10:", strGroupObjId);
//
//
        gpsTracker = new GPSTracker(this, this);
//        gpsTracker.getLocation();
//
//        markers = getData();
//        setContentView(R.layout.activity_maps);
//        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
//
        gpsTracker.getLocation();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #} once when {@link #} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
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
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(12.78, 77.87)).title("Marker"));
//        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(12.78, 77.87)).title("Marker");
//        markerOptions.position(new LatLng(1,1));
    }

    //    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    public static List<Markers> getData() {
//        List<Markers> data = new ArrayList<>();
//        double[] lats = {12.9715011, 12.968064};
//        double[] longs = {77.5959849, 77.5950677};
//        for (int i = 0; (i < lats.length); i++) {
//            Markers current = new Markers();
//            current.latitude = lats[i];
//            current.longitude = longs[i];
//            data.add(current);
//        }
//        return data;
//    }

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
        gpsTracker.getLocation();
        Log.d(TAG, "Location update started ..............: ");
    }

    private void addMarkers() {
        mMap.clear();
        for(Map.Entry entry: hashMapMarkerOptions.entrySet()){
            Markers markers = (Markers)entry.getValue();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(markers.latitude, markers.longitude));

            mMap.addMarker(markerOptions);
        }
    }

//    private void addMarker() {
//        MarkerOptions options = new MarkerOptions();
//
//        // following four lines requires 'Google Maps Android API Utility Library'
//        // https://developers.google.com/maps/documentation/android/utility/
//        // I have used this to display the time as title for location markers
//        // you can safely comment the following four lines but for this info
//        /*IconGenerator iconFactory = new IconGenerator(this);
//        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
//        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
//        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());*/
//
//        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//        options.position(currentLatLng);
//
//        //mMarker = googleMap.addMarker(options);
//        Log.d(TAG, "Marker added.............................");
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
//                14));
//        Log.d(TAG, "Zoom done.............................");
////        addMarkers();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
/*        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);*/
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (mGoogleApiClient.isConnected()) {

        setUpMapIfNeeded();
        startLocationUpdates();
        Log.d(TAG, "Location update resumed .....................");
        //}
    }

    @Override
    public void didUpdateLocation(Location location) {
        LocationAPI locationAPI = new LocationAPI(null);
        ParseUser currentUser = ParseUser.getCurrentUser();
        locationAPI.updateLocationOfUser(currentUser, location);

        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

    }

    @Override
    public void didDataChanged(DataSnapshot dataSnapshot) {
        System.out.println("There are " + dataSnapshot.getChildrenCount() + " blog posts");
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
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
        for (ParseObject parseObject : userGroupList) {
            UserGroup userGroup = (UserGroup) parseObject;
            LocationTrackerAPI locationTrackerAPI = new LocationTrackerAPI(this);
            locationTrackerAPI.trackUserLocation(userGroup.getUser());
            locationTrackerAPIList.add(locationTrackerAPI);
        }
    }

    void stopTrackingGroup() {
        for (LocationTrackerAPI locationTrackerAPI : locationTrackerAPIList) {
            locationTrackerAPI.removeTrackingForUser();
            locationTrackerAPIList.add(locationTrackerAPI);
        }
    }

    //LocationTrackerAPI.Callback methods
    @Override
    public void didDataChanged(DataSnapshot dataSnapshot, ParseUser parseUser) {
        System.out.println("There are " + dataSnapshot.getChildrenCount() + " blog posts");

        double latitude = 0;
        if (dataSnapshot.hasChild("latitude")) {
            latitude = (double) dataSnapshot.child("latitude").getValue();
            System.out.println(latitude);
//            Toast.makeText(this, "User " + parseUser.get("fullname") + location.getLongitude() + ":" + location.getLongitude(), Toast.LENGTH_SHORT).show();
        }
        double longitude = 0;
        if (dataSnapshot.hasChild("longitude")) {
            longitude = (double) dataSnapshot.child("longitude").getValue();
            System.out.println(longitude);
        }

        if (latitude != 0 && longitude != 0) {

            String strParseUserObjId = parseUser.getObjectId();
            Markers markers = null;
            for(Map.Entry entry: hashMapMarkerOptions.entrySet()){
                if (strParseUserObjId.equalsIgnoreCase((String)entry.getKey())) {
                    markers = (Markers)entry.getValue();
                    break;
                }
            }
            if (markers == null) {
//                String fullname = (String) parseUser.get("fullname");
                markers = new Markers();
//            } else {
//                markerOptions.position(new LatLng(latitude, longitude));
            }
            markers.latitude = latitude;
            markers.longitude = longitude;
            hashMapMarkerOptions.put(strParseUserObjId, markers);

            this.addMarkers();

            if (!isAnimated) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),14));
                    isAnimated = true;
            }

//            mMap.addMarker(new MarkerOptions().position(new LatLng(12.78, 77.87)).title("Marker"));
//            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(12.78, 77.87)).title("Marker");
//            markerOptions.position(new LatLng(1,1));

            Toast.makeText(this, "User " + parseUser.get("fullname") + ":" + latitude + ":" + longitude, Toast.LENGTH_SHORT).show();
        }

    }

    boolean isAnimated = false;

    @Override
    public void didCancelledLocationTrackingOfUser(ParseUser parseUser) {

    }
}
