package com.example.adventourmap;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Fragments.FavoritesFragment;
import Fragments.HomeFragment;
import Fragments.SettingsFragment;
import okhttp3.Headers;

public class MainActivity extends AppCompatActivity{
// uncomment this
    //public static final String FIND_PLACE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=AIzaSyCq7zkukn5JAXb0wpL_PCRTrf50B6xxgeo";
    public static final String TAG = "MainActivity";
    List<getRestaurants> restaurants;

// add this to android manifest , where it says meta data
    //android:value="AIzaSyCq7zkukn5JAXb0wpL_PCRTrf50B6xxgeo" />

    // Explanations: Each fragment is part of the xml fragments
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvRestaurants = findViewById(R.id.rvRestaurants);
        restaurants = new ArrayList<>();

        // restaurant list
        final RestaurantList restaurantList = new RestaurantList(this, restaurants);

        // set Rest. List to recycler view
        rvRestaurants.setAdapter(restaurantList);

        // layout manager in recycler view
        rvRestaurants.setLayoutManager(new LinearLayoutManager(this));




        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();

        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(FIND_PLACE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSucess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    restaurants.addAll(getRestaurants.fromJsonArray(results));
                    restaurantList.notifyDataSetChanged();
                    Log.i(TAG, "Restaurant: " + restaurants.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }


            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_favorites:
                            selectedFragment = new FavoritesFragment();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };


}
