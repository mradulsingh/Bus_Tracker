package com.example.ms.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karan.churi.PermissionManager.PermissionManager;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class UserLocationMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RES_REQUEST = 7172;

    // request = mLocationRequest////client = mGoogleApiClient///
    private Location mLastLocation;
    GoogleMap mMap;
    FirebaseAuth auth;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latLng;
    DatabaseReference databaseReference, location;
    FirebaseUser user;
    String current_user_name;
    String current_user_email;
    String current_user_image_url;
    TextView t1_currentName, t2_currentEmail;
    String name, email, password, date, isSharing, code, cName;
    Double lat, lng;
    ImageView i1;
    Uri imageUri;


    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /////////from MyCircle2/////////////
//        Intent intent = getIntent();
//        if(intent!=null){
//            email = intent.getStringExtra("email");
//        }

        /////////from invite activity///////
//        Intent i = getIntent();
//        if (i!=null){
//            name = i.getStringExtra("name");
//            cName = name;
////            email = i.getStringExtra("email");
//            password = i.getStringExtra("password");
//            isSharing = i.getStringExtra("isSharing");
//            code = i.getStringExtra("code");
//            imageUri = i.getParcelableExtra("imageUri");
//        }
        //////////////////////////

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //////////////////////////////edmt/////////////////////

//        if (!TextUtils.isEmpty(email)){
//            Toast.makeText(getApplicationContext(),email+"new", Toast.LENGTH_SHORT).show();
//            loacLocationForThisUsers(email);
//        }
        //////////////////////////////edmt/////////////////////

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        t1_currentName = header.findViewById(R.id.title_text);
        t2_currentEmail = header.findViewById(R.id.email_text);
        i1 = header.findViewById(R.id.imageView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        location = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_user_name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                current_user_email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);
                current_user_image_url = dataSnapshot.child(user.getUid()).child("imageUrl").getValue(String.class);

                t1_currentName.setText(current_user_name);
                t2_currentEmail.setText(current_user_email);
                Picasso.get().load(current_user_image_url).into(i1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

///////////////////////////////////////////////
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            }, MY_PERMISSION_REQUEST_CODE);
//        }
//        else {
//            if (checkPlayServices()){
////                Toast.makeText(getApplicationContext(),"uuu "+email, Toast.LENGTH_SHORT).show();
//                buildGoogleApiClient();
//                createLocationRequest();
//                displayLocation();
//            }
//        }
/////////////////////////////////////////
    }

//    private void loacLocationForThisUsers(String email) {
//        Query user_location = location.child(email);
//        Toast.makeText(getApplicationContext(),"puppi", Toast.LENGTH_SHORT).show();
//
//        user_location.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapShot:dataSnapshot.getChildren()){
//                    CreateUser tracking = postSnapShot.getValue(CreateUser.class);
//
//                    //add marker for friend location
//                    LatLng friendLocation = new LatLng(Double.parseDouble(tracking.getLat()),
//                            Double.parseDouble(tracking.getLng()));
//
//                    //Create location from user coordinates
//                    Location currentUser = new Location("");
//                    currentUser.setLatitude(lat);
//                    currentUser.setLongitude(lng);
//
//                    //create location from friends location
//                    Location friend = new Location("");
//                    friend.setLatitude(Double.parseDouble(tracking.getLat()));
//                    friend.setLongitude(Double.parseDouble(tracking.getLng()));
//
//                    //Create function for calculating distance between both
//                    distance(currentUser, friend);
//
//                    //Add friend marker on Map
//                    mMap.addMarker(new MarkerOptions()
//                        .position(friendLocation)
//                        .title(tracking.getEmail())
//                        .snippet("Distance "+new DecimalFormat("#.#").format(distance(currentUser, friend)))
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.0f));
//                }
//                //Create marker for current user
//                LatLng current = new LatLng(lat, lng);
//                mMap.addMarker(new MarkerOptions().position(current).title(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }



//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case MY_PERMISSION_REQUEST_CODE:{
//                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    if (checkPlayServices()){
//                        buildGoogleApiClient();
//                        createLocationRequest();
//                        displayLocation();
//                    }
//                }
//
//            }
//            break;
//        }
//
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    private double distance(Location currentUser, Location friend) {
//        double theta = currentUser.getLongitude() - friend.getLongitude();
//        double dist = Math.sin(deg2rad(currentUser.getLatitude()))
//                * Math.sin(deg2rad(friend.getLatitude()))
//                * Math.cos(deg2rad(currentUser.getLatitude()))
//                * Math.cos(deg2rad(friend.getLatitude()))
//                * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 *1.1515;
//        return (dist);
//    }

//    private double rad2deg(double rad) {
//        return (rad * 180/Math.PI);
//    }

//    private double deg2rad(double deg) {
//        return (deg * Math.PI/180.0);
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client.connect();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_location_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_joinCircle) {
            // Handle the camera action
            Intent i = new Intent(UserLocationMainActivity.this, JoinCircleActivity.class);
//            i.putExtra("lat", latLng.latitude);
//            i.putExtra("lng", latLng.longitude);
            startActivity(i);

        } else if (id == R.id.nav_myCircle) {
            Intent i = new Intent(UserLocationMainActivity.this, Mycircle2.class);
            startActivity(i);

        } else if (id == R.id.nav_joinedCircle) {

        } else if (id == R.id.nav_InviteMembers) {

        } else if (id == R.id.nav_shareLoc) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "My Location is " + "https://www.google.com/maps/@"+latLng.latitude+","+latLng.longitude);
            startActivity(i.createChooser(i, "Share using: "));

        } else if (id == R.id.nav_SignOut) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                auth.signOut();
                finish();
                Intent i = new Intent(UserLocationMainActivity.this, MainActivity.class);
                startActivity(i);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Turn on Your Location", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //////////////programming experts////////////////
        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(3000);
        //////////////////////////////////////////////////
//        createLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Toast.makeText(getApplicationContext(),"onConnected", Toast.LENGTH_SHORT).show();
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
        //////////////////////programming experts///////////

//        displayLocation();
//        loacLocationForThisUsers(email);
//        startLocationUpdates();



        //LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null){
            Toast.makeText(getApplicationContext(), "onLocationChangedNOT", Toast.LENGTH_SHORT).show();
        }else {

            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions opt = new MarkerOptions();
            opt.position(latLng);
            opt.title("current location");
            mMap.addMarker(opt);
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.0f));
        }
//        mLastLocation = location;
//        displayLocation();

    }

//    private void startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
//
//    }

//    private void displayLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
//        if (mLastLocation != null){
//            //update to firebase
//            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .setValue(new CreateUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
//                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
//                            String.valueOf(mLastLocation.getLatitude()),
//                            String.valueOf(mLastLocation.getLongitude()),
//                            String.valueOf(code),
//                            String.valueOf(password),
//                            String.valueOf(current_user_image_url),
//                            String.valueOf(current_user_name)));
//            Toast.makeText(getApplicationContext(),"displayLocation", Toast.LENGTH_SHORT).show();
//
//
//        }
//        else {
//            //Toast.makeText(getApplicationContext(), "couldn't location", Toast.LENGTH_SHORT).show();
//            Log.d("Test", "could not load location");
//        }
//    }

//    @SuppressLint("RestrictedApi")
//    private void createLocationRequest() {
//        request = new LocationRequest();
//        request.setInterval(UPDATE_INTERVAL);
//        request.setFastestInterval(FASTEST_INTERVAL);
//        request.setSmallestDisplacement(DISTANCE);
//        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        Toast.makeText(getApplicationContext(),"createLocationRequest", Toast.LENGTH_SHORT).show();
//
//    }

//    private void buildGoogleApiClient() {
//        client = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        Toast.makeText(getApplicationContext(),"GoogleApiClient", Toast.LENGTH_SHORT).show();
//        client.connect();
//    }

//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS){
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RES_REQUEST).show();
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (client != null)
//            client.connect();
//    }
//
//    @Override
//    protected void onStop() {
//        if (client != null)
//            client.disconnect();
//        super.onStop();
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//    }
}
