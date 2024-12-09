package com.example.travelbooking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private TextView placeNameTextView, countryNameTextView, priceTextView, descriptionTextView;
    private ImageView placeImageView, backButton;
    private Button bookingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Initialize views from XML layout
        placeNameTextView = findViewById(R.id.place_name);
        countryNameTextView = findViewById(R.id.country_name);
        priceTextView = findViewById(R.id.price_text);
        descriptionTextView = findViewById(R.id.description_text);
        placeImageView = findViewById(R.id.place_image);
        backButton = findViewById(R.id.imageView4);
        bookingButton = findViewById(R.id.button);

        // Get the data from the Intent
        Intent intent = getIntent();
        String placeName = intent.getStringExtra("placeName");
        String countryName = intent.getStringExtra("countryName");
        String description = intent.getStringExtra("description");
        String price = intent.getStringExtra("price");
        String imageBase64 = intent.getStringExtra("imageBase64");

        // Set the data into the views
        placeNameTextView.setText(placeName);
        countryNameTextView.setText(countryName);
        priceTextView.setText(price);
        descriptionTextView.setText(description);

        // Set the place image (Base64 decoded)
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            placeImageView.setImageBitmap(bitmap);
        } else {
            placeImageView.setImageResource(R.drawable.image_icon); // Default image if no image is passed
        }

        // Handle the back button click
        backButton.setOnClickListener(v -> onBackPressed());

        // Handle the booking button click
        bookingButton.setOnClickListener(v -> {
            // Start PaymentActivity and pass the necessary data
            Intent paymentIntent = new Intent(DetailsActivity.this, PaymentActivity.class);
            paymentIntent.putExtra("placeName", placeName);
            paymentIntent.putExtra("countryName", countryName);
            paymentIntent.putExtra("price", price);
            startActivity(paymentIntent);

        });
    }
}
