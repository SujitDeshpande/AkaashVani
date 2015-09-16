package com.locastio.akaashvani.screen.fragment;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.locastio.akaashvani.Markers;
import com.locastio.akaashvani.R;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Locationfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Locationfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Locationfragment extends Fragment implements LocationAPI.Callback, GPSTracker.Callback, UserGroupAPI.Callback, LocationTrackerAPI.Callback {


    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 5 * 1; // 1 minute
    private static String strGroupObjId;
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
    private final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private final long MIN_TIME_BW_UPDATES = 1000; //1000 * 5 * 1; // 1 minute

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Locationfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Locationfragment newInstance(String param1, String param2) {
        Locationfragment fragment = new Locationfragment();

        strGroupObjId = param2;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Locationfragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, container, false);
        UserGroupAPI userGroupAPI = new UserGroupAPI(this);
        userGroupAPI.getUserGroupsOfGroupId(strGroupObjId);
        Log.d("k10:", strGroupObjId);

        gpsTracker = new GPSTracker(getActivity(), this);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            getActivity().finish();
        }
//
        gpsTracker.getLocation();

        setUpMapIfNeeded();
        startLocationUpdates();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        stopLocationUpdates();
        stopTrackingGroup();
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
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    gpsTracker.getLocation();
                    addMarkers();
                    return true;
                }
            });
            addMarker();
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

        LatLng currentLatLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        options.position(currentLatLng);

        //mMarker = googleMap.addMarker(options);
        Log.d(TAG, "Marker added.............................");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                17));
        Log.d(TAG, "Zoom done.............................");
        addMarkers();
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
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }

    protected void startLocationUpdates() {
        gpsTracker.getLocation();
        Log.d(TAG, "Location update started ..............: ");
    }

    private void addMarkers() {
//        Toast.makeText(getActivity(), "Add Markers", Toast.LENGTH_SHORT).show();
        mMap.clear();
        for (Map.Entry entry : hashMapMarkerOptions.entrySet()) {
//            Toast.makeText(getActivity(), "Inside For Loop", Toast.LENGTH_SHORT).show();
            Markers markers = (Markers) entry.getValue();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(markers.latitude, markers.longitude));

            mMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onPause() {
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

        addMarkers();
    }

    void stopTrackingGroup() {
        for (LocationTrackerAPI locationTrackerAPI : locationTrackerAPIList) {
            locationTrackerAPI.removeTrackingForUser();
            //locationTrackerAPIList.add(locationTrackerAPI);
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
            for (Map.Entry entry : hashMapMarkerOptions.entrySet()) {
                if (strParseUserObjId.equalsIgnoreCase((String) entry.getKey())) {
                    markers = (Markers) entry.getValue();
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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
                isAnimated = true;
            }

//            Toast.makeText(this, "User " + parseUser.get("fullname") + ":" + latitude + ":" + longitude, Toast.LENGTH_SHORT).show();
        }

    }

    boolean isAnimated = false;

    @Override
    public void didCancelledLocationTrackingOfUser(ParseUser parseUser) {

    }

}
