package com.example.travelbooking.model;

public class Ticket {
    private String placeName;
    private String countryName;
    private String price;

    // Constructor
    public Ticket(String placeName, String countryName, String price) {
        this.placeName = placeName;
        this.countryName = countryName;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
