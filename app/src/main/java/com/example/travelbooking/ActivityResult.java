package com.example.travelbooking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityResult extends AppCompatActivity {

    private TextView titleText, placeText, countryText, priceText;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize views
        titleText = findViewById(R.id.title_text);
        placeText = findViewById(R.id.place);
        countryText = findViewById(R.id.country);
        priceText = findViewById(R.id.price);
        button2 = findViewById(R.id.button2);

        // Retrieve data passed from PaymentActivity
        String placeName = getIntent().getStringExtra("placeName");
        String countryName = getIntent().getStringExtra("countryName");
        String price = getIntent().getStringExtra("price");

        // Set data to respective TextViews
        if (placeName != null) {
            placeText.setText(placeName);
        }
        if (countryName != null) {
            countryText.setText(countryName);
        }
        if (price != null) {
            priceText.setText(price);
        }

        // Save data to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("place", placeName != null ? placeName : "Default Place");
        editor.putString("country", countryName != null ? countryName : "Default Country");
        editor.putString("price", price != null ? price : "Default Price");
        editor.apply(); // Save changes

        // Set button click listener
        button2.setOnClickListener(v -> {
            finish(); // Close ActivityResult
        });
    }
}
