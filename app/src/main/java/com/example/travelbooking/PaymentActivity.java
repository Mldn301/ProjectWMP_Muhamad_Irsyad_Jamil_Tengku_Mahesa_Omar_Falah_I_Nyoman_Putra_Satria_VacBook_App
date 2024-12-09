package com.example.travelbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private Button proceedButton;
    private RadioButton gopayRadio, spayRadio, ovoRadio;
    private TextView placeNameText, priceText, countryNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize views
        proceedButton = findViewById(R.id.proceed_button);
        gopayRadio = findViewById(R.id.btn_gopay);
        spayRadio = findViewById(R.id.btn_spay);
        ovoRadio = findViewById(R.id.btn_ovo);
        placeNameText = findViewById(R.id.place_name_text);
        priceText = findViewById(R.id.price_text);
        countryNameText = findViewById(R.id.country_name_text);

        // Retrieve data from the intent
        String placeName = getIntent().getStringExtra("placeName");
        String price = getIntent().getStringExtra("price");
        String countryName = getIntent().getStringExtra("countryName"); // Retrieve country name

        // Display the retrieved data in the views
        if (placeName != null) {
            placeNameText.setText("Place: " + placeName);
        }
        if (price != null) {
            priceText.setText("Price: " + price);
        }
        if (countryName != null) {
            countryNameText.setText("Country: " + countryName);
        }

        // Default: Disable the "Proceed Payment" button
        proceedButton.setEnabled(false);

        // Add listeners to each RadioButton
        View.OnClickListener radioClickListener = view -> {
            // Enable the button when any RadioButton is selected
            proceedButton.setEnabled(true);
        };

        gopayRadio.setOnClickListener(radioClickListener);
        spayRadio.setOnClickListener(radioClickListener);
        ovoRadio.setOnClickListener(radioClickListener);

        // Set up the "Proceed Payment" button
        proceedButton.setOnClickListener(v -> {
            // Create an Intent to open the ActivityResult and send data to it
            Intent resultIntent = new Intent(PaymentActivity.this, ActivityResult.class);
            resultIntent.putExtra("placeName", placeName);  // Send place name
            resultIntent.putExtra("price", price);  // Send price
            resultIntent.putExtra("countryName", countryName);  // Send country name

            // Start ActivityResult
            startActivity(resultIntent);
        });
    }
}
