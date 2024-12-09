package com.example.travelbooking;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelbooking.adapter.PlaceAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPlacesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecents, recyclerViewTopPlaces;
    private PlaceAdapter adapterRecents, adapterTopPlaces;
    private List<Place> recentsList, topPlacesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_places);

        // Initialize RecyclerView for Recents and Top Places
        recyclerViewRecents = findViewById(R.id.recyclerViewRecents);
        recyclerViewRecents.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewTopPlaces = findViewById(R.id.recyclerViewTopPlaces);
        recyclerViewTopPlaces.setLayoutManager(new LinearLayoutManager(this));

        // Initialize lists and adapters
        recentsList = new ArrayList<>();
        topPlacesList = new ArrayList<>();

        adapterRecents = new PlaceAdapter(this, recentsList);
        recyclerViewRecents.setAdapter(adapterRecents);

        adapterTopPlaces = new PlaceAdapter(this, topPlacesList);
        recyclerViewTopPlaces.setAdapter(adapterTopPlaces);

        // Fetch data from Firebase
        fetchPlacesFromFirebase("recents", recentsList, adapterRecents);
        fetchPlacesFromFirebase("top_places", topPlacesList, adapterTopPlaces);
    }

    private void fetchPlacesFromFirebase(String category, List<Place> placeList, PlaceAdapter adapter) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(category);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                placeList.clear();
                for (DataSnapshot placeSnapshot : snapshot.getChildren()) {
                    Place place = placeSnapshot.getValue(Place.class);
                    if (place != null) {
                        placeList.add(place);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewPlacesActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

