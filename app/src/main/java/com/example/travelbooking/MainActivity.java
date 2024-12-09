package com.example.travelbooking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load MainFragment dynamically into the container on initial load
        if (savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mainFragment)  // Replace the container with MainFragment
                    .commit();
        }

        // Bottom navigation handling
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.navi_home) {
                // Load MainFragment when 'Home' is selected
                selectedFragment = new MainFragment();
            } else if (id == R.id.navi_user) {
                // Load UserFragment when 'User' is selected
                selectedFragment = new UserFragment();
            }

            // Replace the current fragment with the selected fragment
            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            return true;
        });
    }

    // Method to replace the current fragment with the new one
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);  // Replace with the selected fragment
        transaction.addToBackStack(null);  // Optional: Add to the back stack for navigation
        transaction.commit();
    }
}
