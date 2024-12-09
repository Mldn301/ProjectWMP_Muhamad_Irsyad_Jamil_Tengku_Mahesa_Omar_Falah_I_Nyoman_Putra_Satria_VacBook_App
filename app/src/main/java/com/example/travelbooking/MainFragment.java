package com.example.travelbooking;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.travelbooking.adapter.RecentsAdapter;
import com.example.travelbooking.adapter.TopPlacesAdapter;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView recentRecycler, topPlacesRecycler;
    private RecentsAdapter recentsAdapter;
    private TopPlacesAdapter topPlacesAdapter;
    private List<Place> recentsList, filteredRecents;
    private List<Place> topPlacesList, filteredTopPlaces;
    private EditText searchField;

    // Firebase Database reference
    private DatabaseReference recentsRef, topPlacesRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Initialize RecyclerViews and search field
        searchField = view.findViewById(R.id.editText);
        recentRecycler = view.findViewById(R.id.recent_recycler);
        topPlacesRecycler = view.findViewById(R.id.top_places_recycler);

        // Initialize data lists
        recentsList = new ArrayList<>();
        topPlacesList = new ArrayList<>();
        filteredRecents = new ArrayList<>();
        filteredTopPlaces = new ArrayList<>();

        // Setup RecyclerViews
        setUpRecentsRecycler();
        setUpTopPlacesRecycler();

        // Firebase setup
        recentsRef = FirebaseDatabase.getInstance().getReference("recents");
        topPlacesRef = FirebaseDatabase.getInstance().getReference("top_places");

        // Fetch data from Firebase
        fetchRecentsData();
        fetchTopPlacesData();

        // Add search functionality
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void setUpRecentsRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recentRecycler.setLayoutManager(layoutManager);
        recentsAdapter = new RecentsAdapter(getContext(), filteredRecents);
        recentRecycler.setAdapter(recentsAdapter);
    }

    private void setUpTopPlacesRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        topPlacesRecycler.setLayoutManager(layoutManager);
        topPlacesAdapter = new TopPlacesAdapter(getContext(), filteredTopPlaces);
        topPlacesRecycler.setAdapter(topPlacesAdapter);
    }

    private void fetchRecentsData() {
        recentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recentsList.clear(); // Clear existing list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    if (place != null) {
                        recentsList.add(place); // Add place data to the list
                    }
                }
                filteredRecents.addAll(recentsList);
                recentsAdapter.notifyDataSetChanged(); // Notify adapter about the new data
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle Firebase error
            }
        });
    }

    private void fetchTopPlacesData() {
        topPlacesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                topPlacesList.clear(); // Clear existing list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    if (place != null) {
                        topPlacesList.add(place); // Add place data to the list
                    }
                }
                filteredTopPlaces.addAll(topPlacesList);
                topPlacesAdapter.notifyDataSetChanged(); // Notify adapter about the new data
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle Firebase error
            }
        });
    }

    private void filterData(String query) {
        filteredRecents.clear();
        for (Place place : recentsList) {
            if (place.getPlaceName().toLowerCase().contains(query.toLowerCase()) ||
                    place.getCountryName().toLowerCase().contains(query.toLowerCase())) {
                filteredRecents.add(place);
            }
        }
        recentsAdapter.updateData(filteredRecents);

        filteredTopPlaces.clear();
        for (Place place : topPlacesList) {
            if (place.getPlaceName().toLowerCase().contains(query.toLowerCase()) ||
                    place.getCountryName().toLowerCase().contains(query.toLowerCase())) {
                filteredTopPlaces.add(place);
            }
        }
        topPlacesAdapter.updateData(filteredTopPlaces);
    }
}
