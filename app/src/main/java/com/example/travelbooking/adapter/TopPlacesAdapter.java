package com.example.travelbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbooking.DetailsActivity;
import com.example.travelbooking.R;
import com.example.travelbooking.Place;

import java.util.List;

public class TopPlacesAdapter extends RecyclerView.Adapter<TopPlacesAdapter.ViewHolder> {
    private Context context;
    private List<Place> places;

    public TopPlacesAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_places_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.placeName.setText(place.getPlaceName());
        holder.countryName.setText(place.getCountryName());
        holder.priceRange.setText("Price: IDR " + place.getPrice());

        // Decode Base64 image string and set the image
        if (place.getImageBase64() != null && !place.getImageBase64().isEmpty()) {
            byte[] decodedString = Base64.decode(place.getImageBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.imageView.setImageResource(R.drawable.image_icon);  // Default image if no image provided
        }

        // Set item click listener to open DetailsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("placeName", place.getPlaceName());
            intent.putExtra("countryName", place.getCountryName());
            intent.putExtra("description", place.getDescription());  // Make sure to add description in your Place model
            intent.putExtra("price", place.getPrice());
            intent.putExtra("imageBase64", place.getImageBase64());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void updateData(List<Place> newPlaces) {
        this.places = newPlaces;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, countryName, priceRange;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Ensure your layout has these views with correct IDs
            placeName = itemView.findViewById(R.id.place_name);
            countryName = itemView.findViewById(R.id.country_name);
            priceRange = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.place_image);  // Ensure your layout has an ImageView
        }
    }
}
