package com.example.adventourmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    //private String API_KEY = "AIzaSyCq7zkukn5JAXb0wpL_PCRTrf50B6xxgeo";
// uncomment this
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,10, locationListener);
                }
            }
        }
    }

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                mMap.clear();
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                // we can either get user input of the radius like the area they want to search for in meters or make it as default
                // for types there are couple types we can let users to choose, the following link is the types that we can include
                // we can discuss what types we want to include or make it defult as restaurants.
                //https://developers.google.com/maps/documentation/places/web-service/supported_types?hl=en_US
                long radius = 10000;
                String type = "restaurant";

                Object transfer[] = new Object[2];
                transfer[0] = mMap;
                transfer[1] = getCurrentUrl(location.getLatitude(), location.getLongitude(), radius, type);

                getRestaurants getRestaurants = null;
                //getRestaurants = new getRestaurants();
                getRestaurants.execute(transfer);
            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKonwnLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mMap.clear();
            LatLng userLocation = new LatLng(lastKonwnLocation.getLatitude(), lastKonwnLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        }
    }

    //this is the function to get the url to get a picture to a corresponding restaurant I think you can use
    //https://github.com/codepath/android_guides/wiki/Using-CodePath-Async-Http-Client to call and get the image
    // and put it in the ui
    // you can move this function to other java file, I just created it along with getCurrentUrl
    // https://developers.google.com/maps/documentation/places/web-service/photos?hl=en_US
    public String getPhotoUrl(String maxwidth, String photoreference){
        String result = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + maxwidth
                + "&photoreference=" + photoreference + "&key=" + API_KEY;
        return result;
    }

    public String getCurrentUrl(double latitude, double longitude, long radius, String type){
        String result = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + latitude + "," + longitude +
                "&radius=" + radius +
                "&type=" + type +
                "&key=" + API_KEY;
        return result;
    }



}
