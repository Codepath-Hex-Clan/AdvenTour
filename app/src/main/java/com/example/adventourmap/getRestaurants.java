package com.example.adventourmap;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class getRestaurants extends AsyncTask<Object, String, String > {

    private String restaurantsData, url;
    private GoogleMap mMap;

    String restaurant;
    String location;
    String photoID;
    String photoRef;

    public getRestaurants(JSONObject jsonObject) throws JSONException {
        restaurant = jsonObject.getString("name");
        location = jsonObject.getString("vicinity");
        photoID = jsonObject.getString("photos");
        photoRef = jsonObject.getString("reference");

    }




    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        DownloadUrl downloadUrl = new DownloadUrl();
        restaurantsData = downloadUrl.readUrl(url);
        return restaurantsData;
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<String[]> restaurants;
        DataParser dataParser = new DataParser();
        restaurants = dataParser.parse(s);

        // restaurants is the variable with all the information that parse out from json
        // restaurants is an ArrayList and each element in the ArrayList is a String array which represent a restaurant
        // Each string array has four element
        // the first index 0 is the name of the restaurant
        // name
        // the second index 1 is the address of the restaurant
        // vicinity
        // the third index 2 is the width of the picture
        // photo_id
        // the fourth index 3 is the photo_reference
        // reference
    }
    // you can create a function to bind the data to the ui here or transfer the information of the restaurants to
    //other file by creating a get method
    public static List<getRestaurants> fromJsonArray(JSONArray restaurantJsonArray) throws JSONException {
        List<getRestaurants> restaurants = new ArrayList<>();
        for (int i = 0; i < restaurantJsonArray.length(); i++){
            restaurants.add(new getRestaurants(restaurantJsonArray.getJSONObject(i)));
        }
        return restaurants;

    }

    public String getRestaurant() {
        return restaurant;
    }// does something weird but meh

    public String getLocation() {
        return location;
    }

    public String getPhotoID() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", photoID);
    }

    public String getPhotoRef() {
        return photoRef;
    }


}
