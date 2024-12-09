package com.example.travelbooking;


public class Place {
    private String placeName;
    private String countryName;
    private String description;
    private String imageBase64;
    private String price;


    // Empty constructor require for firebase
    public Place() {
    }

    // Constructor with parameter
    public Place(String placeName, String countryName, String description, String imageBase64, String price) {
        this.placeName = placeName;
        this.countryName = countryName;
        this.description = description;
        this.imageBase64 = imageBase64;
        this.price = price;
    }

    // Getter dan Setter
    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
