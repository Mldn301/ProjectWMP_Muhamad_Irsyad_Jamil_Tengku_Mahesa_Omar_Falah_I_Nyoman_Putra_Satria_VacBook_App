package com.example.travelbooking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.example.travelbooking.adapter.TicketAdapter;
import com.example.travelbooking.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class UserFragment extends Fragment {

    private SwitchMaterial switchDarkMode;
    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private List<Ticket> ticketList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Reference RecyclerView and SwitchMaterial
        recyclerView = view.findViewById(R.id.recent_recycler);
        switchDarkMode = view.findViewById(R.id.switch_dark);

        // Set RecyclerView layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve data from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", getContext().MODE_PRIVATE);
        String placeName = sharedPreferences.getString("place", "Default Place");
        String countryName = sharedPreferences.getString("country", "Default Country");
        String price = sharedPreferences.getString("price", "Default Price");

        // Add data to the list
        ticketList.add(new Ticket(placeName, countryName, price));

        // Set adapter for RecyclerView
        ticketAdapter = new TicketAdapter(ticketList);
        recyclerView.setAdapter(ticketAdapter);

        // SharedPreferences for dark mode
        SharedPreferences sharedPreferencesDarkMode = requireActivity().getSharedPreferences("settings", getContext().MODE_PRIVATE);
        boolean isDarkMode = sharedPreferencesDarkMode.getBoolean("dark_mode", false);

        // Set initial status of the Switch based on preferences
        switchDarkMode.setChecked(isDarkMode);

        // Listener for the Switch to toggle dark mode
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editorDarkMode = sharedPreferencesDarkMode.edit();
            editorDarkMode.putBoolean("dark_mode", isChecked);
            editorDarkMode.apply(); // Save preferences
        });

        // Admin Button to open AdminActivity
        Button btnAdmin = view.findViewById(R.id.btnAdmin);
        btnAdmin.setOnClickListener(v -> authenticateUser());

        return view;
    }

    // Method for fingerprint authentication
    private void authenticateUser() {
        // Check if the device supports biometric authentication
        BiometricManager biometricManager = BiometricManager.from(requireContext());
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) != BiometricManager.BIOMETRIC_SUCCESS) {
            // Notify the user if authentication is not available
            return;
        }

        // Executor to run the prompt
        Executor executor = ContextCompat.getMainExecutor(requireContext());

        // Create a callback to handle authentication results
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // If authentication is successful, open AdminActivity
                Intent intent = new Intent(getActivity(), AdminActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Show a message if authentication fails
            }
        });

        // Create the authentication prompt
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication Required")
                .setSubtitle("Use your fingerprint to access the Admin page.")
                .setNegativeButtonText("Cancel")
                .build();

        // Show the prompt
        biometricPrompt.authenticate(promptInfo);
    }
}
