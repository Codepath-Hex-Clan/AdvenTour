package com.example.adventourmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RestaurantList extends RecyclerView.Adapter<RestaurantList.ViewHolder> {

    Context context;
    List<getRestaurants> restaurants;


    public RestaurantList(Context context, List<getRestaurants> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    // method involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View restaurantView = LayoutInflater.from(context).inflate(R.layout.fragment_home, parent, false);
        return new ViewHolder(restaurantView);

    }

    // involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        getRestaurants restaurant = restaurants.get(position);
        holder.bind(restaurant);

    }

    // returns total count of items in list
    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText Search;
        Button Enter;
        ImageView Image;
        TextView Description;
        TextView Comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Search = itemView.findViewById(R.id.Search);
            Enter = itemView.findViewById(R.id.Enter);
            Image = itemView.findViewById(R.id.Image);
            Description = itemView.findViewById(R.id.Description);
            Comments = itemView.findViewById(R.id.Comments);

        }

        public void bind(getRestaurants restaurant) {
            // Search?
            // Enter?
            // Image?
            Description.setText(restaurant.getRestaurant());
            Glide.with(context).load(restaurant.getPhotoID()).into(Image);

        }
    }
}
