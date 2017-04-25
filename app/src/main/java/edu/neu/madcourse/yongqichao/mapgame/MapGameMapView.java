package edu.neu.madcourse.yongqichao.mapgame;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.neu.madcourse.yongqichao.R;
import edu.neu.madcourse.yongqichao.leaderboard.WordgameRecord;

////////////////////////////////////////////////////
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

//public class MapGameMapView extends FragmentActivity implements

public class MapGameMapView extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        SensorEventListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    ////////////////////////////////////////////////////
    ArrayList<LatLng> markerPoints;
    LatLng myLatlng;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean isSensorPresent = false;
    TextView userNameMapView, levelMapView, scoreMapView;
    private DatabaseReference mDatabase;
    //private ArrayList<String> userlist = new ArrayList<>();

    public String username;
    public Integer level;
    public Integer score;
    public List<LatLng> myPoints = new ArrayList<>();
    public List<LatLng> otherPoints = new ArrayList<>();
    public List<String> userList = new ArrayList<>();

    // Please add the server key from your firebase console in the follwoing format "key=<serverKey>"
    private static final String SERVER_KEY = "key=AAAAuseFPB0:APA91bFsOFJtvWkdBwOtXHLZ8OjkgzAI3D6xUCKLdCZaFCi4nwROmz5wWwuZXP9IWJ_jXHQUmX-XqkDyNQ3qca3M8JLYLsoox54_xfTIHpsKalsL4NywiinkTILmPhGqTZh3HG-5fqQf";

    // This is the client registration token
    private static final String CLIENT_REGISTRATION_TOKEN = FirebaseInstanceId.getInstance().getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapgame_main);

        getSupportActionBar().hide(); //setTitle("Map );

        markerPoints = new ArrayList<LatLng>();


        userNameMapView = (TextView) findViewById(R.id.usernameMapView);
        Bundle b = this.getIntent().getExtras();
        userNameMapView.setText("User:" +b.getString("username"));
        username = b.getString("username");
        levelMapView = (TextView) findViewById(R.id.levelMapView);
        scoreMapView = (TextView) findViewById(R.id.scoreMapView);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        isSensorPresent = true;


        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);


        loadUserList();
    }

    /////////////////////////////////FOLLOWING DATABASE SAVING & LOADING/////////////////////////////

    public void loadUserList() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for(int i = 1; i<11;i++) {
                //String number = "Number" + i;
                if(!dataSnapshot.hasChild(username)){
                    mDatabase.child("mapGameRecords").child(username);
                    saveNewUserList();
                    level = 0;
                    score = 0;
                    levelMapView.setText("level:" + 0);
                    scoreMapView.setText("score:" + 0);
                }

                else {
                    GameUser newuser = dataSnapshot.child(username).getValue(GameUser.class);
                    level = newuser.level;
                    score = newuser.score;
                    myPoints = listToLatlng(newuser.myPoints);
                    otherPoints = listToLatlng(newuser.otherPoints);
                    System.out.println(myPoints + "pointspointspointspointspointspointspointspointspoints");

                    levelMapView.setText("level:" + level);
                    scoreMapView.setText("score:" + score);
                }

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    GameUser user = child.getValue(GameUser.class);
                    System.out.println(user.username + "eqwewq12321");
                    if(!userList.contains(user.username) && !user.username.equals(username)) userList.add(user.username);
                }
//
//                String inputRecord = records.username + "," + records.date +
//                        "," + records.wordWithHighestScore;
//                leaderlist.add(records.finalScore);
//                leaderlist.add(inputRecord);
//                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        mDatabase.child("mapGameRecords").addListenerForSingleValueEvent(postListener);
    }

    public void saveUserList() {
        //int N = 1;
        //for(int i = 0; i<userlist.size();i++) {
        //String number = "Number" + N++;
        GameUser newuser = new GameUser();
        //add score to database
        newuser.username = username;
        newuser.level = level;
        newuser.score = score;
        newuser.myPoints = listToString(myPoints);
        newuser.otherPoints = listToString(otherPoints);

//            String userinfo = leaderlist.get(i);
//            //add other infomation to databse
//            String[] fields = userinfo.split(",");
//            record.username = fields[0];
//            record.date = fields[1];
//            record.wordWithHighestScore = fields[2];

        mDatabase.child("mapGameRecords").child(username).setValue(newuser);
        //}

    }

    public void saveNewUserList() {
        //int N = 1;
        //for(int i = 0; i<userlist.size();i++) {
        //String number = "Number" + N++;
        GameUser newuser = new GameUser();
        //add score to database
        newuser.username = username;
        LatLng n = new LatLng(0, 0);
        myPoints.add(n);
        newuser.myPoints = listToString(myPoints);
        otherPoints.add(n);
        newuser.otherPoints = listToString(otherPoints);

//            String userinfo = leaderlist.get(i);
//            //add other infomation to databse
//            String[] fields = userinfo.split(",");
//            record.username = fields[0];
//            record.date = fields[1];
//            record.wordWithHighestScore = fields[2];

        mDatabase.child("mapGameRecords").child(username).setValue(newuser);
        //}

    }

    /////////////////////////////////ABOVE DATABASE SAVING & LOADING/////////////////////////////

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        if (isSensorPresent) {
            mSensorManager.unregisterListener(this);
        }

        saveUserList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String mStepsSinceReboot = (String.valueOf(event.values[0]));
//        Toast toast = Toast.makeText(getApplicationContext(), mStepsSinceReboot
//                , Toast.LENGTH_SHORT);
//        toast.show();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //////////FOLLOWING IS  GAME LOGIC CODE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public List<LatLng> listToLatlng(List<String> listpoints){
        List<LatLng> newL = new ArrayList<>();
        for(String s : listpoints){
            String[] fields = s.split(",");
            LatLng l = new LatLng(Double.parseDouble(fields[0]),Double.parseDouble(fields[1]));
            newL.add(l);
        }
        return newL;
    }

    public List<String> listToString(List<LatLng> listpoints){
        List<String> newL = new ArrayList<>();
        for(LatLng l : listpoints){
            StringBuilder sb = new StringBuilder();
            sb.append(Double.toString(l.latitude));
            sb.append(",");
            sb.append(Double.toString(l.longitude));
            newL.add(sb.toString());
        }
        return newL;
    }
    public void syncPoint(final List<String> userList, final LatLng point){
        //System.out.println(userList.get(1) + "12321");
        for(String userN: userList) {
            //if (userN != username) {
                final String nnnn = userN;
                mDatabase.child("mapGameRecords").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        GameUser user = dataSnapshot.child(nnnn).getValue(GameUser.class);
                        //GameUser user = dataSnapshot.getValue(GameUser.class);
                        List<String> newl = user.otherPoints;
                        StringBuilder sb = new StringBuilder();
                        sb.append(Double.toString(point.latitude));
                        sb.append(",");
                        System.out.println(userList.size() + "12321");
                        sb.append(Double.toString(point.longitude));
                        newl.add(sb.toString());
                        user.otherPoints = newl;
                        mDatabase.child("mapGameRecords").child(nnnn).setValue(user);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                    }
                });
            //}
        }
    }

    public void addMarker(GoogleMap googleMap){
        for(LatLng l : myPoints){
            MarkerOptions marker = new MarkerOptions().position(l).alpha(1f);
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mGoogleMap.addMarker(marker);
            MarkerOptions m1 = new MarkerOptions().position(l).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
            mGoogleMap.addMarker(m1);

        }

        for(LatLng l : otherPoints){
            MarkerOptions marker = new MarkerOptions().position(l).alpha(1f);
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mGoogleMap.addMarker(marker);
            MarkerOptions m1 = new MarkerOptions().position(l).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
            mGoogleMap.addMarker(m1);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        // mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style_map));

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        addMarker(mGoogleMap);
        //enable adding marker
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
//                MarkerOptions marker = new MarkerOptions().position(point).alpha(1f);
//                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                mGoogleMap.addMarker(marker);

                System.out.println("d(, e.toString());");


                myPoints.add(point);
                //saveUserList();
                syncPoint(userList, point);
//                ////////////////////////////////////////////////////
//                // Already two locations
//                if(markerPoints.size()>0){
//                    markerPoints.clear();
//                    mGoogleMap.clear();
//                }
                addMarker(mGoogleMap);

                // Adding new item to the ArrayList
                //markerPoints.add(point);
//
//                LatLng l1 = new LatLng(47.701493447562065, -122.33859378844498);
//                LatLng l2 = new LatLng(47.68393039438097, -122.36331302672625);
//                LatLng l3 = new LatLng(47.61624213255599, -122.2859510779381);
//                LatLng l4 = new LatLng(47.73644802756488, -122.3092970252037);
//                LatLng l5 = new LatLng(47.64184768522469, -122.36697524785995);
//                LatLng l6 = new LatLng(47.575803067139205, -122.32142765074968);
//                LatLng l7 = new LatLng(47.68177319562829, -122.26580936461687);
//                LatLng l8 = new LatLng(47.626115459090016, -122.20927543938161);
//                LatLng l9 = new LatLng(47.557270956708955, -122.36285537481308);
//                markerPoints.add(l1);
//                markerPoints.add(l2);
//                markerPoints.add(l3);
//                markerPoints.add(l4);
//                markerPoints.add(l5);
//                markerPoints.add(l6);
//                markerPoints.add(l7);
//                markerPoints.add(l8);
//                markerPoints.add(l9);

                // Creating MarkerOptions
//                MarkerOptions options = new MarkerOptions();
//                MarkerOptions options1 = new MarkerOptions().position(l1).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
//                MarkerOptions marker1 = new MarkerOptions().position(l1).alpha(1f);
//                marker1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                MarkerOptions options2 = new MarkerOptions().position(l2).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
//                MarkerOptions marker2 = new MarkerOptions().position(l2).alpha(1f);
//                marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                MarkerOptions options3 = new MarkerOptions().position(l3).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
//                MarkerOptions marker3 = new MarkerOptions().position(l3).alpha(1f);
//                marker3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                MarkerOptions options4 = new MarkerOptions().position(l4).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
//                MarkerOptions marker4 = new MarkerOptions().position(l4).alpha(1f);
//                marker4.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                MarkerOptions options5 = new MarkerOptions().position(l5).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
//                MarkerOptions marker5 = new MarkerOptions().position(l5).alpha(1f);
//                marker5.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                MarkerOptions options6 = new MarkerOptions().position(l6).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
//                MarkerOptions marker6 = new MarkerOptions().position(l6).alpha(1f);
//                marker6.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                MarkerOptions options7 = new MarkerOptions().position(l7).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
//                MarkerOptions marker7 = new MarkerOptions().position(l7).alpha(1f);
//                marker7.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                MarkerOptions options8 = new MarkerOptions().position(l8).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
//                MarkerOptions marker8 = new MarkerOptions().position(l8).alpha(1f);
//                marker8.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                MarkerOptions options9 = new MarkerOptions().position(l9).icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
//                options9.title("Remaining : 3 mins");
//                MarkerOptions marker9 = new MarkerOptions().position(l9).alpha(1f);
//                marker9.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//
//                // Setting the position of the marker
//                options.position(point);
//
//                System.out.println(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
//                //if(markerPoints.size()==1){
//                options.icon(BitmapDescriptorFactory.fromResource(R.raw.coin));
//                options.title("Remaining : 10 mins");
//                }else if(markerPoints.size()==2){
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                }


                for (LatLng p : otherPoints){
                    if(p.latitude > point.latitude - 0.01 && p.latitude < point.latitude + 0.01
                            && p.longitude > point.longitude - 0.01 && p.longitude < point.longitude + 0.01
                            && myLatlng !=null){
                        LatLng origin = myLatlng;
                        LatLng dest = p; // markerPoints.get(1);

                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);

                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                        break;
                    }
                }


//                // Checks, whether start and end locations are captured
//                if (markerPoints.size() >= 1 && myLatlng != null) {
//                    LatLng origin = markerPoints.get(5);
//                    LatLng dest = myLatlng; // markerPoints.get(1);
//
//                    // Getting URL to the Google Directions API
//                    String url = getDirectionsUrl(origin, dest);
//
//                    DownloadTask downloadTask = new DownloadTask();
//
//                    // Start downloading json data from Google Directions API
//                    downloadTask.execute(url);
//                }
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        //move camera
        if (mCurrLocationMarker == null) {
            myLatlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 11));
        }

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        myLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myLatlng);
        markerOptions.title("Player1 Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.raw.pegman));
        markerOptions.alpha(0.8f);
        markerOptions.snippet("you are player");
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        //move map camera
        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

        for (LatLng p : otherPoints) {
            myLatlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if (p.latitude > myLatlng.latitude - 0.001 && p.latitude < myLatlng.latitude + 0.001
                    && p.longitude > myLatlng.longitude - 0.001 && p.longitude < myLatlng.longitude + 0.001) {
                score += 100;
                level += 1;
                levelMapView.setText("level:" + level);
                scoreMapView.setText("score:" + score);
                Toast toast = Toast.makeText(getApplicationContext(), "Congratulation Level Up"
                        , Toast.LENGTH_SHORT);
                toast.show();
                Toast toast1 = Toast.makeText(getApplicationContext(), "Earned 100 score!!"
                        , Toast.LENGTH_SHORT);
                toast1.show();
                otherPoints.remove(p);
                break;
            }
        }
        //mGoogleMap.clear();
        //addMarker(mGoogleMap);
    }

    //////////ABOVE IS  GAME LOGIC CODE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    //////////FOLLOWING IS  GOOGLE MAP CURRENT LOCATION PERMISSION CODE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapGameMapView.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //////////ABOVE IS  GOOGLE MAP CURRENT LOCATION PERMISSION CODE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


    ///////////////////////////FOLLOWING IS  GOOGLE MAP DIRECTION CODE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


    //public class MainActivity extends FragmentActivity {

//        GoogleMap map;
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//
//            // Initializing
//            markerPoints = new ArrayList<LatLng>();
//
//            // Getting reference to SupportMapFragment of the activity_main
//            SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
//
//            // Getting Map for the SupportMapFragment
//            map = fm.getMap();
//
//            if(map!=null){
//
//                // Enable MyLocation Button in the Map
//                map.setMyLocationEnabled(true);

    //               // Setting onclick event listener for the map
//                map.setOnMapClickListener(new OnMapClickListener() {
//
//                    @Override
//                    public void onMapClick(LatLng point) {
//
//                        ////////////////////////////////////////////////////
//                        // Already two locations
//                        if(markerPoints.size()>1){
//                            markerPoints.clear();
//                            map.clear();
//                        }
//
//                        // Adding new item to the ArrayList
//                        markerPoints.add(point);
//
//                        // Creating MarkerOptions
//                        MarkerOptions options = new MarkerOptions();
//
//                        // Setting the position of the marker
//                        options.position(point);
//
//                        /**
//                         * For the start location, the color of marker is GREEN and
//                         * for the end location, the color of marker is RED.
//                         */
//                        if(markerPoints.size()==1){
//                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                        }else if(markerPoints.size()==2){
//                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                        }
//
//                        // Add new marker to the Google Map Android API V2
//                        map.addMarker(options);
//
//                        // Checks, whether start and end locations are captured
//                        if(markerPoints.size() >= 2){
//                            LatLng origin = markerPoints.get(0);
//                            LatLng dest = markerPoints.get(1);
//
//                            // Getting URL to the Google Directions API
//                            String url = getDirectionsUrl(origin, dest);
//
//                            DownloadTask downloadTask = new DownloadTask();
//
//                            // Start downloading json data from Google Directions API
//                            downloadTask.execute(url);
//                        }
//                    }
//                });
//            }
//        }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "mode=walking";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exceptiondownloadingurl", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLACK);
            }

            // Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline(lineOptions);
        }
    }

//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//            // Inflate the menu; this adds items to the action bar if it is present.
//            getMenuInflater().inflate(R.menu.main, menu);
//            return true;
//        }

}




