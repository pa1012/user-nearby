package com.example.usernearby;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.LocationManager;
import android.net.RouteInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.fragment.app.FragmentActivity;

import com.example.usernearby.MainActivity;
import com.example.usernearby.R;
import com.example.usernearby.User;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.util.Log;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
  private final static int MY_PERMISSIONS_REQUEST = 32;
  private static final int REQ_PERMISSION = 1 ;
  private GoogleMap mMap;
  //private ArrayList<Landmark> mLandmark;
  //private Landmark ori;
  private Marker mMarker;
  ArrayList <Marker> markers;
  //private TextToSpeech mText2Speech;
  private boolean isReady = false;
  protected LocationManager locationManager;
  protected LocationListener locationListener;
  protected Context context;
  Intent intentIn;
  private LatLng mOrigin;
  private LatLng mDestination;
  private ArrayList<User> _user;
  private User userLogin;
  private EditText _radius;
  Circle myCircle;
  private int position;

  private DatabaseReference mRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    intentIn = getIntent();

    loadData();
    initComponent();

  }

  private void initComponent() {
    _radius = (EditText) findViewById(R.id.radiusText);
    isReady = true;
    markers = new ArrayList<>();
  }

  private void loadData() {
    _user = new ArrayList<>();

    /*mRef = FirebaseDatabase.getInstance().getReference("users");


    mRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        _user.clear();
        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
          User user = postSnapshot.getValue(User.class);
          _user.add(user);



        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        System.out.println("The read failed: " + databaseError.getMessage());
      }
    });*/
    User user1 = new User("user01",
            null, "user01@gmail.com", R.drawable.user01,
            10.768313, 106.706793, true);
    User user2 = new User("user02",
            null, "user02@gmail.com", R.drawable.user02,
           10.772535, 106.698034, true);
    User user3 = new User("user03",
            null, "user03@gmail.com", R.drawable.user03,
            10.779742, 106.699188, true);
    User user4 = new User("user04",
            null, "user04@gmail.com", R.drawable.user02,
            6, 104, true);
    User user5 = new User("user05",
            null, "user05@gmail.com", R.drawable.user03,
            5, 103, true);
    _user.add(user1);
    _user.add(user2);
    _user.add(user3);
    _user.add(user4);
    _user.add(user5);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    if(checkPermission())
      mMap.setMyLocationEnabled(true);
    else askPermission();
    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

      @Override
      public boolean onMarkerClick(Marker marker) {
        if(isReady) {
          position = getPosition(marker.getTitle());
          Intent intent = new Intent(MapsActivity.this, Profile.class);
          User user = _user.get(position);
          intent.putExtra("user_profile", user);

          startActivity(intent);
        }
        return false;
      }
    });


  }

  private int getPosition(String title) {
    for(int i = 0; i < _user.size(); i++){
      if(_user.get(i).email.equals(title)){
        return i;}
    }
    return -1;
  }

  private void askPermission() {

    ActivityCompat.requestPermissions(
            this,
            new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
            REQ_PERMISSION
    );
  }
  private void requestPermission(String permission) {
    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
              new String[]{permission},
              MY_PERMISSIONS_REQUEST);
    }
  }
  private boolean checkPermission() {

    // Ask for permission if it wasn't granted yet
    return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED );
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch ( requestCode ) {
      case REQ_PERMISSION: {
        if ( grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
          // Permission granted
          if(checkPermission())
            mMap.setMyLocationEnabled(true);

        } else {
          // Permission denied

        }
        break;
      }
    }
  }

  private void display(){
    mOrigin = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
    for(int i = 0; i < _user.size() ;i++){
      LatLng latLng = new LatLng(_user.get(i).getLat(),_user.get(i).getLng());
      mDestination = latLng;
      double dist = CalculateDistance(mOrigin,mDestination);
      if(dist < Double.parseDouble(_radius.getText().toString())){
        displayMarkers(_user.get(i), i);
      }
    }
  }
  private void displayMarkers(User user, int i) {

    Bitmap bmp = BitmapFactory.decodeResource(getResources(), user.getImage());
    bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/20, bmp.getHeight()/20, false);
    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bmp);
    LatLng latLng = new LatLng(_user.get(i).getLat(),_user.get(i).getLng());
    mMarker = mMap.addMarker(new MarkerOptions()
            .position(latLng)
            .title(user.getEmail())
            .icon(bitmapDescriptor)
    );
    markers.add(mMarker);


  }

  private void updateUsers() {

    String user_email = intentIn.getStringExtra("user_email");
    userLogin = _user.get(getPosition(user_email));
    userLogin.setLat(mMap.getMyLocation().getLatitude());
    userLogin.setLng(mMap.getMyLocation().getLongitude());

    mRef.child("users").child(userLogin.getUsername()).setValue(userLogin)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Log.d("user_write","success");
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.d("user_write","failed");
              }
            });

  }

  private static double DegreesToRadians(double degrees)
  {
    return degrees * Math.PI / 180.0;
  }
  public static double CalculateDistance(LatLng location1, LatLng location2)
  {
    double circumference = 40000.0; // Earth's circumference at the equator in km
    double distance = 0.0;

    //Calculate radians
    double latitude1Rad = DegreesToRadians(location1.latitude);
    double longitude1Rad = DegreesToRadians(location1.longitude);
    double latititude2Rad = DegreesToRadians(location2.latitude);
    double longitude2Rad = DegreesToRadians(location2.longitude);

    double logitudeDiff = Math.abs(longitude1Rad - longitude2Rad);

    if (logitudeDiff > Math.PI)
    {
      logitudeDiff = 2.0 * Math.PI - logitudeDiff;
    }

    double angleCalculation =
            Math.acos(
                    Math.sin(latititude2Rad) * Math.sin(latitude1Rad) +
                            Math.cos(latititude2Rad) * Math.cos(latitude1Rad) * Math.cos(logitudeDiff));

    distance = circumference * angleCalculation / (2.0 * Math.PI);

    return distance;
  }

  public void onClickSetRadius(View view) {

    display();
    LatLngBounds.Builder builder = new LatLngBounds.Builder();

//the include method will calculate the min and max bound.
    for (int i = 0 ; i < markers.size(); i++){
      builder.include(markers.get(i).getPosition());
    }

    builder.include(mOrigin);
    LatLngBounds bounds = builder.build();

    int width = getResources().getDisplayMetrics().widthPixels;
    int height = getResources().getDisplayMetrics().heightPixels;
    int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

    mMap.animateCamera(cu);
    /*mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
    CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(mOrigin)    // Sets the center of the map to Mountain View
            .zoom(15)                           // Sets the zoom
            .build();
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

    if(myCircle == null) {
      CircleOptions circleOptions = new CircleOptions()
              .center(mOrigin)   //set center
              .radius(Double.parseDouble(_radius.getText().toString()) * 1000)  //set radius in meters
              .fillColor(Color.TRANSPARENT)  //default
              .strokeColor(Color.BLUE)
              .strokeWidth(10);

      myCircle = mMap.addCircle(circleOptions);
    }
    else {
      myCircle.remove();
      CircleOptions circleOptions = new CircleOptions()
              .center(mOrigin)   //set center
              .radius(Double.parseDouble(_radius.getText().toString()) * 1000)  //set radius in meters
              .fillColor(Color.TRANSPARENT)  //default
              .strokeColor(Color.BLUE)
              .strokeWidth(10);

      myCircle = mMap.addCircle(circleOptions);
    }
  }
}

