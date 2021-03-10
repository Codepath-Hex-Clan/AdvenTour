package com.example.adventourmap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataParser {
    private String[] getRestaurantsInfo(JSONObject googleRestaurantsJSON){
        String[] result = new String[4];
        String name = "";
        String address = "";
        String picture = "";
        String width = "";
        String reference = "";
        try {
            if (!googleRestaurantsJSON.isNull("name")){
                name = googleRestaurantsJSON.getString("name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (!googleRestaurantsJSON.isNull("vicinity")){
                address = googleRestaurantsJSON.getString("vicinity");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (!googleRestaurantsJSON.isNull("photos")){
                picture = googleRestaurantsJSON.getString("photos");
                width = picture.substring(picture.indexOf("width") + 7, picture.length()-2);
                reference = picture.substring(picture.indexOf("photo_reference") + 18, picture.indexOf("width")-3);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result[0] = name;
        result[1] = address;
        result[2] = width;
        result[3] = reference;
        return result;
    }

    private ArrayList<String[]> getAllRestaurants(JSONArray jsonArray){
        ArrayList<String[]> restaurants = new ArrayList<>();
        for( int i = 0; i < jsonArray.length(); i++){
            try {
                restaurants.add((getRestaurantsInfo((JSONObject) jsonArray.get(i))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return restaurants;
    }

    public ArrayList<String[]> parse(String jsonData){
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getAllRestaurants(jsonArray);
    }
}
