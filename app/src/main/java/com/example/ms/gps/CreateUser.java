package com.example.ms.gps;

public class CreateUser {
    public String name, password, email, code, isSharing, lat, lng, imageUrl, userId;
    private String status;

    public CreateUser(String name, String password, String email, String code, String isSharing, String lat, String lng, String imageUrl, String userId){
        this.name = name;
        this.password = password;
        this.email = email;
        this.code = code;
        this.isSharing = isSharing;
        this.lat = lat;
        this.lng = lng;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public CreateUser(){}

    public CreateUser(String email, String userId, String lat, String lng, String code, String password, String
                      imageUrl, String name){
        this.email = email;
        this.userId = userId;
        this.lat = lat;
        this.lng = lng;
        this.code = code;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
    }


    public CreateUser(String email, String status){
        this.email = email;
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
