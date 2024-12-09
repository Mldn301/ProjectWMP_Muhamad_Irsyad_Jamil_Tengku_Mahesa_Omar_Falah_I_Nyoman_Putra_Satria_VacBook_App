package com.example.travelbooking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AdminActivity extends AppCompatActivity {

    private EditText etPlaceName, etCountryName, etDescription, etPrice;
    private Spinner spinnerCategory;
    private ImageView imageView;
    private Button btnSelectImage, btnAddPlace, btnViewPlaces;
    private Bitmap selectedImage;

    private static final int SELECT_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        etPlaceName = findViewById(R.id.etPlaceName);
        etCountryName = findViewById(R.id.etCountryName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imageView = findViewById(R.id.imageView);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnAddPlace = findViewById(R.id.btnAddPlace);
        btnViewPlaces = findViewById(R.id.btnViewPlaces);

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE_REQUEST);
        });

        btnAddPlace.setOnClickListener(v -> {
            String placeName = etPlaceName.getText().toString();
            String countryName = etCountryName.getText().toString();
            String description = etDescription.getText().toString();
            String price = etPrice.getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (placeName.isEmpty() || countryName.isEmpty() || description.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImage == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            // Resize image to smaller size (400x400)
            Bitmap resizedImage = resizeBitmap(selectedImage);

            // Convert resized image to Base64 with lower quality (30%)
            String imageBase64 = convertBitmapToBase64(resizedImage);

            if (imageBase64.isEmpty()) {
                Toast.makeText(this, "Image conversion failed", Toast.LENGTH_SHORT).show();
                return;
            }

            addPlaceToFirebase(placeName, countryName, description, imageBase64, price, category);
        });

        btnViewPlaces.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ViewPlacesActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                Log.e("AdminActivity", "Failed to load image", e);
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Resize the image to a smaller size (400x400)
    private Bitmap resizeBitmap(Bitmap bitmap) {
        int newWidth = 400;  // Set width to 400 pixels
        int newHeight = 400; // Set height to 400 pixels
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    // Convert the Bitmap to Base64 string with reduced image quality (30%)
    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream); // Compress image to 30% quality
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Add the place to Firebase
    private void addPlaceToFirebase(String placeName, String countryName, String description, String imageBase64, String price, String category) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref;

        if ("Recents".equals(category)) {
            ref = database.getReference("recents");
        } else if ("TopPlaces".equals(category)) {
            ref = database.getReference("top_places");
        } else {
            Toast.makeText(this, "Invalid category", Toast.LENGTH_SHORT).show();
            return;
        }

        Place place = new Place(placeName, countryName, description, imageBase64, price);

        ref.push().setValue(place).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Place added to " + category + " successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to upload data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
